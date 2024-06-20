import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr'; // Import ToastrService

interface ApiResponse {
  code: number;
  result: any[]; // Or a specific interface for the result structure
}

@Component({
  selector: 'app-product-management',
  templateUrl: './product-management.component.html',
  styleUrls: ['./product-management.component.scss']
})
export class ProductManagementComponent implements OnInit {

  products: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;

  searchKey: string = '';
  categories: any[] = [];
  selectedCategory: any = null;

  constructor(private productListService: ProductListService, private toastr: ToastrService) { } // Inject ToastrService

  ngOnInit(): void {
    this.loginToken = localStorage.getItem('loginToken');
    this.loadCategories();

    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.productListService.getProducts().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.products = data.result;
            console.log('Danh sách sản phẩm:', this.products);
          } else {
            console.error('Failed to fetch products:', data);
            this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi'); // Display error toast
          }
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Display generic error toast
        }
      );
    } else {
      console.error('No loginToken found in localStorage.');
    }
  }

  loadCategories(): void {
    this.productListService.getAllCategories().subscribe(
      (data: any) => {
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

  searchProduct(): void {
    console.log("Thực hiện tìm kiếm sản phẩm");
    if (this.searchKey) {
      this.productListService.findProductByNameOrCode(this.searchKey)
        .subscribe(
          (data) => {
            if (data.code === 1000) {
              this.products = data.result;
              console.log('Tìm kiếm thành công:', this.products);
              this.toastr.success('Tìm kiếm sản phẩm thành công!', 'Thành công');
            } else if (data.code === 1015) {
              this.products = []; // Clear previous results
              console.error('Tìm kiếm không thành công:', data);
              this.toastr.error('Không tìm thấy sản phẩm!', 'Tìm kiếm thất bại');
              // Handle specific error message
              if (data.message.includes('Không tìm thấy kết quả tìm kiếm')) {
                console.error('Lỗi tìm kiếm: Không tìm thấy sản phẩm phù hợp.');
                // Optionally, you can display a more specific message to the user
                // based on the error details in data.message.
              }
            } else {
              console.error('Mã trả về không hợp lệ:', data.code);
              this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Display generic error toast
            }
          },
          (error) => {
            console.error('Lỗi khi tìm kiếm sản phẩm:', error);
            this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Display generic error toast
          }
        );
    } else {
      console.warn('Từ khóa tìm kiếm trống.');
      // Optionally display a message to the user indicating an empty search term
    }
  }
  

  getProductDetails(productId: number): void {
    this.productListService.getProductById(productId) // Assuming your API endpoint
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            // Update data or display modal with detailed information
            console.log('Product details:', data.result); // For debugging
          } else {
            console.error('Failed to fetch product details:', data);
          }
        },
        (error) => {
          console.error('Error fetching product details:', error);
        }
      );
  }


}