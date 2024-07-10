import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { JobService } from 'src/app/service/job.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { CalendarModule } from 'primeng/calendar';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ProductService } from 'src/app/service/product.service';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import 'jquery';
@Component({
  selector: 'app-job-management',
  templateUrl: './job-management.component.html',
  styleUrls: ['./job-management.component.scss']
})
export class JobManagementComponent implements OnInit {
  products: any[] = [];
  errorForm: FormGroup;
  keyword = 'productName';
  createJobs: FormGroup;
  productRQs: any[] = [];
  currentPage: number = 1;
  searchKey: string = '';
  selectedEmployee: any = {};
  selectedCategory: number = 0;
  selectedStatus: number = 0;
  selectedProduct: any = {}; // Biến để lưu trữ sản phẩm được chọn
  positionEmployees: any[] = [];
  type: any = {};
  statusType: any[] = [];
  statusOptions: any[] = [];
  productForm: FormGroup;
  costJob: any; // Cost of the job
  jobDescription: string = ''; // Job description
  pForJob: any; // List of products for the job
  materialProduct: any[] = [];
  confirmStatus: string = '';
  selectedProductNameCurrentDelele: number = 0;
  showModal = false;
  position: number = 0;
  quantityJobs: FormGroup;
  subMaterialProduct: any[] = [];
  constructor(private fb: FormBuilder, private productList: ProductService, private productListService: ProductListService, private jobService: JobService, private toastr: ToastrService, private sanitizer: DomSanitizer) {
    this.createJobs = this.fb.group({
      job_name: [''],
      quantity_product: [''],
      cost: [],
      description: [''],
      finish: [''],
      start: [''],
    });
    this.errorForm = this.fb.group({
      description: [''],
      solution: ['']
    });
    this.productForm = this.fb.group({
      quantity: ['', [Validators.required, Validators.min(1)]]
    });
    this.quantityJobs = this.fb.group({
      quantity: ['']
    });
  }

  ngOnInit(): void {
    this.loadProductRQForJob();
    this.loadStatusByType();
    this.productListService.getProducts().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          // console.log('Danh sách sản phẩm:', this.products);
        } else {
          console.error('Failed to fetch products:', data);
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
      }
    );
  }

  editSubMaterial(product: any) {
    console.log('Edit sub-material:', product);
  }
  acceptJob(event: Event, jobId: number): void {
    const statusId = (event.target as HTMLSelectElement).value;
    console.log('Job ID:', jobId, 'Status ID:', statusId);

    // if (statusId === 4) {
    //   statusId = 6;
    // } else if (statusId === 7) {
    //   statusId = 9;
    // } else if (statusId === 10) {
    //   statusId = 12;
    // }
    this.jobService.acceptJob(jobId, statusId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.toastr.success('Cập nhật trạng thái làm việc thành công!', 'Thành công');
          console.log('Đổi trạng thái job thành công');
          this.productListService.getProducts().subscribe(
            (data) => {
              if (data.code === 1000) {
                this.products = data.result;
                // console.log('Danh sách sản phẩm:', this.products);
              } else {
                this.toastr.error('Cập nhật trạng thái làm việc thất bại!', 'Lỗi');
                console.error('Failed to fetch products:', data);
              }
            },
            (error) => {
              console.error('Error fetching products:', error);
            }
          );
        } else {
          this.toastr.error('Cập nhật trạng thái làm việc thất bại!', 'Lỗi');
          console.error('Failed to fetch products:', data);

        }
      },
      (error) => {
        console.error('Error accepting job:', error);
      }
    );
  }

  // exportProductTotalJob(id: number, mate_id: number, emp_id: number, productForm: any) {
  //   console.log('export id: ', id);
  //   console.log('export mate_id: ', mate_id);
  //   console.log('export emp_id: ', emp_id);

  // }

  openConfirmChangeStatusJob(product: any): void {

    this.selectedProduct = { ...product };
    console.log('selectedProduct.job_id:', this.selectedProduct.job_id);
    console.log('this.selectedProduct.type:', this.selectedProduct.statusJob.status_id);
    this.confirmStatus = this.selectedProduct.statusJob.status_name;
    // this.acceptJob(this.selectedProduct.job_id, this.selectedProduct.statusJob.status_id);
  }

  getAllProductSubMaterialByProductId(productId: number, materialId: number) {
    this.productList.getProductSubMaterialByProductId(productId, materialId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.materialProduct = data.result;
          // console.log('Material for product:', this.materialProduct);

        } else {
          console.error('Failed to fetch products:', data);

        }
      },
      (error) => {
        console.error('Error fetching Sub Material:', error);
      }
    );
  }



  createNewJob() {
    console.log('Selected Employee:', this.selectedEmployee);
    const user_id = this.selectedEmployee;

    const p_id = this.selectedProduct.product_id; // Thay đổi giá trị tùy theo sản phẩm
    let status_id = this.selectedProduct.statusJob?.status_id; // Thay đổi giá trị tùy theo trạng thái
    const job_id = this.selectedProduct.job_id; // Thay đổi giá trị tùy theo công việc
    const type_id = this.selectedCategory; // cho sp có sẵn     0 - k có sẵn
    let position_id = this.selectedProduct.position_id + 1;

    const jobData = this.createJobs.value;
    if (status_id === 6) {
      status_id = 7;
    } else if (status_id === 3) {
      status_id = 4;
    } else if (status_id === 9) {
      status_id = 10;
    }

    // console.log('emp_id:', user_id);
    // console.log('mate_id:', position_id);
    // console.log('id:', p_id);
    const quantity = this.selectedProduct.quantity;
    const createJobs = {
      quantity_product: quantity
    };
    console.log('quantity productForm:', createJobs);
    this.jobService.createExportMaterialProductTotalJob(p_id, position_id, user_id, createJobs).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.toastr.success('Xuất nguyên liệu thành công', 'Thành công');
          console.log('Xuất nguyên liệu thành công');
          this.jobService.addJob(user_id, p_id, status_id, job_id, type_id, jobData).subscribe(
            (data) => {
              if (data.code === 1000) {
                this.pForJob = data.result;
                // console.log('Add product for job:', this.pForJob);
                this.toastr.success('Thêm sản phẩm sản xuất thành công!', 'Thành công');
                this.ngOnInit();
              } else {
                console.error('Failed to fetch products:', data);
                this.toastr.error('Thêm sản phẩm sản xuất thất bại!', 'Lỗi');
              }
            },
            (error) => {
              console.error('Error fetching products:', error);
              this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
            }
          );
        } else if (data.code === 1015) {
          console.error('Failed to fetch products:', data);
          this.toastr.warning('Số lượng nguyên vật liệu trong kho không đủ', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error:', error);
        if (error && error.error && error.error.errors) {
          Object.entries(error.error.errors).forEach(([key, value]) => {
            this.toastr.warning(`${key}: ${value}`, 'Lỗi');
          });
        } else if (error && error.message) {
          this.toastr.error(`Có lỗi xảy ra: ${error.message}`, 'Lỗi');
        } else {
          this.toastr.error(`Có lỗi xảy ra, vui lòng thử lại.`, 'Lỗi');
        }
      }
    );
  }

  selectEvent(item: any) {
    this.selectedProduct = item;
  }

  onSubmit() {
    if (this.productForm.invalid) {
      return;
    }

    const quantity = this.productForm.get('quantity')?.value;
    if (this.selectedProduct === null) {
      this.toastr.error('Please select a product');
      return;
    }
    // console.log('Selected product for job:', this.selectedProduct.productId);


    this.jobService.addProductForJob(this.selectedProduct.productId, quantity).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.pForJob = data.result;
          // console.log('Add product for job:', this.pForJob);
          this.toastr.success('Thêm sản phẩm sản xuất thành công!', 'Thành công');
          this.ngOnInit();
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Thêm sản phẩm sản xuất thất bại!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }

  onChangeSelectedEmployee(event: any) {
    this.selectedEmployee = event.target.value;
    // console.log('Selected employee ID:', event.target.value);
  }

  onChangeSearch(search: string) {
    this.searchKey = search;
  }

  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
  }

  selectProduct(product: any): void {
    this.selectedProduct = product; // Điều chỉnh theo cấu trúc đối tượng sản phẩm của bạn
    console.log('Tên sản phẩm được chọn:', product);
    // console.log('Id sản phẩm được chọn:', product.productId);
  }


  onFocused(e: any) {
    // Xử lý sự kiện khi focus vào autocomplete
  }
  changeStatusJob() {
    // console.log('selectedProduct.job_id:', this.selectedProduct.job_id);
    // console.log('this.selectedProduct.type:', this.selectedProduct.statusJob.status_id);
    this.jobService.acceptJob(this.selectedProduct.job_id, this.selectedProduct.statusJob.status_id).subscribe(
      (data) => {
        if (data.code === 1000) {
          // console.log('Đổi trạng thái job thành công');
          this.toastr.success('Đổi trạng thái job thành công!', 'Thành công');
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error accepting job:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }
  loadStatusByType() {
    this.jobService.getStatusByType().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.statusType = data.result;
          console.log('Status Job Type:', data.result);
        }
        console.log('Data:', data);
      },
      (error) => {
        console.error('Error fetching data:', error);
      }
    );
  }

  isButtonDisabled(): boolean {
    const selectedStatusId = this.selectedStatus;
    if (selectedStatusId === 10 || selectedStatusId === 7 || selectedStatusId === 4) {
      return true;
    }
    return false;
  }

  getStatusOptions(product: any) {
    console.log('Product:', product);
    this.jobService.getStatusJobByType(product.statusJob.type).subscribe(
      (data) => {
        // console.log('Status Job:', data);
        this.statusOptions = data;
      },
      (error) => {
        console.error('Error fetching data:', error);
      }
    );
  }




  loadProductRQForJob() {
    this.jobService.getListProductRQ().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.productRQs = data.result;
          console.log('Sp đặc biệt cho job:', this.productRQs);
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

  loadProduct() {
    this.jobService.getListProduct().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.productRQs = data.result;
          // console.log('Sp cho job:', this.productRQs);
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

  manageJob(product: any): void {
    this.selectedProduct = { ...product };
    console.log("Sản phẩm được chọn để giao việc:", this.selectedProduct);
    // console.log("Trạng thái của sản phẩm:", this.selectedProduct.statusJob?.status_id);

    if (this.selectedProduct.code !== null) {
      this.selectedProduct.type_id = 0;
    } else {
      this.selectedProduct.type_id = 1;
    }

    //console.log("employee:", this.selectedProduct.user_name);

    if (this.selectedProduct) {
      this.loadPosition3(product);
    }

    this.createJobs.patchValue({
      quantity_product: this.selectedProduct.quantity
    });

    // Call getSubMTRProduct with appropriate IDs
    let productId = this.selectedProduct.product_id;
    let mateId = this.selectedProduct.position_id;
    mateId += 1; // Adjust this as necessary
    // console.log("Product ID:", productId);
    // console.log("Material ID:", mateId);
    this.getProductSubMaterial(productId, mateId);

  }

  errorJob(product: any): void {
    this.selectedProduct = { ...product };
    //console.log("Sản phẩm được chọn để báo cáo lỗi:", this.selectedProduct);

  }
  createErrorJob() {
    const jobId = this.selectedProduct.job_id; // Hoặc lấy từ giá trị khác nếu cần
    console.log('Job ID:', jobId);
    const formValues = this.errorForm.value;

    console.log('Form values:', formValues);
    this.productListService.createProductError(
      jobId,
      formValues.description,
      formValues.solution
    )
      .subscribe(
        (data) => {
          if (data.code === 1000) {

            console.log('Thêm lỗi sản phẩm thành công:');
            this.toastr.success('Thêm lỗi sản phẩm thành công!', 'Thành công');
            $('[data-dismiss="modal"]').click();
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
  getProductSubMaterial(productId: number, mateId: number) {
    this.jobService.getSubMTRProduct(productId, mateId).subscribe(

      data => {
        // Handle the data as needed
        if (data.code === 1000) {
          this.subMaterialProduct = data.result;
          console.log("Sub-material product data:", this.subMaterialProduct);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách vật liệu sản phẩm!', 'Lỗi');
        }
      },
      error => {
        console.error("Error fetching sub-material data:", error);
      }
    );
  }
  onSearch(selectedCategory: number, searchKey: string): void {
    // console.log('Thực hiện tìm kiếm:', searchKey);
    // console.log('Category:', selectedCategory);

    if (selectedCategory === 0) {
      this.loadProductRQForJob();
    } else if (selectedCategory === 1) {
      this.loadProduct();
    }
  }

  loadPosition3(product: any) {
    this.selectedProduct = { ...product };
    this.type = this.selectedProduct.statusJob.type;
    let positon = this.selectedProduct.position_id;
    console.log('posion truoc khi cong:', positon);
    const statusId = this.selectedProduct.statusJob?.status_id;
    console.log('Status ID:', this.selectedProduct.statusJob?.status_id);

    positon += 1;
    this.jobService.GetPosition3(positon).subscribe(
      (data) => {
        this.positionEmployees = data.result;
        console.log('Position 3 data:', this.positionEmployees);

      },
      (error) => {
        console.error('Error fetching Position 3 data:', error);
      }
    );
  }


}