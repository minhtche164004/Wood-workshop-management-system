import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { DataService } from '../service/data.service';
import { WishlistService } from '../service/wishlist.service';
declare var $: any; // Declare jQuery globally
interface Category {
  categoryId: number;
  categoryName: string;
}

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})

export class ProductComponent implements OnInit {
  products: any[] = [];
  currentPage: number = 1;
  categories: Category[] = [];
  searchKey: any = '';
  searchKey2: string = '';
  categoryId?: number;
  selectedCategory: number = 0;
  selectedStatus: number = 0;
  // selectedType: number = 0;

  selectedSortByPrice: string = '';
  selectedSortById: string = '';
  obj: any[] = [];
  minPrice: any;
  maxPrice: any;
  constructor( private dataService: DataService, 
    private productListService: ProductListService, 
    private toastr: ToastrService, 
    private router: Router, private wishList: WishlistService) { }
  ngOnInit(): void {
    this.loadCategories();


    this.dataService.currentSearchKey.subscribe(searchKey => {
      if (searchKey === null) {
        console.log('Received search key:', searchKey);
        // If searchKey is null or empty, call getProduct to fetch all products
        this.getProduct();
      } else {
        this.searchKey = searchKey; // Store searchKey in a class property
        console.log('Received search key:', this.searchKey);
        this.searchProductCustomer(); // Call method using stored searchKey
      }
    });
  }
  addToWishlist(productId: number) {  
    this.wishList.addWishlist(productId)
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            console.log('Product added to wishlist:');
            this.toastr.success('Sản phẩm đã được thêm vào yêu thích!', 'Thành công'); // Success message

          }else{
            this.toastr.warning('Vui lòng đăng nhập để thêm sản phẩm yêu thích!', 'Lỗi'); // Error message
          }
        },
        (error) => {
          console.error('Error adding product to wishlist:', error);
          this.toastr.warning('Vui lòng đăng nhập để thêm sản phẩm yêu thích!', 'Lỗi'); // Error message
        }
      );
  }
  validatePriceRange() {
    if (this.minPrice > this.maxPrice) {
      this.minPrice = this.maxPrice;
    }
  }
 

  
  

  onPriceRangeChange(event: Event): void {
    // Handle price range change logic here if needed
  }

  onSliderChange(): void {
    console.log('Min price:', this.minPrice);
    console.log('Max price:', this.maxPrice);
   
  }

  getProduct() {
    this.productListService.getProducts().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          //    console.log('Danh sách sản phẩm:', this.products);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }
  loadCategories(): void {
    this.productListService.getAllCategories().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.categories = data.result as Category[];

          // Log categories to console for verification
          //   console.log('Categories:', this.categories);

          // Iterate through categories and log ID and name

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
  sortProducts(order: string) {
    this.selectedSortByPrice = order;
    console.log('Sắp xếp sản phẩm theo:', order);
    this.searchProductCustomer();

  }


  searchProductCustomer(): void {
    console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory, "và giá:", this.selectedSortByPrice);
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

    this.router.navigate(['/product'], { queryParams: filteredQueryParams });
    this.productListService.getMultiFillterProductForCustomer(this.searchKey, this.selectedCategory, this.selectedStatus, this.selectedSortByPrice)
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.products = data.result;
            // console.log('Lọc sản phẩm thành công:', this.products);
        //    this.toastr.success('Lọc sản phẩm thành công!', 'Thành công');
          } else if (data.code === 1015) {
            this.products = [];
            //   console.error('Lọc sản phẩm không thành công:', data);
            this.toastr.error('Không tìm thấy sản phẩm phù hợp!', 'Lọc thất bại');
          }
        }
      );
  }
}
