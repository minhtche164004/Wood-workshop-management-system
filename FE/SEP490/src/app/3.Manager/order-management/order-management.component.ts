
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { timer } from 'rxjs';
import { AuthenListService } from 'src/app/service/authen.service';
import { OrderService } from 'src/app/service/order.service';
import { ProductListService } from 'src/app/service/product/product-list.service';

import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { OrderRequestService } from 'src/app/service/order-request.service';
import { data } from 'jquery';
import { formatDate } from '@angular/common';
import { Binary } from '@angular/compiler';
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
  selectedCategory: string = '';
  selectedSDate: string = '';
  selectedEDate: string = '';
  selectProduduct: number = -1;

  OrderdetailById: any = {};
  isLoadding: boolean = false;
  selectedC: number | null = null;
  selectedOrderId: number | null = null;

  selectedSpecialOrder: boolean | null = null;
  activeModal: any;
  cancelReason: string = '';
  cancelReasonPrice: string = '';
  percentDepositPrice: number = 0;
  percentOrderPrice: number = 0;
  depositeAmount: number = 0;
  totalAmountA: number = 0;
  totalAmount: number = 0;
  totalRefundAmount: number = 0;
  totalAmount102: number = 0;

 

  priceDiscount: number = 0;
  datepriceDiscount: string = '';



  constructor(private http: HttpClient, private productListService: ProductListService, private orderService: OrderService,
    private authenListService: AuthenListService, private toastr: ToastrService, private orderRequestService: OrderRequestService
  ) {

  }
  calculateTotalAmount() {
    this.totalAmount = this.depositeAmount + this.totalAmountA;
  }
  updateDepositeAmount() {
    if (this.percentDepositPrice >= 0 && this.percentDepositPrice <= 100) {
      this.depositeAmount = (this.OrderdetailById.deposite * this.percentDepositPrice) / 100;
      this.updateTotalRefundAmount();
    }
  }

  // Update the total amount and calculate the total refund
  updateTotalAmount() {
    if (this.percentOrderPrice >= 0 && this.percentOrderPrice <= 100) {
      this.totalAmountA = (this.OrderdetailById.totalAmount * this.percentOrderPrice) / 100;
      this.updateTotalRefundAmount();
    }
  }

  // Calculate the total refund amount
  updateTotalRefundAmount() {
    // Calculate deposit amount
    if (this.percentDepositPrice >= 0 && this.percentDepositPrice <= 100) {
      this.depositeAmount = (this.OrderdetailById.deposite * this.percentDepositPrice) / 100;
    }

    // Calculate total amount
    if (this.percentOrderPrice >= 0 && this.percentOrderPrice <= 100) {
      this.totalAmountA = (this.OrderdetailById.totalAmount * this.percentOrderPrice) / 100;
    }

    // Update total refund amount
    this.totalRefundAmount = this.depositeAmount + this.totalAmountA;
  }



  ngOnInit(): void {
    this.updateDepositeAmount();
    this.updateTotalAmount();
    this.calculateTotalAmount();
    this.loadPosition();
    this.loadStatus();
    this.getOrderStatus();
    this.getAllOrder();

  }
  searchKey: string = '';
  selectedModalJob: string = '';
  selectedModalId: string = '';
  indexStatus: number = 0;
  previousStatusId: string = '';
  contactUser(phoneNumber?: string): void {
    console.log(phoneNumber);
    if (phoneNumber) {
      window.location.href = `https://zalo.me/${phoneNumber}`;
    } else {
      console.error('Phone number is not available.');
    }
  }

  tempTotalAmount: number = 0;

  cancelRefundModal() {
    this.percentDepositPrice = 0;
    this.percentOrderPrice = 0;
    // Reset form fields
    this.OrderdetailById.deposite = 0;
    this.depositeAmount = 0;
    this.OrderdetailById.totalAmount = 0;
    this.totalAmountA = 0;
    this.totalRefundAmount = 0;
    this.cancelReasonPrice = '';

    // If you are using Angular Forms, you may need to reset the form
    // this.refundForm.reset(); // Uncomment this if you are using FormGroup


  }

  cancelChangeStatusJob() {
    this.selectedModalId = '';
  }
  openModal(orderId: number, event: Event, index: number): void {
    const statusId = (event.target as HTMLSelectElement).value;
    this.selectedModalJob = orderId.toString();
    this.selectedModalId = statusId;
    this.indexStatus = index;

    console.log('event:', event);
    console.log('Job ID:', this.selectedModalJob, 'Status ID:', statusId);

    // Trigger the modal open action
    this.launchModalButton.nativeElement.click();
  }

  closeModal(event: Event): void {
    // const statusId = (event.target as HTMLSelectElement).value;
    // const selectedStatusOption = this.status_order.find(status => status.status_id == parseInt(statusId));
    // const element = document.getElementById("mySelect" + this.indexStatus) as HTMLSelectElement;
    // if (element) {
    //   element.value = statusId;
    // }
    this.refilterStatus();
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
            this.user = data.result.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
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

    this.productListService.getAllOrder().subscribe(
      (data: ApiResponse) => {
        if (data.code === 1000) {
          this.user = data.result;


        } else {
          console.error('Failed to fetch products:', data);


        }
      },
      (error) => {
        console.error('Error fetching products:', error);




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
  totalAmoutOrder: number = 0;
  selectedOrderDetail: any = {};
  getOrDetailById(us: any, order_detail_id: string): void {
    this.isLoadding = true;
    console.log('Order_detail_id:', order_detail_id);
    console.log("order detail: ", us)
    console.log("order detail type: ", us.specialOrder)
    this.totalAmoutOrder = us.totalAmount
    this.selectedOrderDetail = us;
    this.authenListService.getOrderDetailById(us.orderId).subscribe(
      (data) => {
        this.OrderdetailById = data.result;
        this.isLoadding = false;
         console.log('OrderdetailById:', data.result);
        // console.log('OrderdetailById:', this.OrderdetailById);
      },
      (error) => {
        console.error('Error fetching user data:', error);
        this.isLoadding = false;

      }
    );
    if (us.specialOrder == true) {
      this.orderRequestService.getAllOrderDetailByOrderId(order_detail_id).subscribe(
        (data) => {
          this.productOfOrder = data.result;
          //  console.log('Product Orders:', this.productOfOrder);
        },
        (error) => {
          console.error('Error fetching user data:', error);


        }
      );
    } else if (us.specialOrder == false) {
      this.orderRequestService.getAllOrderDetailOfProductByOrderId(order_detail_id).subscribe(
        (data) => {
          this.productOfOrder = data.result;
          console.log('Product Flase:', this.productOfOrder);
        },
        (error) => {
          console.error('Error fetching user data:', error);
        }
      );
    }
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
        this.isLoadding = false;
        console.log('Order status changed', response);
        this.toastr.success('Thay đổi tình trạng  thành công.');

        $('[data-dismiss="modal"]').click();

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
    let startDate: string = this.selectedSDate || '';
    let endDate: string = this.selectedEDate || '';
    if (this.selectedSDate) {
      startDate = this.selectedSDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }

    if (this.selectedEDate) {
      endDate = this.selectedEDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }
    console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory,
      "DateS:", startDate, "DateE:", endDate, "DateE:", this.selectProduduct
    );
    this.authenListService.getFilterStatus(
      this.searchKey.trim(),
      this.selectedCategory,
      this.selectProduduct,
      startDate,
      endDate


    )
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.currentPage = 1;
            this.user = data.result;
            console.log(this.user);
            this.isLoadding = false;

          } else if (data.code === 1015) {
            this.user = [];
            this.isLoadding = false;
            // this.toastr.warning(data.message);
          }
        },
        (error: HttpErrorResponse) => {
          this.isLoadding = false;
          this.toastr.error('Có lỗi xảy ra, vui lòng thử lại sau');


        }
      );
  }
  refilterStatus(): void {

    console.log(this.selectedCategory);


    let startDate: string = this.selectedSDate || '';
    let endDate: string = this.selectedEDate || '';
    if (this.selectedSDate) {
      startDate = this.selectedSDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }

    if (this.selectedEDate) {
      endDate = this.selectedEDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }

    this.authenListService.getFilterStatus(
      this.searchKey.trim(),
      this.selectedCategory,
      this.selectProduduct,
      startDate,
      endDate

    )
      .subscribe(
        (data) => {
          if (data.code === 1000) {

            this.user = data.result;


          } else if (data.code === 1015) {
            this.user = [];


            // this.toastr.warning(data.message);

          }
        },
        (error: HttpErrorResponse) => {

          // this.toastr.error('Có lỗi xảy ra, vui lòng thử lại sau');


        }
      );

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
  RefundcancelOrder() {
    // Reset loading state and close modal in case of failure
    const closeModal = () => {
      this.isLoadding = false;
      const closeModalButton = document.querySelector('.close') as HTMLElement;
      if (closeModalButton) {
        closeModalButton.click();
      }
      $('[data-dismiss="modal"]').click();
    };

    // Validate percentage fields
    const isValidPercentage = (value: number | null): boolean => {
      if (value === null || value === undefined || isNaN(value)) {
        return false;
      }
      const percentageString = value.toString().trim();
      const percentageRegex = /^(100(\.0{1,2})?|(\d{1,2})(\.\d{1,2})?)$/;
      return percentageRegex.test(percentageString) && parseFloat(percentageString) >= 0;
    };

    const percentDepositPriceTrimmed = this.percentDepositPrice !== null ? this.percentDepositPrice.toString().trim() : '';
    const percentOrderPriceTrimmed = this.percentOrderPrice !== null ? this.percentOrderPrice.toString().trim() : '';
    const cancelReasonPriceTrimmed = this.cancelReasonPrice ? this.cancelReasonPrice.trim() : '';

    if (percentDepositPriceTrimmed === '' || percentOrderPriceTrimmed === '' || cancelReasonPriceTrimmed === '') {
      this.toastr.error('Các trường không được để trống');
      return;
    }

    if (!isValidPercentage(this.percentDepositPrice) || !isValidPercentage(this.percentOrderPrice)) {
      this.toastr.error('Phần trăm phải nằm trong khoảng 0-100% và không được là số âm');
      return;
    }

    this.isLoadding = true;
    console.log({
      selectedOrderId: this.selectedOrderId,
      selectedSpecialOrder: this.selectedSpecialOrder,
      cancelReasonPrice: this.cancelReasonPrice,
      percentDepositPrice: this.percentDepositPrice,
      percentOrderPrice: this.percentOrderPrice
    });

    if (this.selectedOrderId !== null && this.selectedSpecialOrder !== null) {
      this.authenListService.RefundcancelOrder(
        this.selectedOrderId, this.selectedSpecialOrder,
        this.cancelReasonPrice, this.percentDepositPrice, this.percentOrderPrice
      ).subscribe({
        next: (response: any) => {
          if (response.code == 1000) {
          this.toastr.success('Hoàn tiền đơn hàng thành công');
          this.isLoadding = false;
          this.cancelRefundModal();
          closeModal();
        }},
        error: (error: HttpErrorResponse) => {
          this.isLoadding = false;
          this.toastr.success('Hoàn tiền đơn hàng thất bại');
          console.log(error);
          this.realoadgetAllUser();
          this.cancelRefundModal();
          closeModal();
        }
      });
    } else {
      this.toastr.error('Thông tin đơn hàng không hợp lệ');
      this.isLoadding = false;
    }
  }
  depositeOrder: number = 0;
  formattedValue: string = '0';

  // Called when the user types in the input field
  onInputChange(value: string) {
    // Remove commas and convert to a number
    const numericValue = parseFloat(value.replace(/,/g, ''));
    this.depositeOrder = isNaN(numericValue) ? 0 : numericValue;

    // Update formatted value
    this.formattedValue = this.formatInputValue(this.depositeOrder);
  }

  formatInputValue(value: number): string {
    if (isNaN(value)) return '0';
    // Format the number with commas
    return value.toLocaleString();
  }
  confirmPayment() {
    this.isLoadding = true;
    const formattedDepositOrder = this.depositeOrder.toFixed(2); // Ví dụ: '4000000.00'
    if (this.selectedOrderId !== null) {
      this.authenListService.Paymentmoney(this.selectedOrderId, formattedDepositOrder).subscribe({
        next: (response: any) => {
          if (response.code == 1000) {
            this.toastr.success(response.result);
            this.realoadgetAllUser();
            this.isLoadding = false;
            const closeModalButton = document.querySelector('.close') as HTMLElement;
            if (closeModalButton) {
              closeModalButton.click();
            }
            $('[data-dismiss="modal"]').click();
          } else if (response.code === 1043) {
            this.toastr.error(response.message);
            this.isLoadding = false;
       
          }
        },
        error: (error: HttpErrorResponse) => {
          this.isLoadding = false;
          this.toastr.error(error.error.message);
        }
      });
    }
  }

  sendMail(orderId: number) {
    console.log(orderId);
    this.isLoadding = true;
    this.authenListService.SendMail(orderId).subscribe({
      next: (response: any) => {

        this.toastr.success("Gửi mail cho khách hàng thành công!");
        this.isLoadding = false;
        // const closeModalButton = document.querySelector('.close') as HTMLElement;
        // if (closeModalButton) {
        //   closeModalButton.click();
        // }
        // $('[data-dismiss="modal"]').click();

      },
      error: (error: HttpErrorResponse) => {
        this.isLoadding = false;
        this.toastr.error('Gửi mail thất bại');
        this.realoadgetAllUser();
        $('[data-dismiss="modal"]').click();
      }
    });
  }

  cancelDiscountModal() {
    this.priceDiscount = 0;
    this.datepriceDiscount = '';
  }
  DiscountOrder() {
    // Reset loading state and close modal in case of failure
    const closeModal = () => {
      this.isLoadding = false;
      const closeModalButton = document.querySelector('.close') as HTMLElement;
      if (closeModalButton) {
        closeModalButton.click();
      }
      $('[data-dismiss="modal"]').click();
    };
  
    // Validate percentage fields
    const isValidPercentage = (value: number | null): boolean => {
      if (value === null || value === undefined || isNaN(value)) {
        return false;
      }
      const priceDiscount = value.toString().trim();
      const percentageRegex = /^(100(\.0{1,2})?|(\d{1,2})(\.\d{1,2})?)$/;
      return percentageRegex.test(priceDiscount) && parseFloat(priceDiscount) >= 0;
    };
  
    const percentDepositPriceTrimmed = this.percentDepositPrice !== null ? this.percentDepositPrice.toString().trim() : '';
  
    if (percentDepositPriceTrimmed === '') {
      this.toastr.error('Các trường không được để trống');
      return;
    }
  
    if (!isValidPercentage(this.priceDiscount)) {
      this.toastr.error('Phần trăm phải nằm trong khoảng 0-100% và không được là số âm');
      return;
    }
  
    // Validate date
    let datepriceDiscountString: string = '';
    if (this.datepriceDiscount) {
      datepriceDiscountString = this.datepriceDiscount.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
      const discountDate = new Date(datepriceDiscountString);
      const currentDate = new Date();
  
      if (discountDate <= currentDate) {
        this.toastr.error('Ngày hoàn thành đơn hàng phải lớn hơn ngày hiện tại');
        return;
      }
    } else {
      this.toastr.error('Ngày hoàn thành đơn hàng không được để trống');
      return;
    }
  
    this.isLoadding = true;
    console.log({
      selectedOrderId: this.selectedOrderId,
      priceDiscount: this.priceDiscount,
      datepriceDiscount: this.datepriceDiscount,
    });
  
    if (this.selectedOrderId !== null) {
      this.authenListService.DateDiscountlOrder(
        this.selectedOrderId, this.priceDiscount, datepriceDiscountString
      ).subscribe({
        next: (response: any) => {
          if (response.code == 1000) {
            this.toastr.success('Giảm giá cho đơn hàng thành công');
            this.isLoadding = false;
            this.cancelDiscountModal();
            closeModal();
          }
        },
        error: (error: HttpErrorResponse) => {
          this.isLoadding = false;
          this.toastr.error('Giảm giá thất bại');
          this.realoadgetAllUser();
          this.cancelDiscountModal();
          closeModal();
        }
      });
    } else {
      this.toastr.error('Thông tin đơn hàng không hợp lệ');
      this.isLoadding = false;
    }
  }
  
}
