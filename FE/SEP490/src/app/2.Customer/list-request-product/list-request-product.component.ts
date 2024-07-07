import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
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
  
  loginToken: string | null = null; 
  list_request_product: any[] = [];
  currentPage: number = 1;
  requestForm: FormGroup;
  selectedImages: File[] = [];
  imagesPreview: string[] = [];
  selectedProductIdCurrentDelele: number = 0;
  constructor(private authenListService: AuthenListService, private toastr: ToastrService,private fb: FormBuilder,) {
    
    this.requestForm = this.fb.group({
      request_Id: [0],
      description: [''],
      imageList: ['']
    });
   }
  ngOnInit(): void {
    
    this.getHistoryOrder();
    
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
    this.authenListService.deleteRequestProductCustomer(this.selectedProductIdCurrentDelele)
      .subscribe(
        response => {
          console.log('Xóa thành công', response);
          if (response.code === 1000) {
            this.toastr.success('Xóa sản phẩm yêu cầu đã đặt thành công!', 'Thành công');
            window.location.reload();
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
  getDataRequest(requestId: number) {
    this.requestForm.patchValue({
      request_Id: requestId,
      description: null,
      imagesList: null
    });
    this.imagesPreview = [];
    
    this.authenListService.getRequestByIdCustomer(requestId)
      .subscribe(async product => {
        if (product && product.result) {
          this.requestForm.patchValue({
            description: product.result.description,
            imagesList: product.result.imagesList
          });
          if (product.result.imagesList) {
            this.imagesPreview = product.result.imagesList.map((image: any) => {
              return image.fullPath;
            });
          }
        }
      });
  }
  

  onEditSubmit(): void {

      const requestData = this.requestForm.value;
      // console.log('Form Data for Edit:', requestData.product_id);
      const updatedRequestProduct = {
        ...requestData,
        images: this.selectedImages
      };
      console.log('Form Data for updatedProduct:', updatedRequestProduct);
      this.authenListService.editRequestProductForCustomer(updatedRequestProduct, this.selectedImages, requestData.request_Id)
        .subscribe(
          response => {
            this.getHistoryOrder();
            console.log('Update successful', response);
            this.toastr.success('Cập nhật sản phẩm yêu cầu thành công!', 'Thành công');
            const closeButton = document.querySelector('.btn-mau-do[data-dismiss="modal"]') as HTMLElement;
            if (closeButton) { // Check if the button exists
              closeButton.click(); // If it exists, click it to close the modal
              console.log("close button success")
            }
          },
          error => {
            console.error('Update error', error);
            this.toastr.error('Cập nhật sản phẩm bị lỗi!', 'Lỗi');
            const closeButton = document.querySelector('.btn-mau-do[data-dismiss="modal"]') as HTMLElement;
            if (closeButton) { // Check if the button exists
              closeButton.click(); // If it exists, click it to close the modal
              console.log("close button success")
            }
          }
        );
    
  }

  
}  
