
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { timer } from 'rxjs';
import { AuthenListService } from 'src/app/service/authen.service';
import { OrderService } from 'src/app/service/order.service';
import { ProductListService } from 'src/app/service/product/product-list.service';

import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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
  @ViewChild('launchModalButton')
  launchModalButton!: ElementRef;
  user: any[] = [];
  userStatus: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  position: any[] = [];
  status_order: any[] = [];
  selectedCategory: any = null;
  OrderdetailById: any = {};
  isLoadding: boolean = false;
  selectedOrderId: number | null = null;
  selectedSpecialOrder: boolean | null = null;
  activeModal: any;
  constructor(private http: HttpClient, private productListService: ProductListService, private orderService: OrderService,
    private authenListService: AuthenListService, private toastr: ToastrService,
  ) { }

  ngOnInit(): void {

    this.loadPosition();
    this.loadStatus();
    this.getOrderStatus();
    this.getAllUser();

  }
  selectedModalJob: string = '';
  selectedModalId: string = '';
  indexStatus: number = 0;
  previousStatusId: string = '';
  cancelChangeStatusJob() {
    this.selectedModalId = '';
  }

  openModal(orderId: number, event: Event, index: number): void {

    console.log('event:', event);
    const statusId = (event.target as HTMLSelectElement).value;
    this.selectedModalJob = orderId.toString();
    console.log('Job ID:', this.selectedModalJob, 'Status ID:', statusId);
    this.selectedModalId = statusId;
    // this.createJobs.reset();  
    // S? d?ng tham chi?u này d? kích ho?t click
    this.launchModalButton.nativeElement.click();
    this.indexStatus = index;
    // console.log("indexStatus:", this.indexStatus);
  }

  closeModal() {
    this.getAllUser();
  }
  getOrderStatus(): void {
    this.orderService.getOrderStatus().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.userStatus = data.result;
          //   console.log('Danh sách status don hang', this.userStatus);
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
    this.isLoadding = true;
    this.loginToken = localStorage.getItem('loginToken');

    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.productListService.getAllOrder().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.user = data.result;
            this.isLoadding = false;

            //      console.log('Danh sách order:', this.user);


          } else {
            console.error('Failed to fetch products:', data);
            this.isLoadding = false;

          }
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.isLoadding = false;

        }
      );
    } else {
      console.error('No loginToken found in localStorage.');
      this.isLoadding = false;

    }
  }
  loadPosition(): void {
    this.productListService.getAllPosition().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.position = data.result;
          //  console.log('Danh sách Loại:', this.position);
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
          //    console.log('Danh sách trạng thái:', this.status_order);
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
    this.isLoadding = true;

    this.authenListService.getOrderDetailById(order_detail_id).subscribe(
      (data) => {
        this.OrderdetailById = data.result;
        this.isLoadding = false;

        //  console.log('OrderdetailById:', this.OrderdetailById);
      },
      (error) => {
        console.error('Error fetching user data:', error);
        this.isLoadding = false;

      }
    );

  }
  selectedOrder: any = {};
  getOrderDetail(order: any) {
    this.selectedOrder = order;
    console.log('Order:', this.selectedOrder);
  }
  onStatusChange(orderId: string, event: Event): void {
    const statusId = (event.target as HTMLSelectElement).value;
    this.changeStatus(orderId, statusId);
  }

  changeStatus(orderId: string, statusId: string): void {
    this.isLoadding = true;
    this.authenListService.changeStatusOrder(orderId, statusId).subscribe(
      response => {
        this.isLoadding = false;
        console.log('Order status changed', response);
        this.toastr.success('Thay đổi tình trạng công việc thành công.');
        this.getAllUser();
        $('[data-dismiss="modal"]').click();
      },
      error => {
        this.isLoadding = false;

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
    console.log(this.selectedCategory);
    this.isLoadding = true;


    const selectedStatusOption = this.userStatus.find(status => status.status_id === this.selectedCategory);
    if (selectedStatusOption !== null) {
      this.authenListService.getFilterStatus(this.selectedCategory)
        .subscribe(
          (data) => {
            if (data.code === 1000) {
              this.user = data.result;
              this.isLoadding = false;
              this.toastr.success('Lọc đơn hàng thành công!', 'Thành công');
            } else if (data.code === 1015) {
              this.user = [];
              console.error('Lọc đơn hàng không thành công:', data);
              this.isLoadding = false;
              this.toastr.error('Không tìm thấy đơn hàng phù hợp!', 'Lọc thất bại');
            }
          },
          
        );
    } else {
      // The selected category doesn't exist in the userStatus array, so we'll fetch all user data
      this.getAllUser();
      this.isLoadding = false;
      this.toastr.success('Lọc đơn hàng thành công!', 'Thành công');
    }

  }
  setOrderForCancellation(orderId: number | null, specialOrder: boolean | null) {
    this.selectedOrderId = orderId;
    this.selectedSpecialOrder = specialOrder;
  }
  confirmCancel() {
    this.isLoadding = true;
    if (this.selectedOrderId !== null && this.selectedSpecialOrder !== null) {
      this.authenListService.cancelOrder(this.selectedOrderId, this.selectedSpecialOrder).subscribe({
        next: (response: string) => {
          this.toastr.success(response, 'Success');
          // Close the modal
          this.isLoadding = false;
          const closeModalButton = document.querySelector('.btn-close') as HTMLElement;
          if (closeModalButton) {
            closeModalButton.click();
          }
        },
        error: (error: HttpErrorResponse) => {
          if (error.status === 400) {
            const errorMessage = typeof error.error === 'string' ? error.error : 'Unexpected error';
            this.isLoadding = false;
            this.toastr.warning(errorMessage, 'Lỗi');
          } else {
            const responseText = error.error.text || 'Unexpected error';
            this.isLoadding = false;
            this.toastr.success(responseText);
            timer(1000).subscribe(() => {
              window.location.reload();
            });
          }
        }
      });
    }
  }
}
