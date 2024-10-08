
import { ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
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
  @ViewChild('launchModalButton') launchModalButton!: ElementRef;
  @ViewChild('launchModalButton1') launchModalButton1!: ElementRef;
  
  
  

  user: any[] = [];
  userStatus: any[] = [];
  getStatusRefund: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  position: any[] = [];
  status_order: any[] = [];
  selectedCategory: string = '';
  selectedSDate: string = '';
  selectedEDate: string = '';
  selectedReason: string = '';
  selectProduduct: number = -1;

  OrderdetailById: any = {};
  isLoadding: boolean = false;
  selectedC: number | null = null;
  selectedOrderId: number | null = null;

  selectedSpecialOrder: boolean | null = null;
  activeModal: any;
  cancelReason: string = '';
  cancelReasonPrice: string = '';
  percentDepositPrice: string = '';
  percentOrderPrice: number = 0;
  depositeAmount: number = 0;
  remain_price: string = '';
  totalAmountA: number = 0;
  totalAmount: number = 0;
  totalRefundAmount: number = 0;
  totalAmount102: number = 0;



  priceDiscount: number = 0;
  datepriceDiscount: string = '';



  constructor(private cdr: ChangeDetectorRef,private http: HttpClient, private productListService: ProductListService, private orderService: OrderService,
    private authenListService: AuthenListService, private toastr: ToastrService, private orderRequestService: OrderRequestService
  ) {
    this.updateFormattedDepositeOrder();
  }
  formatPercentDepositPrice() {
    if (this.percentDepositPrice) {
      // Remove existing commas
      const valueWithoutCommas = this.percentDepositPrice.replace(/,/g, '');

      // Format the number with commas
      this.percentDepositPrice = parseFloat(valueWithoutCommas).toLocaleString('en-US');
    }
  }

  
  formatDepositOrder() {
    if (this.depositeOrder) {
      // Remove existing commas
      const valueWithoutCommas = this.depositeOrder.replace(/,/g, '');

      // Format the number with commas
      this.depositeOrder = parseFloat(valueWithoutCommas).toLocaleString('en-US');
    }
  }
  // onInput(event: Event) {
  //   const input = event.target as HTMLInputElement;
  //   let value = input.value;

  //   // Xóa dấu phân cách hàng nghìn để tính toán số
  //   value = value.replace(/,/g, '');

  //   // Cập nhật giá trị kiểu số
  //   const numericValue = parseFloat(value);
  //   if (!isNaN(numericValue)) {
  //     this.depositeOrder = numericValue;
  //   }

  //   // Cập nhật giá trị định dạng để hiển thị
  //   this.updateFormattedDepositeOrder();
  // }

  onBlur(event: Event) {
    // Định dạng giá trị khi mất focus
    this.updateFormattedDepositeOrder();
  }

  private updateFormattedDepositeOrder() {
    // Đảm bảo giá trị là kiểu số trước khi gọi toFixed
    const numericValue = Number(this.depositeOrder);
    this.formattedDepositeOrder = numericValue.toLocaleString('en-US', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
  }

  calculateTotalAmount() {
    this.totalAmount = this.depositeAmount + this.totalAmountA;
  }
  // updateDepositeAmount() {
  //   if (this.percentDepositPrice >= 0 && this.percentDepositPrice <= 100) {
  //     this.depositeAmount = (this.OrderdetailById.deposite * this.percentDepositPrice) / 100;
  //     this.updateTotalRefundAmount();
  //   }
  // }

  // Update the total amount and calculate the total refund
  // updateTotalAmount() {
  //   if (this.percentOrderPrice >= 0 && this.percentOrderPrice <= 100) {
  //     this.totalAmountA = (this.OrderdetailById.totalAmount * this.percentOrderPrice) / 100;
  //     this.updateTotalRefundAmount();
  //   }
  // }

  // Calculate the total refund amount
  // updateTotalRefundAmount() {
  //   // Calculate deposit amount
  //   if (this.percentDepositPrice >= 0 && this.percentDepositPrice <= 100) {
  //     this.depositeAmount = (this.OrderdetailById.deposite * this.percentDepositPrice) / 100;
  //   }

  //   // Calculate total amount
  //   if (this.percentOrderPrice >= 0 && this.percentOrderPrice <= 100) {
  //     this.totalAmountA = (this.OrderdetailById.totalAmount * this.percentOrderPrice) / 100;
  //   }

  //   // Update total refund amount
  //   this.totalRefundAmount = this.depositeAmount + this.totalAmountA;
  // }



  ngOnInit(): void {
    // this.updateDepositeAmount();
    // this.updateTotalAmount();
    this.calculateTotalAmount();
    this.loadPosition();
    this.loadStatus();
    this.loadgetStatusRefund();

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
      // Open the Zalo app/website in a new tab/window with the phone number
      window.open(`https://zalo.me/${phoneNumber}`, '_blank');
    } else {
      console.error('Phone number is not available.');
    }
  }

  tempTotalAmount: number = 0;

  cancelRefundModal() {
    this.percentDepositPrice = '';
    // this.percentOrderPrice = 0;
    // // Reset form fields
    // this.OrderdetailById.deposite = 0;
    // this.depositeAmount = 0;
    // this.OrderdetailById.totalAmount = 0;
    // this.totalAmountA = 0;
    // this.totalRefundAmount = 0;
    this.cancelReasonPrice = '';

    // If you are using Angular Forms, you may need to reset the form
    // this.refundForm.reset(); // Uncomment this if you are using FormGroup


  }

  cancelChangeStatusJob() {
    this.selectedModalId = '';
    this.remain_price = '';
  }
  cancelChangeStatusJob1() {
    this.depositeOrder = '';
  }
  getModalTarget(statusId: number): string {
    console.log(statusId);
    if (statusId === 5) {
   
      return '#myModal1';
    } else if (statusId === 8) {
      return '#myModal2';
    } else {
      return '';
    }
  }
  openModalForNullTotalAmount(orderId: number, event: Event, index: number) {
    // Handle logic specific to when totalAmount is null
    const statusId = (event.target as HTMLSelectElement).value;
    this.selectedModalJob = orderId.toString();
    this.selectedModalId = statusId;
    this.indexStatus = index;
    const selectedValue = (event.target as HTMLSelectElement).value;
    if (selectedValue === '8') {
      this.launchModalButton1.nativeElement.click();
    }
  }
  openModal(orderId: number, event: Event, index: number): void {
    try {
      const statusId = (event.target as HTMLSelectElement).value;
      this.selectedModalJob = orderId.toString();
      this.selectedModalId = statusId;
      this.indexStatus = index;
  
      console.log('event:', event);
      console.log('Job ID:', this.selectedModalJob, 'Status ID:', statusId);
  
      if (statusId === '5') {
        console.log('Opening Modal 1');
        if (this.launchModalButton1) {
          this.launchModalButton1.nativeElement.click();
        } else {
          console.error('launchModalButton1 is undefined');
        }
      } else {
        console.log('Opening Modal 2');
        if (this.launchModalButton) {
          this.launchModalButton.nativeElement.click();
        } else {
          console.error('launchModalButton is undefined');
        }
      }
    } catch (error) {
      console.error('Error in openModal:', error);
    }
  }
  
  openModalForNonNullTotalAmount(orderId: number, event: Event, index: number) {
    // Handle logic specific to when totalAmount is not null
    const statusId = (event.target as HTMLSelectElement).value;
    this.selectedModalJob = orderId.toString();
    this.selectedModalId = statusId;
    this.indexStatus = index;
    const selectedValue = (event.target as HTMLSelectElement).value;
    if (selectedValue === '5') {
      this.launchModalButton.nativeElement.click();
    }
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
  loadgetStatusRefund(): void {
    this.authenListService.getAllStatusOrdeRefund().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.getStatusRefund = data.result;
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
  emailCustomer: string = '';
  getOrDetailById(us: any, order_detail_id: string): void {
  
    console.log('Order_detail_id:', order_detail_id);
    console.log("order detail: ", us);
    console.log("order detail type: ", us.specialOrder);
    this.totalAmoutOrder = us.totalAmount;
    this.selectedOrderDetail = us;

    // Create an array to store all promises
    const apiCalls = [];

    // First API call
    const orderDetailPromise = this.authenListService.getOrderDetailById(us.orderId).toPromise().then(
        (data) => {
            this.OrderdetailById = data.result;
            console.log('OrderdetailById:', data.result);
        },
        (error) => {
            console.error('Error fetching order detail by ID:', error);
        }
    );
    apiCalls.push(orderDetailPromise);

    // Second API call based on the condition
    if (us.specialOrder) {
        const specialOrderPromise = this.orderRequestService.getAllOrderDetailByOrderId(order_detail_id).toPromise().then(
            (data) => {
                this.productOfOrder = data.result;
                console.log('Product Orders:', this.productOfOrder);
                console.log('email:', this.productOfOrder[0].email);
                this.emailCustomer = this.productOfOrder[0].email;
            },
            (error) => {
                console.error('Error fetching special order details:', error);
            }
        );
        apiCalls.push(specialOrderPromise);
    } else {
        const productOrderPromise = this.orderRequestService.getAllOrderDetailOfProductByOrderId(order_detail_id).toPromise().then(
            (data) => {
                this.productOfOrder = data.result;
                console.log('Product False:', this.productOfOrder);
                console.log('email:', this.productOfOrder[0].email);
                this.emailCustomer = this.productOfOrder[0].email;
            },
            (error) => {
                console.error('Error fetching product order details:', error);
            }
        );
        apiCalls.push(productOrderPromise);
    }

    // Wait for all promises to resolve
    Promise.all(apiCalls).finally(() => {
        this.isLoadding = false;
    });
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
  formatRemainPrice() {
    if (this.remain_price) {
      // Remove any non-numeric characters (except for the decimal point)
      const valueWithoutCommas = this.remain_price.replace(/,/g, '');
  
      // Format the number with commas
      const formattedValue = parseFloat(valueWithoutCommas).toLocaleString('en-US');
  
      // Update the remain_price only if the formatted value is different
      if (this.remain_price !== formattedValue) {
        this.remain_price = formattedValue;
      }
    }
  }
  


  changeStatusFinish(orderId: string, statusId: string): void {

    // Remove commas from remain_price and convert to a number
    const numericRemainPrice = parseFloat(this.remain_price.replace(/,/g, '').trim());
  
    // Check if remain_price is empty
    if (!this.remain_price.trim()) {
      this.toastr.error('Số tiền còn lại cần thanh toán không được để trống.');
      return;
    }
  
    // Check if remain_price is a valid number
    if (isNaN(numericRemainPrice)) {
      this.toastr.error('Vui lòng chỉ được nhập số.');
      return;
    }
  
    // Convert numericRemainPrice back to a string before sending it to the backend
    const remainPriceAsString = numericRemainPrice.toString();
  
    // The remain_price is valid, you can proceed with the rest of the logic
    this.isLoadding = true;
  
    this.authenListService.changeStatusOrderFinish(orderId, statusId, remainPriceAsString).subscribe(
      response => {
        if (response.code === 1000) {
          this.realoadgetAllUser();
          this.isLoadding = false;
          console.log('Order status changed', response);
          this.toastr.success('Thanh toán tiền đơn hàng thành công');
          $('[data-dismiss="modal"]').click();
        } else if (response.code === 1043) {
          this.isLoadding = false;
          this.toastr.error(response.message);
        } else {
          this.isLoadding = false;
          console.error('Error changing order status', response);
          this.toastr.error('Số tiền còn lại cần thanh toán không đúng với giá trị, vui lòng nhập lại!');
        }
      },
      (error: HttpErrorResponse) => {
        if (error.error.code === 1043) {
          this.isLoadding = false;
          this.toastr.error(error.error.message);
        } else {
          this.isLoadding = false;
          console.error('Error changing order status', error);
          this.toastr.error('Số tiền còn lại cần thanh toán không đúng với giá trị, vui lòng nhập lại!');
        }
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
  ngAfterViewInit(): void {
    console.log('launchModalButton:', this.launchModalButton);
    console.log('launchModalButton1:', this.launchModalButton1);
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
            this.cdr.detectChanges(); // Ensure view updates before any modal triggers
          } else if (data.code === 1015) {
            this.user = [];
            this.isLoadding = false;
            // this.toastr.warning(data.message);
            this.cdr.detectChanges(); // Ensure view updates before any modal triggers
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
    const closeModal = () => {
      this.isLoadding = false;
      const closeModalButton = document.querySelector('.close') as HTMLElement;
      if (closeModalButton) {
        closeModalButton.click();
      }
      $('[data-dismiss="modal"]').click();
    };

    this.isLoadding = true;
    console.log({
      selectedOrderId: this.selectedOrderId,
      selectedSpecialOrder: this.selectedSpecialOrder,
      selectedReason: this.selectedReason,
      percentDepositPrice: this.percentDepositPrice,
      cancelReasonPrice: this.cancelReasonPrice,
    });

    // Remove commas from percentDepositPrice before sending to the backend
    const numericValue = Number(this.percentDepositPrice.replace(/,/g, ''));

    // Check if the conversion resulted in NaN or if the value is empty
    if (this.percentDepositPrice.trim() === '' ) {
      this.toastr.error('Vui lòng số tiền hoàn không được để trống.');
      this.isLoadding = false;
      return;
    }
    if ( isNaN(numericValue)) {
      this.toastr.error('Vui lòng chỉ nhập số.');
      this.isLoadding = false;
      return;
    }
    
    if (!this.selectedReason) {
      this.toastr.error('Vui lòng chọn lý do.');
      this.isLoadding = false;
      return;
    }

    if (!this.cancelReasonPrice.trim()) {
      this.toastr.error('Nội dung hoàn tiền không được để trống.');
      this.isLoadding = false;
      return;
    }

    if (this.selectedOrderId !== null && this.selectedSpecialOrder !== null) {
      this.authenListService.RefundcancelOrder(
        this.selectedOrderId,
        this.selectedSpecialOrder,
        numericValue.toString(), // Send the numeric value without commas
        this.selectedReason,
        this.cancelReasonPrice
      ).subscribe({
        next: (response: any) => {
          console.log('Raw Response:', response); // Log the raw response
          this.toastr.success("Hoàn tiền đơn hàng thành công");
          this.cancelRefundModal();
          closeModal();
          this.isLoadding = false;
        },
        error: (error: any) => {
          if (error.status === 400) {
            try {
              const errorResponse = JSON.parse(error.error);
              if (errorResponse.code === 1044) {
                this.toastr.error(errorResponse.message);
                console.error('Error:', error);
              } else {
                this.toastr.error(error.error);
              }
            } catch (e) {
              this.toastr.error(error.error);
            }
          } else {
            this.toastr.error(error.error);
          }
          this.isLoadding = false;
        },
      });
    } else {
      this.toastr.error('Thông tin đơn hàng không hợp lệ');
      this.isLoadding = false;
    }
  }
  depositeOrder: string = "";
  depositeOrderDisplay: string = "";
  formattedDepositeOrder: string = '';


  // formatInputValue(value: string) {
  //   const numericValue = parseFloat(value);
  //   if (!isNaN(numericValue)) {
  //     this.depositeOrder = numericValue;
  //     // Định dạng giá trị và cập nhật formattedDepositeOrder
  //     this.formattedDepositeOrder = this.depositeOrder.toLocaleString('en-US');
  //   } else {
  //     this.depositeOrder = 0;
  //     this.formattedDepositeOrder = '';
  //   }

  // }

  confirmPayment() {
    this.isLoadding = true;
  
    // Remove commas and convert depositeOrder to a number
    const numericDepositOrder = parseFloat(this.depositeOrder.replace(/,/g, ''));
  
    // Ensure depositeOrder is a valid number
    if (isNaN(numericDepositOrder)) {
      this.toastr.error('Vui lòng chỉ nhập số.');
      this.isLoadding = false;
      return;
    }
  
    // Format the number to two decimal places for backend
    const formattedDepositOrder = numericDepositOrder.toFixed(2);
  
    if (this.selectedOrderId !== null) {
      console.log(this.selectedOrderId, formattedDepositOrder);
      this.authenListService.Paymentmoney(this.selectedOrderId, formattedDepositOrder).subscribe({
        next: (response: any) => {
          if (response.code === 1000) {
            this.toastr.success(response.result);
            this.realoadgetAllUser();
            this.isLoadding = false;
            const closeModalButton = document.querySelector('.close') as HTMLElement;
            if (closeModalButton) {
              closeModalButton.click();
            }
            $('[data-dismiss="modal"]').click();
            this.cancelChangeStatusJob1();
          } 
        },
        error: (error: HttpErrorResponse) => {
          console.log(error);
          if (error.error.code === 1043) {
            this.toastr.error(error.error.message);
          } else {
            this.toastr.error('An error occurred.');
          }
          this.isLoadding = false;
        }
      });
    } else {
      this.isLoadding = false;
      this.toastr.error('Order ID is not selected.');
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

    // const percentDepositPriceTrimmed = this.percentDepositPrice !== null ? this.percentDepositPrice.toString().trim() : '';

 

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