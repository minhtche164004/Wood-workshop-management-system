import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
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

declare var $: any;

@Component({
  selector: 'app-job-management',
  templateUrl: './job-management.component.html',
  styleUrls: ['./job-management.component.scss']
})

export class JobManagementComponent implements OnInit {

  @ViewChild('launchModalButton')
  launchModalButton!: ElementRef;
  products: any[] = [];
  errorForm: FormGroup;
  editJob: FormGroup;
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
  detailJob: FormGroup;
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
  isProduct: boolean = true; // check product or product request
  isLoadding: boolean = false;
  jobList: any[] = [];

  constructor(private fb: FormBuilder, private productList: ProductService, private productListService: ProductListService, private jobService: JobService, private toastr: ToastrService, private sanitizer: DomSanitizer) {
    this.createJobs = this.fb.group({
      job_name: [''],
      quantity_product: [''],
      cost: [],
      description: [''],
      finish: [''],
      start: [''],
    });
    this.editJob = this.fb.group({
      job_name: [''],
      quantity_product: [''],
      cost: [],
      description: [''],
      finish: [''],
      start: [''],
    });
    this.detailJob = this.fb.group({
      code: [''],
      cost: [''],
      description: [''],
      jobId: [''],
      job_log: [false],
      job_name: [''],
      quantityProduct: [''],
      timeFinish: [''],
      timeStart: ['']
    });
    this.errorForm = this.fb.group({
      description: [''],
      solution: [''],
      quantity: [''],
    });
    this.productForm = this.fb.group({
      quantity: ['', [Validators.required, Validators.min(1)]]
    });
    this.quantityJobs = this.fb.group({
      quantity: ['']
    });
  }
  loadProductNgOn(): void {
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
  ngOnInit(): void {
    this.loadProductRQForJob();
    this.loadStatusByType();
    this.loadProductNgOn();
  }
  selectedModalJob: string = '';
  selectedModalId: string = '';

  indexStatus: number = 0;

  openModal(event: Event, jobId: number, index: number): void {
    this.isLoadding = true;
    console.log('event:', event);
    const statusId = (event.target as HTMLSelectElement).value;
    this.selectedModalJob = jobId.toString();
    console.log('Job ID:', this.selectedModalJob, 'Status ID:', statusId);
    this.selectedModalId = statusId;
    // this.createJobs.reset();  
    // S? d?ng tham chi?u này d? kích ho?t click
    this.launchModalButton.nativeElement.click();
    this.indexStatus = index;
    // console.log("indexStatus:", this.indexStatus);
  }

  closeModal() {
    var element = document.getElementById("mySelect"+this.indexStatus);
    if (element instanceof HTMLSelectElement) {
      element.selectedIndex = 0;
      // console.log('element.value', element.options[element.selectedIndex].value);
      // console.log('element.selectedIndex', element.selectedIndex);
    }
    // console.log('Close modal');

  }

  cancelChangeStatusJob() {
    this.selectedModalId = '';
  }

  acceptJob(jobId: string, statusId: string): void {
    this.isLoadding = true

    console.log('Job ID:', jobId, 'Status ID:', statusId);
    this.createJobs.reset();
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
          console.log('Đổi trạng thái job thành công'); this.isLoadding = false;
          this.productListService.getProducts().subscribe(
            (data) => {
              if (data.code === 1000) {
                this.products = data.result; this.isLoadding = false;
                // console.log('Danh sách sản phẩm:', this.products);
                $('[data-dismiss="modal"]').click();
              }
            },
            (error) => {
              console.error('Error fetching products:', error);
              this.isLoadding = false;
              $('[data-dismiss="modal"]').click();
            }
          );
        } else {
          this.toastr.error('Cập nhật trạng thái làm việc thất bại!', 'Lỗi');
          console.error('Failed to fetch products:', data); this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        }
      },
      (error) => {
        console.error('Error accepting job:', error); this.isLoadding = false;
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
    this.isLoadding = true;
    this.productList.getProductSubMaterialByProductId(productId, materialId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.materialProduct = data.result; this.isLoadding = false;
          // console.log('Material for product:', this.materialProduct);

        } else {
          console.error('Failed to fetch products:', data); this.isLoadding = false;

        }
      },
      (error) => {
        console.error('Error fetching Sub Material:', error); this.isLoadding = false;
      }
    );
  }



  createNewJob() {
    this.isLoadding = true;
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
    //   console.log('quantity productForm:', createJobs);
    console.log('code :', this.selectedProduct.code);
    if (this.selectedProduct.code == null) {
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
                  $('[data-dismiss="modal"]').click();
                  this.isLoadding = false;
                  this.loadProduct();
                }
              },
              (error) => {
                console.error('Error fetching products:', error);
                this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); this.isLoadding = false;
              }
            );
          } else if (data.code === 1015) {
            console.error('Failed to fetch products:', data);
            this.toastr.warning('Số lượng nguyên vật liệu trong kho không đủ', 'Lỗi');
            $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          }
        },
        (error) => {
          console.error('Error:', error);
          if (error && error.error && error.error.errors) {
            Object.entries(error.error.errors).forEach(([key, value]) => {
              this.toastr.warning(`${key}: ${value}`, 'Lỗi');
              $('[data-dismiss="modal"]').click(); this.isLoadding = false;
            });

          } else if (error && error.message) {
            this.toastr.error(`Có lỗi xảy ra: ${error.message}`, 'Lỗi'); this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          } else {
            this.toastr.error(`Có lỗi xảy ra, vui lòng thử lại.`, 'Lỗi'); this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          }
        }
      );
    } else if (this.selectedProduct.code != null) {
      console.log("exort submaterial product request");
      this.jobService.createExportMaterialRequestTotalJob(p_id, position_id, user_id, createJobs).subscribe(
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
                  $('[data-dismiss="modal"]').click(); this.isLoadding = false;
                  this.loadProductRQForJob();
                } else {
                  console.error('Failed to fetch products:', data);
                  this.toastr.error('Thêm sản phẩm sản xuất thất bại!', 'Lỗi'); this.isLoadding = false;
                }
              },
              (error) => {
                console.error('Error fetching products:', error);
                this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); this.isLoadding = false;
              }
            );
          } else if (data.code === 1015) {
            console.error('Failed to fetch products:', data);
            this.toastr.warning('Số lượng nguyên vật liệu trong kho không đủ', 'Lỗi');
            $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          }
        },
        (error) => {
          console.error('Error:', error);
          if (error && error.error && error.error.errors) {
            Object.entries(error.error.errors).forEach(([key, value]) => {
              this.toastr.warning(`${key}: ${value}`, 'Lỗi');
              $('[data-dismiss="modal"]').click(); this.isLoadding = false;
            });

          } else if (error && error.message) {
            this.toastr.error(`Có lỗi xảy ra: ${error.message}`, 'Lỗi'); this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          } else {
            this.toastr.error(`Có lỗi xảy ra, vui lòng thử lại.`, 'Lỗi'); this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          }
        }
      );
    }

  }

  selectEvent(item: any) {
    this.selectedProduct = item;
  }

  onSubmit() {
    if (this.productForm.invalid) {
      return;
    }
    this.createJobs.reset();
    const quantity = this.productForm.get('quantity')?.value;
    if (this.selectedProduct === null) {
      this.toastr.error('Please select a product');
      return;
    }
    // console.log('Selected product for job:', this.selectedProduct.productId);


    this.jobService.addProductForJob(this.selectedProduct.productId, quantity).subscribe(

      (data) => {
        this.isLoadding = true;
        if (data.code === 1000) {
          this.pForJob = data.result;
          // console.log('Add product for job:', this.pForJob);
          this.toastr.success('Thêm sản phẩm sản xuất thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          this.ngOnInit();
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Thêm sản phẩm sản xuất thất bại!', 'Lỗi');
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        $('[data-dismiss="modal"]').click(); this.isLoadding = false;
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
    this.isLoadding = true;
    // console.log('selectedProduct.job_id:', this.selectedProduct.job_id);
    // console.log('this.selectedProduct.type:', this.selectedProduct.statusJob.status_id);
    this.jobService.acceptJob(this.selectedProduct.job_id, this.selectedProduct.statusJob.status_id).subscribe(
      (data) => {
        if (data.code === 1000) {
          // console.log('Đổi trạng thái job thành công');
          this.toastr.success('Đổi trạng thái job thành công!', 'Thành công'); this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
          this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        }
      },
      (error) => {
        console.error('Error accepting job:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); this.isLoadding = false;
        $('[data-dismiss="modal"]').click();
      }
    );
  }
  loadStatusByType() {
    this.jobService.getStatusByType().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.statusType = data.result;
          // console.log('Status Job Type:', data.result); 

        }
        // console.log('Data:', data);
      },
      (error) => {
        //console.error('Error fetching data:', error); 

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
    this.isLoadding = true;
    this.jobService.getListProductRQ().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.productRQs = data.result;
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }

        this.isLoadding = false;
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    );
  }

  loadProduct() {
    this.jobService.getListProduct().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.productRQs = data.result;
          this.selectedCategory = 1;
          // console.log('Sp cho job:', this.productRQs);
        } else {
          //   console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        // console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }

  manageJob(product: any): void {
    this.isLoadding = true;
    this.selectedProduct = { ...product };
    //  console.log("Sản phẩm được chọn để giao việc:", this.selectedProduct);
    //   console.log("thiis.type_id: ", this.selectedProduct.code);
    // console.log("Trạng thái của sản phẩm:", this.selectedProduct.statusJob?.status_id);
    this.createJobs.reset();


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
    const typeId = this.selectedProduct.type_id;

    mateId += 1; // Adjust this as necessary
    console.log("Product ID:", productId);
    console.log("Material ID:", mateId);
    if (this.selectedProduct.code != null) {
      this.getSubMTRProductRQ(productId, mateId);
    } else if (this.selectedProduct.code == null) {
      this.getProductSubMaterial(productId, mateId);
    }

    $('[data-dismiss="modal"]').click(); this.isLoadding = false;
  }
  getSubMTRProductRQ(id: number, mate_id: number) {
    this.jobService.getSubMTRProductRQ(id, mate_id).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.subMaterialProduct = data.result;
          console.log('Sub-material product data:', this.subMaterialProduct);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách vật liệu sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching sub-material data:', error);
      }
    );
  }
  // saveChanges() {
  //   this.isLoadding = true;

  //   this.isLoadding = false;
  // }
  showJobDetail(job: any) {
    let mate_id = job.position_id;
    console.log('Mate ID:', mate_id);
    console.log('Product ID:', job.product_id);
     this.isLoadding = true;  
    this.jobService.getSubMTRProduct(job.product_id, mate_id).subscribe(  //thay bang api goi job product submaterial
      (data) => {
        if (data.code === 1000) {
          this.subMaterialProduct = data.result;

          console.log('Sub-material product data:', this.subMaterialProduct);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách vật liệu sản phẩm!', 'Lỗi');
        }
        this.isLoadding = false;  
      },
      (error) => {
        console.error('Error fetching sub-material data:', error);
        this.isLoadding = false;
      }
    )
    this.jobService.getJobDetailById(job.job_id).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.selectedJob = data.result;
          console.log('Form detailJob value:', this.selectedJob);

          this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    );

  }
  selectedJob: any = {};

  editJobDetail(job_id: number): void {
    console.log('Job ID:', job_id);
    this.isLoadding = true;

    // Fetch job detail by ID
    this.jobService.getJobDetailById(job_id).subscribe(
      (data) => {
        console.log('Job detail:', data.result);
        if (data.code === 1000) {
          // Patch values to editJob form
          this.editJob.patchValue({
            // code: data.result.code,
            // cost: data.result.cost,
            description: data.result.description,
          //  jobId: data.result.jobId,
         //   job_log: data.result.job_log,
            
            job_name: data.result.job_name,
            quantity_product: data.result.quantityProduct,
            finish: data.result.timeFinish,
            start: data.result.timeStart,
            cost: data.result.cost
          });
          console.log('Form editJob value:', this.editJob.value);
          this.isLoadding = false;
        } else {
       //   console.error('Failed to fetch job detail:', data);
       //   this.toastr.error('Không thể lấy thông tin công việc!', 'Lỗi');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error fetching job detail:', error);
        this.toastr.error('Có lỗi xảy ra khi lấy thông tin công việc!', 'Lỗi');
        this.isLoadding = false;
      }
    );
  }

  errorJob(product: any): void {
    this.selectedProduct = { ...product };
    //console.log("Sản phẩm được chọn để báo cáo lỗi:", this.selectedProduct);

  }
  
saveChanges(): void {
  // this.isLoadding = true;
 // console.log('Form Values:', this.errorForm.value);
  const errorFormData = this.editJob.value;
  console.log('error edit form saveChanges:', errorFormData);
  this.jobService.editJob(errorFormData.id,errorFormData).subscribe(
    (response) => {
      if (response.code === 1000) {
        this.toastr.success('Sửa lỗi sản phẩm thành công!', 'Thành công');
        $('[data-dismiss="modal"]').click();this.isLoadding = false;
         this.ngOnInit();
      } else {
        console.error('Failed to edit product:', response);
        this.toastr.error('Không thể sửa sản phẩm!', 'Lỗi');this.isLoadding = false;
        $('[data-dismiss="modal"]').click();
      }
    },
    (error) => {
      console.error('Error editing product:', error);
      this.toastr.error('Có lỗi xảy ra!', 'Lỗi');this.isLoadding = false;
    }
  );
  
}
  createErrorJob() {
    this.isLoadding = true;

    const jobId = this.selectedProduct.job_id; // Hoặc lấy từ giá trị khác nếu cần
    console.log('Job ID:', jobId);
    const formValues = this.errorForm.value;

    console.log('Form values:', formValues);
    this.productListService.createProductError(
      jobId,
      formValues.description,
      formValues.solution,
      formValues.quantity
    )
      .subscribe(
        (data) => {
          if (data.code === 1000) {

            console.log('Thêm lỗi sản phẩm thành công:');
            this.toastr.success('Thêm lỗi sản phẩm thành công!', 'Thành công');
            $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          }
        },
        (error: HttpErrorResponse) => {
          console.error('Error details:', error);
          if (error.error) {
            //  console.error('Error code:', error.error.code);
            //  console.error('Error message:', error.error.message);
            this.toastr.error(error.error.message, error.error.code); this.isLoadding = false;
          }
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
          this.isLoadding = false;
        }

      );
    this.errorForm.reset();
  }
  getProductSubMaterial(productId: number, mateId: number) {
    this.isLoadding = true;
    this.jobService.getSubMTRProduct(productId, mateId).subscribe(
      data => {
        // Handle the data as needed
        if (data.code === 1000) {
          this.subMaterialProduct = data.result;
          this.isLoadding = false;
          console.log("Sub-material product data:", this.subMaterialProduct);
        } else {
          console.error('Failed to fetch products:', data);
          this.isLoadding = false;
          this.toastr.error('Không thể lấy danh sách vật liệu sản phẩm!', 'Lỗi');
        }
      },
      error => {
        console.error("Error fetching sub-material data:", error);
      }
    );
  }
  // hàm search api bằng produc thawojc product rq
  onSearch(selectedCategory: number, searchKey: string): void {
    console.log('Thực hiện tìm kiếm:', searchKey);
    console.log('Category:', selectedCategory);
    this.isLoadding = true;
    if (selectedCategory === 1) {
      console.log("Tìm kiếm sản phẩm có sẵn")
      this.jobService.getProductJobByNameOrCode(searchKey).subscribe(
        (data) => {
          if (data.code === 1000) {
            this.productRQs = data.result;
            console.log('Danh sách sản phẩm search:', this.productRQs);

          } 
          else (
            this.toastr.error('Không tìm thấy sản phẩm!', 'Lỗi')
          )
          this.isLoadding = false;
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
          this.isLoadding = false;
        }
      );
      this.isLoadding = false;
  } else if (selectedCategory === 0) {
      console.log("Tìm kiếm sản phẩm yêu cầu")
      this.jobService.seachRequestByName(searchKey).subscribe(
        (data) => {
          if (data.code === 1000) {
            this.productRQs = data.result;
            console.log('Danh sách sản phẩm request:', this.productRQs);

          }
          this.isLoadding = false;
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
          this.isLoadding = false;
        }
      );

      this.isLoadding = false;
    }
  }
  // onSearchInput(cate: number,searchKey: any){
  //   console.log("Selected cate: ", cate)
  //   console.log("Search key: ", searchKey)
  // }
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