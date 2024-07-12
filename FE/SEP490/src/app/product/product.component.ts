import { Component, OnInit, OnDestroy } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { DataService } from '../service/data.service';
import { WishlistService } from '../service/wishlist.service';
import { Subscription } from 'rxjs';
import { error } from 'jquery';
import { ActivatedRoute } from '@angular/router';

interface Category {
  categoryId: number;
  categoryName: string;
}

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})

export class ProductComponent implements OnInit, OnDestroy {
  products: any[] = [];
  currentPage: number = 1;
  categories: Category[] = [];
  searchKey: any = '';
  searchKey2: string = '';
  categoryId?: number;
  selectedCategory: number = 0;
  selectedStatus: number = 0;
  selectedSortByPrice: string = '';
  selectedSortById: string = '';
  obj: any[] = [];
  minPrice: any;
  maxPrice: any;
  private searchKeySubscription: Subscription | undefined;

  constructor(
    private dataService: DataService,
    private productListService: ProductListService,
    private toastr: ToastrService,
    private router: Router,
    private wishList: WishlistService,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.loadCategories();

    this.searchKeySubscription = this.dataService.currentSearchKey.subscribe(searchKey => {
      if (!searchKey) {
        this.getProduct();
      } else {
        this.searchKey = searchKey;
        this.searchProductCustomer();
      }
    });
  }

  ngOnDestroy(): void {
    if (this.searchKeySubscription) {
      this.searchKeySubscription.unsubscribe();
    }
  }

  addToWishlist(productId: number): void {
    this.wishList.addWishlist(productId).subscribe(
      data => {
        console.log('data:', data);
        if (data.code === 1000) {
          this.toastr.success('Sản phẩm đã được thêm vào yêu thích!', 'Thành công');
        } else if (data.code === 1034) {
          this.toastr.success('Sản phẩm đã tồn tại trong danh sách yêu thích!', 'Thành công');
        } else {
          console.log("data.code: ", data.code);
          this.toastr.warning('Vui lòng đăng nhập để thêm sản phẩm yêu thích!', 'Lỗi');
        }
      },
      error => {
        // console.error('error:', error);
        this.toastr.success('Sản phẩm đã được thêm vào yêu thích!', 'Thành công');
      }
    );
  }
  

  validatePriceRange(): void {
    if (this.minPrice > this.maxPrice) {
      this.minPrice = this.maxPrice;
    }
  }

  getProduct(): void {
    this.productListService.getProducts().subscribe(
      data => {
        if (data.code === 1000) {
          this.products = data.result;
        } else {
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      error => {
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }

  loadCategories(): void {
    this.productListService.getAllCategories().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.categories = data.result as Category[];
        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching categories:', error);
      }
    );
  }

  filterProducts(): void {
    this.searchProductCustomer();
  }

  sortProducts(order: string): void {
    this.selectedSortByPrice = order;
    this.searchProductCustomer();
  }

  clearSearchKey(): void {
    this.dataService.clearSearchKey();
  }

  searchProductCustomer(): void {
    const queryParams = {
      searchKey: this.searchKey,
      category: this.selectedCategory,
      status: this.selectedStatus,
      sortByPrice: this.selectedSortByPrice
    };

    const filteredQueryParams = Object.entries(queryParams)
      .filter(([_, value]) => Boolean(value))
      .reduce<Record<string, string>>((obj, [key, value]) => {
        obj[key] = value;
        return obj;
      }, {});

      this.activatedRoute.queryParams.subscribe(params => {
        if (params['searchKey']) { // neu param co searchKey
          this.router.navigate(['/product'], { queryParams: filteredQueryParams });
        } else { // neu param khong co searchKey
          this.searchKey = '';
          this.router.navigate(['/product']);        
        }
      });

    // this.router.navigate(['/product'], { queryParams: filteredQueryParams });
    this.productListService.getMultiFillterProductForCustomer(this.searchKey, this.selectedCategory, this.selectedStatus, this.selectedSortByPrice)
      .subscribe(
        data => {
          if (data.code === 1000) {
            this.products = data.result;
            this.toastr.success('Lọc sản phẩm thành công!', 'Thành công');
          } else if (data.code === 1015) {
            this.products = [];
            this.toastr.error('Không tìm thấy sản phẩm phù hợp!', 'Lọc thất bại');
          }
        },
        error => {
          this.toastr.error('Có lỗi xảy ra!', 'Lọc thất bại');
        }
      );
  }

  onSearch(): void {
    this.dataService.changeSearchKey(this.searchKey);
    this.routerSearch(this.searchKey);
  }

  routerSearch(searchKey: string): void {
    const queryParams = { searchKey };
    this.router.navigate(['/product'], { queryParams });
  }
}
