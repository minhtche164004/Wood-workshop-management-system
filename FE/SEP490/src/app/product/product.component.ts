import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { DataService } from '../service/data.service';
declare var $: any; // Declare jQuery globally
interface Category{
  categoryId: number;
  categoryName: string;
}

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})

export class ProductComponent  implements OnInit  {
  products: any[] = [];
  currentPage: number = 1;
  categories: Category[] = [];
  selectedCategory: number = 0;
  searchKey?: string;
  categoryId?: number;
  minPrice: any;
  maxPrice: any;
  constructor( private dataService: DataService, 
    private productListService: ProductListService, 
    private toastr: ToastrService, 
    private router: Router) { }
  ngOnInit(): void {
    this.loadCategories();
    this.getProduct();
    this.dataService.currentSearchKey.subscribe(searchKey => {
      if (searchKey !== null && searchKey !== undefined) {
        this.searchKey = searchKey;
        console.log('Search key data:', this.searchKey);
        // Bạn có thể gọi hàm tìm kiếm sản phẩm ở đây nếu cần
        this.getProductsSearch();
      } else {
        console.log('Search key is null or undefined, skipping search.');
        // Có thể xử lý hoặc bỏ qua khi searchKey là null
      }
    });
  }
  validatePriceRange() {
    if (this.minPrice > this.maxPrice) {
      this.minPrice = this.maxPrice;
    }
  }
  formSubmit(): void {
    this.getProductsSearch();
  }

  getProductsSearch(): void {
    console.log('Search header:', this.categoryId);
    this.productListService.getMultiFilterProductForCustomer(this.searchKey, this.categoryId, this.minPrice, this.maxPrice).subscribe(
      (data: any) => {
        if (data && Array.isArray(data) && data.length > 0) {
          // Lấy dữ liệu thành công từ response.body
          this.products = data;
          this.toastr.success('Tìm kiếm sản phẩm thành công!', 'Thành công');
          console.log('Kết quả tìm kiếm:', this.products);
        } else {
          // Xử lý khi không có dữ liệu hoặc dữ liệu không hợp lệ
          this.toastr.error('Không tìm thấy sản phẩm nào!', 'Thông báo');
        }
      },
      (error: any) => {
        // Xử lý khi gặp lỗi trong quá trình gọi API
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra khi tìm kiếm sản phẩm!', 'Lỗi');
      }
    );
  }
  

  onPriceRangeChange(event: Event): void {
    // Handle price range change logic here if needed
  }

  onSliderChange(): void {
    console.log('Min price:', this.minPrice);
    console.log('Max price:', this.maxPrice);
    this.getProductsSearch();
  }

  getProduct() {
    this.productListService.getProducts().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          console.log('Danh sách sản phẩm:', this.products);
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
          console.log('Categories:', this.categories);

          // Iterate through categories and log ID and name
          this.categories.forEach(category => {
            console.log(`Category ID: ${category.categoryId}, Category Name: ${category.categoryName}`);
          });
        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching categories:', error); 
      }
    );
  }
  searchProducts(categoryId: number): void {
    this.productListService.getMultiFilterProductForCustomer(this.searchKey, categoryId, this.minPrice, this.maxPrice).subscribe(
      (data: any) => {
        this.toastr.success('Tìm kiếm sản phẩm thành công!', 'Thành công');
        this.products = data;
        console.log('Danh sách sản phẩm sau khi tìm kiếm:', this.products);
      },
      (error: any) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }
}
