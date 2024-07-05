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
  selectedProductIdCurrentDelele: number = 0;
  constructor(private authenListService: AuthenListService, private toastr: ToastrService,private fb: FormBuilder,) {
    
    this.requestForm = this.fb.group({
      description: ['']
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
  getDataRequest(requestId: number) {
    console.log('Request ID:', requestId); // Log the requestId
    this.authenListService.getRequestByIdCustomer(requestId)
    
      .subscribe(
        product => {
          console.log('API response:', product); // Log the entire response
          if (product && product.result) {
            this.requestForm.patchValue({
              description: product.result.description,
            });
            console.log("Product details:", this.requestForm.value);
          } else {
            console.error('Product result is undefined', product);
            this.toastr.error('Failed to load product details. Please try again later.', 'Error');
          }
        },
        error => {
          console.error('Error fetching product details:', error);
          this.toastr.error('Failed to load product details. Please try again later.', 'Error');
        }
      );
  }
}  
