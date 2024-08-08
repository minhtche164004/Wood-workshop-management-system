import { AnimationStyleMetadata } from '@angular/animations';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ErrorProductService } from 'src/app/service/error-product.service';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { data } from 'jquery';
@Component({
  selector: 'app-report-management',
  templateUrl: './report-management.component.html',
  styleUrls: ['./report-management.component.scss']
})
export class ReportManagementComponent implements OnInit {
  constructor(private fb: FormBuilder,
    private route: ActivatedRoute,
    private productListService: ProductListService,
    private errorProductService: ErrorProductService,
    private toastr: ToastrService) {
    this.errorForm = this.fb.group({
      code: [''],
      code_order: [''],
      description: [''],
      employee_name: [''],
      id: [],
      job_id: [],
      job_name: [''],
      product_id: [],
      product_name: [''],
      request_product_id: [],
      request_product_name: [''],
      solution: [''],
      user_name_order: [''],
      isFixed: []
    });
  }
  searchKey: string = '';
  originalError: any = {};
  selectedFixed: number = -1;
  selectedError: any = {
    code: null,
    code_order: null,
    description: null,
    employee_name: null,
    id: null,
    job_id: null,
    job_name: null,
    product_id: null,
    product_name: null,
    request_product_id: null,
    request_product_name: null,
    solution: null,
    user_name_order: null,
    isFixed: null
  };
  selectedProductIdCurrentDelele: number = 0;
  selectedProductNameCurrentDelele: string | null = null;
  errorForm: FormGroup;
  description: string = '';
  solution: string = '';
  productId: number = 0;
  errorDetail: any;
  errorProducts: any[] = [];
  currentPage: number = 1;
  isLoadding: boolean = false;
  checkNotFound: boolean = false;
  
  ngOnInit(): void {
    this.getAllProductError();

  }
  searchError(){
    this.isLoadding = true;
    this.searchKey.trim();
    this.errorProductService.filterProductErrors(this.searchKey, this.selectedFixed).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.errorProducts = data.result;
          console.log('Danh sách lỗi sản phẩm ngOninit:', this.errorProducts);
          this.isLoadding = false;
        } else {
          this.toastr.warning(data.message, 'Lỗi');
          this.isLoadding = false;
          this.checkNotFound = true;
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.warning('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
        this.checkNotFound = true;
      }
    );
  }
  getAllProductError(): void {
    this.isLoadding = true;
    this.errorProductService.getAllProductError().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.errorProducts = data.result;
          console.log('Danh sách lỗi sản phẩm ngOninit:', this.errorProducts);
          this.isLoadding = false;
          this.checkNotFound = true;
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.warning('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    );
  }
  showErrorDetails(productId: any) {
    this.isLoadding = true;
    console.log('errorId: ', productId);
    this.errorProductService.getRrrorDetailById(productId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.errorDetail = data.result;
          console.log('Chi tiết lỗi sản phẩm:', data.result);
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.warning('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    );

  }

  resetForm() {
    this.isLoadding = true;
    this.errorForm.reset();
  }

  editProduct(errorid: number) {
    this.isLoadding = true;
    this.errorProductService.getRrrorDetailById(errorid)
      .subscribe((response: any) => {

        if (response.code === 1000) {
          const errorEdit = response.result;

          console.log('Get Error Edit:', errorEdit);
          this.selectedError = {
            code: errorEdit.code,
            code_order: errorEdit.code_order,
            description: errorEdit.des,
            employee_name: errorEdit.employee_name,
            id: errorEdit.id,
            job_id: errorEdit.job_id,
            job_name: errorEdit.job_name,
            product_id: errorEdit.product_id,
            product_name: errorEdit.product_name,
            request_product_id: errorEdit.request_product_id,
            request_product_name: errorEdit.request_product_name,
            solution: errorEdit.solution,
            user_name_order: errorEdit.user_name_order,
            isFixed: errorEdit.fix

          };

          //   this.originalError = { ...this.selectedError };
          console.log('Form Values after patchValue:', this.selectedError);
          this.errorForm.patchValue({
            code: this.selectedError.code,
            code_order: this.selectedError.code_order,
            description: this.selectedError.description,
            employee_name: this.selectedError.employee_name,
            id: this.selectedError.id,
            job_id: this.selectedError.job_id,
            job_name: this.selectedError.job_name,
            product_id: this.selectedError.product_id,
            product_name: this.selectedError.product_name,
            request_product_id: this.selectedError.request_product_id,
            request_product_name: this.selectedError.request_product_name,
            solution: this.selectedError.solution,
            user_name_order: this.selectedError.user_name_order,
            isFixed: this.selectedError.isFixed
          });
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', response); this.isLoadding = false;
          // this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }

      })
    //  console.log('Form Values after patchValue:', this.errorForm.value);
  }

  selectedReportCode: string = '';
  openConfirmDeleteModal(product_id: number, product_name: string, report_code: string): void {
    this.selectedReportCode = report_code;
    console.log("code: ", this.selectedReportCode)
    console.log('productId delete: ', product_id);
    this.selectedProductIdCurrentDelele = product_id;
    this.selectedProductNameCurrentDelele = product_name;

  }
  deleteProduct() {
    this.isLoadding = true;
    console.log('productId', this.selectedProductIdCurrentDelele);
    this.errorProductService.deleteProductError(this.selectedProductIdCurrentDelele)
      .subscribe(
        response => {
          console.log('Xóa thành công', response);
          if (response.code === 1000) {
            this.toastr.success('Xóa báo cáo lỗi sản phẩm thành công!', 'Thành công');
            this.ngOnInit();
            this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          }
          const cancelButton = document.querySelector('.btn.btn-secondary[data-dismiss="modal"]') as HTMLElement;
          if (cancelButton) { // Check if the button exists
            cancelButton.click(); // If it exists, click it to close the modal
          }
          this.isLoadding = false;
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
        },
        (error) => {


        }
      );
    console.log('productId', this.selectedProductIdCurrentDelele);
  }

  onEditSubmit(): void {

  }


  saveChanges(): void {
     this.isLoadding = true;
    // console.log('Form Values:', this.errorForm.value);
    const errorFormData = this.errorForm.value;
    console.log('error edit form saveChanges:', errorFormData);
    if (!errorFormData.description || !errorFormData.solution) {
      this.isLoadding = false;
      this.toastr.info('Description và Solution không được bỏ trống!', 'Thông báo');
      return;
  }

  // Kiểm tra nếu description hoặc solution ngắn hơn 3 ký tự
  if (errorFormData.description.length < 3) {
      this.isLoadding = false;
      this.toastr.info('Description phải có ít nhất 3 ký tự!', 'Thông báo');
      return;
  }

  if (errorFormData.solution.length < 3) {
      this.isLoadding = false;
      this.toastr.info('Solution phải có ít nhất 3 ký tự!', 'Thông báo');
      return;
  }

    this.errorProductService.editProductError(errorFormData.id, errorFormData).subscribe(
      (response) => {
        if (response.code === 1000) {
          this.toastr.success('Sửa lỗi sản phẩm thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          this.ngOnInit();
        } else {
          console.error('Failed to edit product:', response);
          this.toastr.warning('Không thể sửa sản phẩm!', 'Thông báo'); this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        }
      },
      (error) => {
        console.error('Error editing product:', error);
        this.toastr.warning('Có lỗi xảy ra!', 'Thông báo'); this.isLoadding = false;
      }
    );

  }

  showProductDetails(error: any) {
    this.isLoadding = true;
    this.errorForm.patchValue({
      code: error.code,
      code_order: error.code_order,
      description: error.description,
      employee_name: error.employee_name,
      id: error.id,
      job_id: error.job_id,
      job_name: error.job_name,
      product_id: error.product_id,
      product_name: error.product_name,
      request_product_id: error.request_product_id,
      request_product_name: error.request_product_name,
      solution: error.solution,
      user_name_order: error.user_name_order,
      isFixed: error.isFixed
    });
    this.isLoadding = false;
  }

}
