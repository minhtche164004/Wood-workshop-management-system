import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
interface ApiResponse {
  code: number;
  result: any[]; // hoặc bạn có thể sử dụng một interface riêng để định nghĩa cấu trúc của mảng result
}

@Component({
  selector: 'app-product-management',
  templateUrl: './product-management.component.html',
  styleUrls: ['./product-management.component.scss']
})
export class ProductManagementComponent implements OnInit {
  products: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1; // Biến lưu trữ trang hiện tại


  categories: any[] = [];
  selectedCategory: any = null;



  constructor(private productListService: ProductListService ) { }

  ngOnInit(): void {

   

    // Lấy loginToken từ localStorage
    this.loginToken = localStorage.getItem('loginToken');
    this.loadCategories();
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);

      // Gọi dịch vụ để lấy danh sách sản phẩm
      this.productListService.getProducts().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.products = data.result;
            console.log('Danh sách sản phẩm:', this.products); // In ra danh sách sản phẩm vào console
          } else {
            console.error('Failed to fetch products:', data);
          }
        },
        (error) => {
          console.error('Error fetching products:', error);
        }
      );
    } else {
      console.error('No loginToken found in localStorage.');
    }
  }
  loadCategories(): void {
    this.productListService.getAllCategories().subscribe(
      (data: any) => { // them any vao`
        if (data.code === 1000) {
          this.categories = data.result;
          console.log('Danh sách Loại:', this.categories);
        } else {
          console.error('Dữ liệu trả về không hợp lệ:', data);
        }
      },
      (error) => {
        console.error('Lỗi khi lấy danh sách Loại:', error);
      }
    );
  }
    
}
