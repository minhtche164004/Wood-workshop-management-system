import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ErrorProductService } from 'src/app/service/error-product.service';
import { ProductListService } from 'src/app/service/product/product-list.service';

@Component({
  selector: 'app-report-management',
  templateUrl: './report-management.component.html',
  styleUrls: ['./report-management.component.scss']
})
export class ReportManagementComponent implements OnInit {
  constructor(private fb: FormBuilder,
    private productListService: ProductListService,
    private errorProductService: ErrorProductService,
    private toastr: ToastrService) { 
      this.errorForm = this.fb.group({
        productCode: [''],
        employeeCode: [''],
        orderCode: [''],
        description: [''],
        solution: ['']
      });
    }
    selectedProductIdCurrentDelele: number = 0;
    selectedProductNameCurrentDelele: string | null = null;
    errorForm: FormGroup;
    description: string = '';
    solution: string = '';
  errorProducts: any[] = [];
  currentPage: number = 1;
  ngOnInit(): void {
    this.getAllProductError();
  }
  getAllProductError(): void {
    this.productListService.getAllProductError().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.errorProducts = data.result;
          console.log('Danh sách lỗi sản phẩm:', this.errorProducts);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }
  reportError() {
    const jobId = 99; // Hoặc lấy từ giá trị khác nếu cần
    const formValues = this.errorForm.value;
    console.log('Form values:', formValues);
    this.productListService.createProductError(
      jobId,
      formValues.description,
      formValues.solution
    ).subscribe(
      (data) => {
        if (data.code === 1000) {
         
          console.log('Thêm lỗi sản phẩm thành công:');
          this.toastr.success('Thêm lỗi sản phẩm thành công!', 'Thành công');
        } else {
          console.error('Thêm lỗi sản phẩm thất bại');
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Lỗi thêm sản phẩm:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }
  resetForm() {
    this.errorForm.reset();
  }
  editProduct(productId: number) {
    
  }
  openConfirmDeleteModal(product_id : number, product_name: string): void {
    console.log('productId delete: ', product_id);
    this.selectedProductIdCurrentDelele = product_id;
    this.selectedProductNameCurrentDelele = product_name;
  }
  deleteProduct() {
    console.log('productId', this.selectedProductIdCurrentDelele);
    // this.errorProductService.deleteProductError(this.selectedProductIdCurrentDelele)
    //   .subscribe(
    //     response => {
    //       console.log('Xóa thành công', response);
    //       if (response.code === 1000) {
    //         this.toastr.success('Xóa sản phẩm thành công!', 'Thành công');
    //       }
    //       const cancelButton = document.querySelector('.btn.btn-secondary[data-dismiss="modal"]') as HTMLElement;
    //       if (cancelButton) { // Check if the button exists
    //         cancelButton.click(); // If it exists, click it to close the modal
    //       }

    //     },
    //     (error: HttpErrorResponse) => {
    //       if (error.status === 400 && error.error.code === 1030) {
    //         this.toastr.error(error.error.message, 'Lỗi');
    //       } else {
    //         this.toastr.error("Không thể xoá sản phẩm do sản phẩm đang được sử dụng ở các chức năng khác", 'Lỗi');
    //       }
    //       const cancelButton = document.querySelector('.btn.btn-secondary[data-dismiss="modal"]') as HTMLElement;
    //       if (cancelButton) { // Check if the button exists
    //         cancelButton.click(); // If it exists, click it to close the modal
    //       }
    //       // this.isLoading = false; // Stop the loading spinner on error
    //     }
    //   );
    console.log('productId', this.selectedProductIdCurrentDelele);
  }

  onEditSubmit(): void {
  

    //   this.productListService.updateProduct(updatedProduct)
    //     .subscribe(
    //       response => {
    //         console.log('Update successful', response);
    //         this.toastr.success('Cập nhật sản phẩm thành công!', 'Thành công');
    //         this.reloadProducts();
    //       },
    //       error => {
    //         console.error('Update error', error);
    //         this.toastr.error('Cập nhật sản phẩm bị lỗi!', 'Lỗi');
    //       }
    //     );
     }
  

  

  showProductDetails(productId: number) {
    this.productListService.getProductById(productId)
      .subscribe(product => {
        // Update modal content with retrieved product data

      });
  }
}
