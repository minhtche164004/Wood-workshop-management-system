import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ProductListService } from 'src/app/service/product/product-list.service';

@Component({
  selector: 'app-report-management',
  templateUrl: './report-management.component.html',
  styleUrls: ['./report-management.component.scss']
})
export class ReportManagementComponent implements OnInit {
  constructor(private fb: FormBuilder,
    private productListService: ProductListService,
    private toastr: ToastrService) { 
      this.errorForm = this.fb.group({
        productCode: [''],
        employeeCode: [''],
        orderCode: [''],
        description: [''],
        solution: ['']
      });
    }
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
  

  deleteProduct(productId: number) {
    // Implement delete logic
  }

  showProductDetails(productId: number) {
    this.productListService.getProductById(productId)
      .subscribe(product => {
        // Update modal content with retrieved product data

      });
  }
}
