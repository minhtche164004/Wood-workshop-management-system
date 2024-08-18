import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { timer } from 'rxjs';
import { AuthenListService } from 'src/app/service/authen.service';
import { OrderRequestService } from 'src/app/service/order-request.service';
import { OrderService } from 'src/app/service/order.service';
interface ApiResponse {
  code: number;
  result: any[];
}
@Component({
  selector: 'app-history-order',
  templateUrl: './history-order.component.html',
  styleUrls: ['./history-order.component.scss']
})
export class HistoryOrderComponent {
  selectedImages: File[] = [];
  history_order: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  initialFormValue: any;
  userStatus: any[] = [];
  orderData: any = {};
  isLoadding: boolean = false;
  imagesPreview: string[] = [];
  selectedCategory: string = '';
  searchKey: string = '';
  selectedSDate: string = '';
  selectedEDate: string = '';
  requestForm: FormGroup;
  constructor(private orderRequestService: OrderRequestService,private toastr: ToastrService,private authenListService: AuthenListService,private orderService: OrderService,private fb: FormBuilder) { 
    this.requestForm = this.fb.group({
      request_Id: [0],
      description: [''], 
      status_id:[''],
      requestImages: ['']
    });
   }
   initializeForm(): void {
    this.requestForm = this.fb.group({
      request_Id: [null],
      description: [''],
      status_id: [null],
      requestImages: [null]
    });
  }
  ngOnInit(): void {
    this.orderData = {
      order_detail_id: '',
      product_id: '',
      product_name: '',
      request_product_id: '',
      request_product_name: '',
      price: '',
      quantity: '',

    };

    this.getHistoryOrder();
    this.getOrderStatus();

  }
  filterHistoryOrder(): void {
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
      "DateS:", startDate, "DateE:", endDate
    );
    this.authenListService.getFilterHistoryOrder(
      this.searchKey.trim(),
      this.selectedCategory,
      startDate,
      endDate


    )
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.currentPage = 1;
            this.history_order = data.result;
            console.log(this.history_order);
            this.isLoadding = false;

          } else if (data.code === 1015) {
            this.history_order = [];
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
  getHistoryOrder(): void {
    this.loginToken = localStorage.getItem('loginToken');
    this.isLoadding = true;
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getHistoryOrderCustomer().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.history_order = data.result;
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
  realoadgetHistoryOrder(): void {
    this.loginToken = localStorage.getItem('loginToken');
    this.isLoadding = true;
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getHistoryOrderCustomer().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.history_order = data.result;
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
  onResetImage() {
    this.selectedImages = [];
    this.imagesPreview = [];
  }
  onImagesSelected(event: any): void {
    this.selectedImages = Array.from(event.target.files);

    const files: File[] = Array.from(event.target.files as FileList);
    if (event.target.files && event.target.files.length) {
      // xoa list preview cu    
      this.imagesPreview = [];

      // Create and store URLs for preview
      files.forEach((file: File) => {
        const url = URL.createObjectURL(file);
        this.imagesPreview.push(url);
      });

    }
  }
  historyOrderDetail(orderId: string): void {
    this.isLoadding = true;
    this.authenListService.getOrderDetailById(orderId).subscribe(
      (data) => {
        this.orderData = data.result;
        console.log("Order data:", this.orderData.price)
        this.isLoadding = false;


      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    )
  }
  totalAmoutOrder: number = 0;
  selectedOrderDetail: any = {};
  OrderdetailById: any = {};
  productOfOrder: any = [];
  selectedOrderId: number = 0;
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
        // console.log('OrderdetailById:', data.result);
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
  resetForm(): void {
    this.requestForm.reset(this.initialFormValue);
    this.imagesPreview = this.initialFormValue.requestImages ? this.initialFormValue.requestImages.map((image: any) => image.fullPath) : [];
  }
  setOrderForPayment(orderId: number) {
    this.selectedOrderId = orderId;

  }
  getDataRequest(orderId: number) {
    console.log(orderId);
    this.requestForm.patchValue({
      orderId: orderId,
      description: null,
      status_id: null,
      requestImages: null
    });
    this.imagesPreview = [];
    
    this.authenListService.getRequestByIdCustomer(orderId)
      .subscribe(async product => {
        if (product && product.result) {

          this.requestForm.patchValue({
            description: product.result.description,
            status_id: product.result.status_id,
            requestImages: product.result.requestImages
          });
          if (product.result.requestImages) {
            this.imagesPreview = product.result.requestImages.map((image: any) => {
              return image.fullPath;
            });
          }
        }
        console.log(product);
      });
  }
  onEditSubmit(): void {
    this.isLoadding = true;
      const requestData = this.requestForm.value;
      // console.log('Form Data for Edit:', requestData.product_id);
      const updatedRequestProduct = {
        ...requestData,
        images: this.selectedImages
      };
      // console.log('Form Data for updatedProduct:', updatedRequestProduct);
      this.authenListService.editRequestProductForCustomer(updatedRequestProduct, this.selectedImages, this.selectedOrderId)
        .subscribe(
          response => {
            this.isLoadding = false;

            this.toastr.success('Cập nhật sản phẩm yêu cầu thành công!', 'Thành công');
            timer(1000).subscribe(() => {
              window.location.reload();
            });
            const closeButton = document.querySelector('.btn-mau-do[data-dismiss="modal"]') as HTMLElement;
            if (closeButton) { // Check if the button exists
              closeButton.click(); // If it exists, click it to close the modal
              console.log("close button success")
            }
          },
          error => {
            this.isLoadding = false;
            // console.error('Update error', error);
            this.toastr.error('Cập nhật sản phẩm bị lỗi!', 'Lỗi');
            const closeButton = document.querySelector('.btn-mau-do[data-dismiss="modal"]') as HTMLElement;
            if (closeButton) { // Check if the button exists
              closeButton.click(); // If it exists, click it to close the modal
              console.log("close button success")
            }
          }
        );
    
  }

  cancelReason: string = '';
  selectedSpecialOrder: boolean | null = null;
  setOrderForCancellation(orderId: number , specialOrder: boolean | null) {
    this.selectedOrderId = orderId;
    this.selectedSpecialOrder = specialOrder;
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
          this.realoadgetHistoryOrder();
          $('[data-dismiss="modal"]').click();
        }
      });
    }
  }
}
