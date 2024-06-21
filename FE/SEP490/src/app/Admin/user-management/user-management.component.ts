import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';


interface ApiResponse {
  code: number;
  result: any[];
}

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {
  user: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  position: any[] = [];
  selectedCategory: any = null;


  constructor(private productListService: ProductListService) { }

  ngOnInit(): void {
    this.loginToken = localStorage.getItem('loginToken');
    this.loadPosition();

    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.productListService.getAllUser().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.user = data.result;



            console.log('Danh sách người dùng:', this.user);

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

  loadPosition(): void {
    this.productListService.getAllPosition().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.position = data.result;
          console.log('Danh sách Loại:', this.position);
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
