
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { timer } from 'rxjs';
import { AuthenListService } from 'src/app/service/authen.service';
import { OrderService } from 'src/app/service/order.service';
import { ProductListService } from 'src/app/service/product/product-list.service';

import { HttpClient } from '@angular/common/http';
interface ApiResponse {
  code: number;
  result: any[];
}

@Component({
  selector: 'app-order-management',
  templateUrl: './order-management.component.html',
  styleUrls: ['./order-management.component.scss']
})
export class OrderManagementComponent implements OnInit {
  user: any[] = [];
  userStatus: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  position: any[] = [];
  status_order: any[] = [];
  selectedCategory: any = null;
  OrderdetailById: any = {};

  constructor(private http: HttpClient,private productListService: ProductListService, private orderService: OrderService,
    private authenListService: AuthenListService,    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {

    this.loadPosition();
    this.loadStatus();
    this.getOrderStatus();
    this.getAllUser();
 
  }
  getOrderStatus(): void {
    this.orderService.getOrderStatus().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.userStatus = data.result;
          console.log('Danh sách status don hang', this.userStatus);
        } else {
          console.error('Dữ liệu trả về không hợp lệ:', data);
        }
      },
      (error) => {
        console.error('Lỗi khi lấy danh sách người dùng:', error);
      }
    );
  }
  getAllUser(): void {
    this.loginToken = localStorage.getItem('loginToken');

    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.productListService.getAllOrder().subscribe(
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
  loadStatus(): void {
    this.authenListService.getAllStatusOrder().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.status_order = data.result;
          console.log('Danh sách trạng thái:', this.status_order);
        } else {
          console.error('Dữ liệu trả về không hợp lệ:', data);
        }
      },
      (error) => {
        console.error('Lỗi khi lấy danh sách Loại:', error);
      }
    );

  }
  getOrDetailById(order_detail_id: string): void {
    this.authenListService.getOrderDetailById(order_detail_id).subscribe(
      (data) => {
        this.OrderdetailById = data.result;
        console.log('OrderdetailById:', this.OrderdetailById);
      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    );

  }
  
  onStatusChange(orderId: string, event: Event): void {
    const statusId = (event.target as HTMLSelectElement).value;
    this.changeStatus(orderId, statusId);
  }

  changeStatus(orderId: string, statusId: string): void {
    this.authenListService.changeStatusOrder(orderId, statusId).subscribe(
      response => {
        console.log('Order status changed', response);
        this.toastr.success('Thay đổi tình trạng công việc thành công.');
        // timer(200).subscribe(() => {
        //   window.location.reload();
        // });
      },
      error => {
        console.error('Error changing order status', error);
      }
    );
  }
  getStatusColor(statusId: number): { [key: string]: string } {
    switch (statusId) {
      case 1:
        return { 'color': 'red' };
      case 3:
        return { 'color': 'blue' };
      case 4:
        return { 'color': 'green' };
      case 5:
        return { 'color': 'purple' };
      default:
        return { 'color': 'black' };
    }
  }
  
  filterStatus(): void {
    console.log( this.selectedCategory);

    this.authenListService.getFilterStatus(this.selectedCategory)
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.user = data.result;
            console.log('Lọc đơn hàng thành công:', this.user);
            this.toastr.success('Lọc đơn hàng thành công!', 'Thành công');
          } else if (data.code === 1015) {
            this.user = [];
            console.error('Lọc đơn hàng không thành công:', data);
            this.toastr.error('Không tìm thấy đơn hàng phù hợp!', 'Lọc thất bại');
          }
        }
      );
  }
  
  cancelOrder(orderId: number,specialOrderId: number) {
    this.authenListService.cancelOrder(orderId,specialOrderId).subscribe(
      (response) => {
        this.toastr.success('Hủy đơn hàng thành công!', 'Thành công');
        console.log('Order canceled successfully');
      },
      (error) => {
        this.toastr.error('Hủy đơn hàng không thành công!', 'Thành công');
        console.error('Error canceling order:', error);
      }
    );
  }
}
