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
  searchKey: string = '' ;
  searchKey2: string = '';
  categoryId?: number;
  selectedCategory: number = 0;
  selectedStatus: number = 0;
  // selectedType: number = 0;

  selectedSortByPrice: string = '';
  selectedSortById: string = '';
  
  minPrice: any;
  maxPrice: any;
  constructor( private dataService: DataService, 
    private productListService: ProductListService, 
    private toastr: ToastrService, 
    private router: Router) { }
  ngOnInit(): void {
    this.loadCategories();
    this.getProduct();
    const sort = '';
    
    this.dataService.currentSearchKey.subscribe(searchKey => {
      if (searchKey !== undefined) {
        this.searchKey = searchKey; // Store searchKey in a class property
        console.log('Received search key:', this.searchKey);
        this.searchProductCustomer(); // Call method using stored searchKey
      }
 
    
    });
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

    this.productListService.getMultiFillterProductForCustomer(this.searchKey, this.selectedCategory, this.selectedStatus, this.selectedSortByPrice)
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.products = data.result;
            console.log('Lọc sản phẩm thành công:', this.products);
            this.toastr.success('Lọc sản phẩm thành công!', 'Thành công');
          } else if (data.code === 1015) {
            this.products = [];
            console.error('Lọc sản phẩm không thành công:', data);
            this.toastr.error('Không tìm thấy sản phẩm phù hợp!', 'Lọc thất bại');
          }
        }
      );
  }
}
