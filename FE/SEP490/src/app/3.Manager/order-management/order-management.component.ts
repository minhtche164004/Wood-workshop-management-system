
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { timer } from 'rxjs';
import { AuthenListService } from 'src/app/service/authen.service';
import { OrderService } from 'src/app/service/order.service';
import { ProductListService } from 'src/app/service/product/product-list.service';

import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { OrderRequestService } from 'src/app/service/order-request.service';
import { data } from 'jquery';
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
  selectedCategory: number = 0;
  OrderdetailById: any = {};
  isLoadding: boolean = false;
  selectedOrderId: number | null = null;
  selectedSpecialOrder: boolean | null = null;
  activeModal: any;
  cancelReason: string = '';
  constructor(private http: HttpClient, private productListService: ProductListService, private orderService: OrderService,
    private authenListService: AuthenListService, private toastr: ToastrService, private orderRequestService: OrderRequestService
  ) { }

  ngOnInit(): void {

    this.loadPosition();
    this.loadStatus();
    this.getOrderStatus();
    this.getAllOrder();

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
  closeModal(event: Event) {
    // const statusId = (event.target as HTMLSelectElement).value;
    // console.log('Status IDClóe:', statusId);
    // const element = document.getElementById("mySelect" + this.indexStatus);
    // if (element instanceof HTMLSelectElement && element !== null) {
    //   // Reset the selected index to the initial value
    //   element.selectedIndex = parseInt(statusId);
      
    // }
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
  getAllOrder(): void {
    this.isLoadding = true;
    this.loginToken = localStorage.getItem('loginToken');

    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.productListService.getAllOrder().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.user = data.result;
            this.isLoadding = false;
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
  realoadgetAllUser(): void {
    this.isLoadding = true;
    this.productListService.getAllOrder().subscribe(
      (data: ApiResponse) => {
        if (data.code === 1000) {
          this.user = data.result;
          this.isLoadding = false;

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
  productOfOrder: any = [];

  getOrDetailById(us: any, order_detail_id: string): void {
    //  this.isLoadding = true;
    //   console.log('Order_detail_id:', order_detail_id);
    this.authenListService.getOrderDetailById(order_detail_id).subscribe(
      (data) => {
        this.OrderdetailById = data.result;
        this.isLoadding = false;
        console.log('OrderdetailById tối 20/7:', data.result);
        // console.log('OrderdetailById:', this.OrderdetailById);
      },
      (error) => {
        console.error('Error fetching user data:', error);
        this.isLoadding = false;

      }
    );
    console.log("order detail: ", us)
    console.log("order detail id: ", us.orderId)

    this.orderRequestService.getAllOrderDetailByOrderId(us.orderId).subscribe(
      (data) => {
        this.productOfOrder = data.result;
        this.isLoadding = false;

        console.log('Product Orders:', this.productOfOrder);
      },
      (error) => {
        console.error('Error fetching user data:', error);
        this.isLoadding = false;

      }
    );
  }
  selectedOrder: any = {};
  getOrderDetail(orderId: number): void {

    console.log('OrderID:', this.selectedOrder);
  }
  onStatusChange(orderId: string, event: Event): void {
    const statusId = (event.target as HTMLSelectElement).value;
    this.changeStatus(orderId, statusId);
  }

  changeStatus(orderId: string, statusId: string): void {
    this.isLoadding = true;
    this.authenListService.changeStatusOrder(orderId, statusId).subscribe(
      response => {
        this.realoadgetAllUser();
        console.log('Order status changed', response);
        this.toastr.success('Thay đổi tình trạng  thành công.');

        $('[data-dismiss="modal"]').click();
        this.isLoadding = false;
      },
      error => {
        this.isLoadding = false;

        console.error('Error changing order status', error);
        $('[data-dismiss="modal"]').click();
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
    if (this.selectedCategory !== 0) {
      this.authenListService.getFilterStatus(this.selectedCategory)
        .subscribe(
          (data) => {
            if (data.code === 1000) {
              this.currentPage = 1;
              this.user = data.result;
              this.isLoadding = false;

            } else if (data.code === 1015) {
              this.realoadgetAllUser();

            }

          },
        );
    }

  }
  setOrderForCancellation(orderId: number | null, specialOrder: boolean | null) {
    this.selectedOrderId = orderId;
    this.selectedSpecialOrder = specialOrder;
  }
  setOrderForPayment(orderId: number) {
    this.selectedOrderId = orderId;
   
  }
  confirmCancel() {
    this.isLoadding = true;
    if (this.selectedOrderId !== null && this.selectedSpecialOrder !== null) {
      this.authenListService.cancelOrder(this.selectedOrderId, this.selectedSpecialOrder, this.cancelReason).subscribe({
        next: (response) => {

          this.toastr.success('Hủy đơn hàng thành công');
          this.isLoadding = false;
          const closeModalButton = document.querySelector('.close') as HTMLElement;
          if (closeModalButton) {
            closeModalButton.click();
          }
          $('[data-dismiss="modal"]').click();
        },
        error: (error: HttpErrorResponse) => {
          this.isLoadding = false;
          this.toastr.success('Hủy đơn hàng thành công');
          this.realoadgetAllUser();
          $('[data-dismiss="modal"]').click();
        }
      });
    }
  }
  confirmPayment() {
    this.isLoadding = true;
    if (this.selectedOrderId !== null) {
      this.authenListService.Paymentmoney(this.selectedOrderId).subscribe({
        next: (response: any ) => {
         if(response.code == 1000){
          this.toastr.success(response.result);
          this.realoadgetAllUser();
    
          this.isLoadding = false;
          const closeModalButton = document.querySelector('.close') as HTMLElement;
          if (closeModalButton) {
            closeModalButton.click();
          }
          $('[data-dismiss="modal"]').click();
        }},
        error: (error: HttpErrorResponse) => {
          this.isLoadding = false;
          this.toastr.error('Thanh toán thất bại');
          this.realoadgetAllUser();
          $('[data-dismiss="modal"]').click();
        }
      });
    }
  }
  
}
