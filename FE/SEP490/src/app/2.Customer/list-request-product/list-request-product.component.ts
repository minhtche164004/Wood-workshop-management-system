import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { timer } from 'rxjs';
import { AuthenListService } from 'src/app/service/authen.service';
interface ApiResponse {
  code: number;
  result: any[];
}
@Component({
  selector: 'app-list-request-product',
  templateUrl: './list-request-product.component.html',
  styleUrls: ['./list-request-product.component.scss']
})
export class ListRequestProductComponent {
  selectedOrderId: number = 0;
  loginToken: string | null = null; 
  list_request_product: any[] = [];
  currentPage: number = 1;
  isLoadding: boolean = false; 
  requestForm: FormGroup;
  selectedImages: File[] = [];
  imagesPreview: string[] = [];
  initialFormValue: any;
  selectedProductIdCurrentDelele: number = 0;
  constructor(private authenListService: AuthenListService, private toastr: ToastrService,private fb: FormBuilder,) {
    
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
    this.initializeForm();
    
    this.getHistoryOrder();
    
  }
  setOrderForPayment(orderId: number) {
    this.selectedOrderId = orderId;

  }
  getHistoryOrder(): void {
    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getListRequestProductCusomer().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.list_request_product = data.result;
            console.log('Danh sách sản phẩm đã đặt:', data);
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
  openConfirmDeleteModal(requestId: number): void {
    this.selectedProductIdCurrentDelele = requestId;
  }
  deleteRequest() {
    this.isLoadding = true;
    this.authenListService.deleteRequestProductCustomer(this.selectedProductIdCurrentDelele)
      .subscribe(
        response => {
          
          if (response.code === 1000) {
            this.isLoadding = false;
            this.toastr.success('Xóa sản phẩm yêu cầu đã đặt thành công!', 'Thành công');
            timer(200).subscribe(() => {
              window.location.reload();
            });
          }
          const cancelButton = document.querySelector('.btn.btn-secondary[data-dismiss="modal"]') as HTMLElement;
          if (cancelButton) { // Check if the button exists
            
            cancelButton.click(); // If it exists, click it to close the modal
          }

        },
        (error: HttpErrorResponse) => {
          if (error.status === 400 && error.error.code === 1030) {
            this.toastr.error(error.error.message, 'Lỗi');
          } else {
            this.toastr.error("Không thể xoá sản phẩm do sản phẩm đang được sử dụng ở các chức năng khác", 'Lỗi');
          }
          const cancelButton = document.querySelector('.btn.btn-secondary[data-dismiss="modal"]') as HTMLElement;
          if (cancelButton) { // Check if the button exists
            cancelButton.click(); // If it exists, click it to close the modal
          }
          // this.isLoading = false; // Stop the loading spinner on error
        }
      );
    console.log('requestId', this.selectedProductIdCurrentDelele);
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
    resetForm(): void {
      this.requestForm.reset(this.initialFormValue);
      this.imagesPreview = this.initialFormValue.requestImages ? this.initialFormValue.requestImages.map((image: any) => image.fullPath) : [];
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

  get isStatusDisabled(): boolean {
    return this.requestForm.get('status_id')?.value === '1';
  }

  get isDescriptionReadonly(): boolean {
    return this.isStatusDisabled; // Description is readonly if status_id is 1
  }
}  
