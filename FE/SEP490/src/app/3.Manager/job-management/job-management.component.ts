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
import { EmployeeService } from 'src/app/service/employee.service';
import { isCancel } from 'axios';
import { ErrorProductService } from 'src/app/service/error-product.service';

declare var $: any;

@Component({
  selector: 'app-job-management',
  templateUrl: './job-management.component.html',
  styleUrls: ['./job-management.component.scss']
})

export class JobManagementComponent implements OnInit {

  @ViewChild('launchModalButton')
  
  launchModalButton!: ElementRef;
  errorReport!: ElementRef;
  products: any[] = [];
  errorForm: FormGroup;
  editJob: FormGroup;
  keyword = 'product_name';
  createJobs: FormGroup;
  productRQs: any[] = [];
  currentPage: number = 1;
  searchKey: string = '';
  selectedEmployee: string = '';
  selectedCategory: number = 0;
  selectedStatus: number = 0;
  selectedProduct: any = {}; // Biến để lưu trữ sản phẩm được chọn
  positionEmployees: any[] = [];
  positions: any[] = [];
  type: any = {};
  statusType: any[] = [];
  statusOptions: any[] = [];
  productForm: FormGroup;
  detailJob: FormGroup;
  costJob: any; // Cost of the job
  jobDescription: string = ''; // Job description
  pForJob: any; // List of products for the job
  materialProduct: any[] = [];
  editForm: FormGroup;
  confirmStatus: string = '';
  selectedProductNameCurrentDelele: number = 0;
  showModal = false;
  position: number = 0;
  quantityJobs: FormGroup;
  subMaterialProduct: any[] = [];
  isProduct: boolean = true; // check product or product request
  isLoadding: boolean = false;
  jobList: any[] = [];
  showPrimaryModal: boolean | undefined;
  showWarningModal: boolean | undefined;
  @ViewChild('errorProduct') errorProduct: any;
  @ViewChild('otherModal') otherModal: any;
  constructor(private fb: FormBuilder,private errorProductService: ErrorProductService, private employeeService: EmployeeService, private productList: ProductService, private productListService: ProductListService, private jobService: JobService, private toastr: ToastrService, private sanitizer: DomSanitizer) {
    this.createJobs = this.fb.group({
      job_name: [''],
      quantity_product: [''],
      cost: [],
      description: [''],
      finish: [''],
      start: [''],
    });
    this.editForm = this.fb.group({
      code: ['', Validators.required],
      code_order: ['', Validators.required],
      des: ['', Validators.required],
      employee_name: ['', Validators.required],
      fix: [false, Validators.required],
      id: [0, Validators.required],
      job_id: [0, Validators.required],
      job_name: ['', Validators.required],
      position_id: [0, Validators.required],
      position_name: ['', Validators.required],
      product_id: [0, Validators.required],
      product_name: ['', Validators.required],
      quantity: [1, Validators.required],
      request_product_id: [0, Validators.required],
      request_product_name: ['', Validators.required],
      solution: ['', Validators.required],
      user_name_order: ['', Validators.required]
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

  ngOnInit(): void {
    this.showLoadingIndicator();
   
    Promise.all([
      this.loadProductRQForJob(),
      this.loadStatusByType(),
     //  this.loadProductNgOn(),
      this.getAllPostionEmp(), 
      this.getAllStatusJob(),
      this.loadPosition(),
      this.loadAutoSearchProduct()
    ]).then(() => {
      this.hideLoadingIndicator();
    }).catch(error => {
      console.error('Error loading data:', error);
      this.hideLoadingIndicator();
    });
  }
  showLoadingIndicator() {
    this.isLoadding = true;
  }

  hideLoadingIndicator() {
    this.isLoadding = false;
  }
  selectedModalJob: string = '';
  selectedModalId: string = '';
  errorCheckResults: { [key: number]: boolean } = {};
  indexStatus: number = 0;

  openModal(event: Event, jobId: number, index: number): void {

    console.log('event:', event);
    const statusId = (event.target as HTMLSelectElement).value;
    this.selectedModalJob = jobId.toString();

    this.selectedModalId = statusId;

    this.launchModalButton.nativeElement.click();
    this.indexStatus = index;

  }

  closeModal() {
    var element = document.getElementById("mySelect" + this.indexStatus);
    if (element instanceof HTMLSelectElement) {
      element.selectedIndex = 0;
    }


  }
cancelChangeStatusJob() {
    this.selectedModalId = '';
  }
  

  acceptJob(jobId: string, statusId: string): void {
    this.isLoadding = true

    this.createJobs.reset();

    if (this.selectedCategory === 1) {
      console.log('SP có sẵn');

    } else if (this.selectedCategory === 0) {
      console.log('SP không có sẵn');
    }

    this.jobService.acceptJob(jobId, statusId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.toastr.success('Cập nhật trạng thái làm việc thành công!', 'Thành công');
      //    console.log('Đổi trạng thái job thành công'); this.isLoadding = false;
          if (this.selectedCategory === 1) {
            console.log('SP có sẵn');
            $('[data-dismiss="modal"]').click();
            this.loadProduct();
          } else if (this.selectedCategory === 0) {
        //    console.log('SP không có sẵn');
            this.loadProductRQForJob();
            $('[data-dismiss="modal"]').click();
          }
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
  selectedStatusJob: string = '';
  getAllStatusJob(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.jobService.getStatusByType().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.statusSearch = data.result.filter((item: any) => [1, 2, 3, 4].includes(item.type));
     //       console.log('Status Job :', this.statusSearch);
          }
          resolve();
        },
        (error) => {
          console.error('Error fetching data:', error);
          reject(error);
        }
      );
    });
  }

  openConfirmChangeStatusJob(product: any): void {

    this.selectedProduct = { ...product };
    //  console.log('selectedProduct.job_id:', this.selectedProduct.job_id);
    //  console.log('this.selectedProduct.type:', this.selectedProduct.statusJob.status_id);
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


  selectedEmpCreateJob: any = {};
  createNewJob() {
    
   // this.isLoadding = true;
    console.log('Selected Employee:', this.selectedEmployee);
    console.log('Selected Product:', this.selectedProduct);
    const user_id = this.selectedEmployee;
    const p_id = this.selectedProduct.product_id; // Thay đổi giá trị tùy theo sản phẩm
    let status_id = this.selectedProduct.statusJob?.status_id; // Thay đổi giá trị tùy theo trạng thái
    const job_id = this.selectedProduct.job_id; // Thay đổi giá trị tùy theo công việc
    const type_id = this.selectedCategory; // cho sp có sẵn     0 - k có sẵn
    let position_id = this.selectedProduct.position_id + 1;
    
    //  console.log('JobName:', this.selectedProduct.position_name);
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


    const quantity = this.selectedProduct.quantity;
    let modifiedPositionName = this.positionName.replace(/Thợ/g, 'Làm');
    console.log('Modified Position Name:', modifiedPositionName);
    const createJobs = {
      quantity_product: quantity,
      job_name: modifiedPositionName
    };
    console.log('createJobs before API call:', createJobs);

      console.log('code :', this.selectedProduct.code);
    if (this.selectedProduct.code == null) {
      this.isLoadding = true
    //  console.log("consolo lod: ", this.isLoadding)
    console.log("create form:", createJobs);
    console.log('API parameters:', p_id, position_id, user_id, createJobs);
      this.jobService.createExportMaterialProductTotalJob(p_id, position_id, user_id, createJobs).subscribe(
        (data) => {
          if (data.code === 1000) {
            console.log('Xuất nguyên liệu thành công');
            this.jobService.addJob(user_id, p_id, status_id, job_id, type_id, createJobs).subscribe(
              (data) => {
                if (data.code === 1000) {
                  this.toastr.success('Giao việc thành công', 'Thành công');
                  this.pForJob = data.result;
                  // console.log('Add product for job:', this.pForJob);


                  this.isLoadding = false;
           //       console.log("consolo lod flase: ", this.isLoadding)
                 this.loadProduct();
                  $('[data-dismiss="modal"]').click();
                }
                this.isLoadding = false;
              },
              (error) => {
                console.error('Error fetching products:', error);
                this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); this.isLoadding = false;
                $('[data-dismiss="modal"]').click();

              }
            );
          } else if (data.code === 1015) {
            console.error('Failed to fetch products:', data);
            this.toastr.warning('Số lượng nguyên vật liệu trong kho không đủ', 'Thông báo');
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

      this.isLoadding = true
    //  console.log("consolo lod: ", this.isLoadding)
    //  console.log("exort submaterial product request");
      this.jobService.createExportMaterialRequestTotalJob(p_id, position_id, user_id, createJobs).subscribe(
        (data) => {
          if (data.code === 1000) {
            console.log('Xuất nguyên liệu thành công');
            this.jobService.addJob(user_id, p_id, status_id, job_id, type_id, jobData).subscribe(
              (data) => {
                if (data.code === 1000) {
                  this.pForJob = data.result;
                  this.toastr.success('Giao việc thành công', 'Thành công');

                  // console.log('Add product for job:', this.pForJob);
                  // this.toastr.success('Thêm sản phẩm sản xuất thành công!', 'Thành công');
                  $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          //        console.log("consolo lod false: ", this.isLoadding)
                  this.loadProductRQForJob();
                } else {
                  // console.error('Failed to fetch products:', data);
                  //  this.toastr.error('Thêm sản phẩm sản xuất thất bại!', 'Lỗi'); this.isLoadding = false;
                }
              },
              (error) => {
                // console.error('Error fetching products:', error);
                this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); this.isLoadding = false;
              }
            );
          } else if (data.code === 1015) {
            console.error('Failed to fetch products:', data);
            this.toastr.warning('Số lượng nguyên vật liệu trong kho không đủ', 'Thông báo');
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
    this.isLoadding = true;
    if (this.productForm.invalid) {
      return;
    }
    this.createJobs.reset();
    const quantity = this.productForm.get('quantity')?.value;
    if (this.selectedProduct === null) {
      this.toastr.error('Hãy chọn một sản phẩm');
      return;
    }
    if (quantity <= 0) {
      this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      return;
    }
     console.log('Selected product for job:', this.selectedProduct.product_id);
    this.jobService.addProductForJob(this.selectedProduct.product_id, quantity).subscribe(

      (data) => {

        if (data.code === 1000) {
          this.pForJob = data.result;
          // console.log('Add product for job:', this.pForJob);
          this.toastr.success('Thêm sản phẩm sản xuất thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          this.loadProduct();
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Thêm sản phẩm sản xuất thất bại!', 'Lỗi');
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          this.loadProduct();
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        $('[data-dismiss="modal"]').click(); this.isLoadding = false;
      }
    );
  }
  selectedPosEmp: any = {};
  empId: number = 0;
  onChangeSelectedEmployee(event: any) {
    this.selectedEmployee = event.target.value;
    console.log('Selected employee ID:', this.selectedEmployee);
    this.empId = event.target.value;
    this.getPositionNameById(this.empId);
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
  loadStatusByType(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.jobService.getStatusByType().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.statusType = data.result;
          }
          resolve();
        },
        (error) => {
          console.error('Error fetching data:', error);
          reject(error);
        }
      );
    });
  }
  isButtonDisabled(): boolean {
    const selectedStatusId = this.selectedStatus;
    if (selectedStatusId === 10 || selectedStatusId === 7 || selectedStatusId === 4) {
      return true;
    }
    return false;
  }

  getStatusOptions(product: any) {
//    console.log('Product:', product);
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

  listErrorJob: any[] = [];
  loadProductRQForJob(): Promise<void> {
    this.currentPage = 1;
    return new Promise((resolve, reject) => {
      this.isLoadding = true;
    
      this.jobService.getListProductRQ().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.productRQs = data.result;
            console.log('Sp cho job:', this.productRQs);
            this.checkJobsForErrors();
          } else {
            console.error('Failed to fetch products:', data);
            this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
          }
          this.isLoadding = false;
          resolve();
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
          this.isLoadding = false;
          reject(error);
        }
      );
    });
  }

  checkJobsForErrors() {
    this.listErrorJob = [];
    const errorCheckPromises = this.productRQs.map((product) => {
      return this.jobService.checkErrorOfJob(product.job_id).toPromise().then(
        (data) => {
          if (data.result === false) {
  //          console.log(`Job ${product.job_id} has errors`);
            this.listErrorJob.push(product.job_id);
          }
        },
        (error) => {
          console.error(`Error checking job ${product.job_id}:`, error);
        }
      );
    });

    Promise.all(errorCheckPromises).then(() => {
      this.isLoadding = false;
   //   console.log('Jobs with errors:', this.listErrorJob);
    });
  }
  loadProduct() {
    this.currentPage = 1;
    this.jobService.getListProduct().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.productRQs = data.result;
          this.selectedCategory = 1;
          // console.log('Sp cho job:', this.productRQs);
          this.checkJobsForErrors();
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
  jobId: number = 0;
  isDisabled(product: any): boolean {
    return [10, 7, 4, 14].includes(product.statusJob.status_id);
  }

  isErrorDisabled(product: any): boolean {
    return [6, 9, 12, 14].includes(product.statusJob.status_id) || product.statusJob.type === 1;
  }
  productAutoSearch: any[] = [];
  loadAutoSearchProduct(){
    this.productListService.getProducts().subscribe(
      (data) => {
        this.isLoadding = false;
        if (data.code === 1000) {
          this.productAutoSearch = data.result;
           console.log('Danh sách sản phẩm:', this.productAutoSearch);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        this.isLoadding = false;
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }

  manageJob(product: any): void {
    this.isLoadding = true;

    this.selectedProduct = { ...product };
  //  console.log('Product:', this.selectedProduct);
    this.jobId = this.selectedProduct.job_id;
    this.jobService.getJobDetailById(this.jobId).subscribe(
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
    );
    //   console.log("Sản phẩm được chọn để giao việc:", this.selectedProduct);
    //   console.log("thiis.type_id: ", this.selectedProduct.code);
    // console.log("Trạng thái của sản phẩm:", this.selectedProduct.statusJob?.status_id);
    this.createJobs.reset();


    //console.log("employee:", this.selectedProduct.user_name);

    if (this.selectedProduct) {
      this.loadPosition3(product);
    }
    // Sử dụng regex để thay thế chữ "Thợ" bằng chữ "Làm"
    let modifiedPositionName = this.selectedProduct.position_name.replace(/Thợ/g, 'Làm');

    // Gán giá trị vào form createJobs
    this.createJobs.patchValue({
      quantity_product: this.selectedProduct.quantity,
      job_name: modifiedPositionName
    });



    // Call getSubMTRProduct with appropriate IDs
    let productId = this.selectedProduct.product_id;
    let mateId = this.selectedProduct.position_id;
    const typeId = this.selectedProduct.type_id;

    mateId += 1;
    // console.log("Product ID:", productId);
    // console.log("Material ID:", mateId);
    if (this.selectedProduct.code != null) {
      this.getSubMTRProductRQ(productId, mateId);
    } else if (this.selectedProduct.code == null) {
      this.getProductSubMaterial(productId, mateId);
    }

    $('[data-dismiss="modal"]').click(); this.isLoadding = false;
  }

   isCancel: boolean = false;
  cancelAssign(): void {
    if (this.isCancel) {
      console.log('Cancel Assign Called');
  
    
      if (this.selectedCategory === 0) {
        this.loadProductRQForJob();
      } else if (this.selectedCategory === 1) {
        this.loadProduct();
      }
    }
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
  selected_productName: string = '';
  positionEmpList: any[] = [];
  selectedPosition: any = {};
  statusSearch: any = {};
  getAllPostionEmp(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.employeeService.getAllPostionEmp().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.positionEmpList = data.result;
            console.log('Danh sách chức vụ nhân viên: ', this.positionEmpList);
          } else {
            console.error('Failed to fetch products:', data);
          }
          resolve();
        },
        (error) => {
          console.error('Error fetching data:', error);
          reject(error);
        }
      );
    });
  }
  showJobDetail(job: any) {
    let mate_id = job.position_id;
  //  console.log('Mate ID:', mate_id);
   // console.log('Product ID:', job.product_id);
   // console.log('productName:', job.product_name);
    this.selected_productName = job.product_name;
    // this.isLoadding = true;  
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
    //      console.error('Failed to fetch products:', data);
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
  user_name: string = '';
  JOBID: number = 0;
  editJobDetail(job: any, job_id: number): void {
  //  console.log('Job ID:', job_id);
    this.JOBID = job_id;
    this.isLoadding = true;
    this.user_name = job.user_name;
 //   console.log('User name:', this.user_name);
    // Fetch job detail by ID
    this.jobService.getJobDetailById(job_id).subscribe(
      (data) => {
        //  console.log('Job detail:', data.result);
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
  //        console.log('Form editJob value:', this.editJob.value);
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
    const jobid = this.JOBID;
  //  console.log('JOBID: ', this.JOBID)
   // console.log('error edit form saveChanges:', errorFormData);
    this.jobService.editJob(jobid, errorFormData).subscribe(
      (response) => {
        if (response.code === 1000) {
          this.toastr.success('Cập nhật thông tin sản xuất thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          this.ngOnInit();
        } else {
          console.error('Failed to edit product:', response);
          this.toastr.error('Không thể cập nhật thông tin sản xuất!', 'Lỗi'); this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        }
      },
      (error) => {
        console.error('Error editing product:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); this.isLoadding = false;
      }
    );

  }
  
  createErrorJob() {
    this.isLoadding = true;

    const jobId = this.selectedProduct.job_id; // Hoặc lấy từ giá trị khác nếu cần
  //  console.log('Job ID:', jobId);
    const formValues = this.errorForm.value;
  //  console.log('Form values:', formValues);
    this.productListService.createProductError(
      jobId,
      formValues.description,
      formValues.solution,
      formValues.quantity
    )

      .subscribe(
        (data) => {
          console.log('Form values:', formValues);
          if (data.code === 1000) {

            console.log('Thêm lỗi sản phẩm thành công:');
            this.toastr.success('Thêm lỗi sản phẩm thành công!', 'Thành công');
            $('[data-dismiss="modal"]').click(); this.isLoadding = false;
            this.loadProduct();
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
    //      console.log("Sub-material product data:", this.subMaterialProduct);
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
  checkNotFound: boolean = false;
  selectedPositionId: any = '';
  // hàm search api bằng produc thawojc product rq
  searchJob(selectedCategory: number) {
    if (selectedCategory === 1) {
      this.loadProduct();
    } else if (selectedCategory === 0) {
      this.loadProductRQForJob();
    }
  }
  showError: boolean = false;
  onSearch(selectedCategory: number, searchKey: string): void {
    this.checkNotFound = false;
    // console.log('checkNotFound:', this.checkNotFound);
  //  console.log("Selected cate: ", this.selectedStatusJob)
    // console.log('Thực hiện tìm kiếm:', searchKey);
    // console.log('Category:', selectedCategory);
    this.isLoadding = true;
    if (selectedCategory === 1) {
      this.jobService.multiSearchJob(searchKey, this.selectedStatusJob, this.selectedPosSearch).subscribe(
        (data) => {
          console.log('Data:', data);
          if (data.code === 1000) {
            this.productRQs = data.result;
            this.currentPage = 1;
      //      console.log('Danh sách sản phẩm search:', this.productRQs);
          } else if (data.code === 1015) {
       //     console.log('Danh sách sản phẩm search:', this.productRQs);
            this.checkNotFound = true;
            this.productRQs = [];
       //     console.log("checkNotFound job:", this.checkNotFound);
          }

          this.isLoadding = false;
          this.checkNotFound = true;
        },
        (error) => {
       //   console.log('Error fetching products:', error);

          this.isLoadding = false;
          this.checkNotFound = true;

        }
      );
      this.isLoadding = false;
    } else if (selectedCategory === 0) {

      this.jobService.multiSearchJobRequest(searchKey, this.selectedStatusJob, this.selectedPosSearch).subscribe(
        (data) => {
          if (data.code === 1000) {
            this.productRQs = data.result;
      //      console.log('Danh sách sản phẩm request:', this.productRQs);
            this.currentPage = 1;
          } else if (data.code === 1015) {
       //     console.log('Khoong thay san pham request');
            this.productRQs = [];
            this.checkNotFound = true;
      //      console.log("checkNotFound jobRequest:", this.checkNotFound);
          }

          this.isLoadding = false;
          this.checkNotFound = true;
        },
        (error) => {
          this.isLoadding = false;
          this.checkNotFound = true;
        }
      );

      this.isLoadding = false;
    }
  }
  // onSearchInput(cate: number,searchKey: any){
  //   console.log("Selected cate: ", cate)
  //   console.log("Search key: ", searchKey)
  // }
  positionName: string = '';
  getPositionNameById(id: number): string {
    this.positionName = '';
    console.log('Position Employees:', this.positionEmployees);

    console.log('Position Name check id:', typeof id);

    this.positionEmployees.forEach((item) => {
        const userIdNum = item.userId.toString(); // Chuyển userId thành số
        console.log('Type of userId:', typeof userIdNum);
        if (userIdNum === id) {
            console.log('Position Name check id:', item.position?.position_name);
            this.positionName = item.position?.position_name;
        }
    });

    console.log('Position Name check id:', this.positionName);
    return this.positionName;
}

  loadPosition3(product: any) {
    this.selectedProduct = { ...product };
    this.type = this.selectedProduct.statusJob.type;
    let positon = this.selectedProduct.position_id;
 //   console.log('posion truoc khi cong:', positon);
    const statusId = this.selectedProduct.statusJob?.status_id;
  //  console.log('Status ID:', this.selectedProduct.statusJob?.status_id);

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
  selectedPosSearch: any = '';
  positionsSearch: any[] = [];
  loadPosition(): Promise<void> {
    return new Promise((resolve, reject) => {
      this.productListService.getAllPosition().subscribe(
        (data: any) => {
          if (data.code === 1000) {
            this.positionsSearch = data.result;
      //      console.log("Position search: ", this.positionsSearch);
          } else {
            console.error('Invalid data returned:', data);
          }
          resolve();
        },
        (error) => {
          console.error('Error fetching positions:', error);
          reject(error);
        }
      );
    });
  }
  openModal2(product: any) {
    console.log('open Modal 2:', product);
    console.log('job_id: ', product.job_id)
    this.editProduct(product.job_id, product);
  }

  errorDetail: any = {};

  shouldShowAnotherButton(): boolean {
    // Replace this logic with your own conditions
    // For example, return true if some condition is met to show the second button
    return this.isCancel; // or use any logic you need
  }
  isEditing: boolean = false;
  editProfile() {
    this.isEditing = true;
  }
  cancelChanges() {
    this.isEditing = false;
    // Reload the user profile to discard changes
    this.jobService.getAllProductErrorsByJobId(this.errorId).subscribe(
      (response) => {
        this.jobErrors = response.result;
       // console.log('error history: ', this.editForm.value);
        if (this.jobErrors.length > 0) {
          this.editForm.patchValue(this.jobErrors[0]);
        }
        console.log("error history: ", this.editForm.value);
      },
      (error) => {
        console.error('Error fetching product errors:', error);
      }
    );
  }
  jobErrors: any = {};
  errorHistory: any = [];
  error_job_id: any;
  errorId: any;
  editProduct(errorid: number, product: any) {
    this.errorId = errorid;
    console.log('report product function:', errorid);
    console.log('Product:', product);
    this.error_job_id = product.product_error_id;
    this.jobService.getAllProductErrorsByJobId(errorid).subscribe(
      (response) => {
        this.jobErrors = response.result;
       // console.log('error history: ', this.editForm.value);
        if (this.jobErrors.length > 0) {
          this.editForm.patchValue(this.jobErrors[0]);
        }
        console.log("error history: ", this.editForm.value);
      },
      (error) => {
        console.error('Error fetching product errors:', error);
      }
    );
  }
  errorHistoryjob(jobId: number){
    this.jobService.getAllProductErrorsByJobId(jobId).subscribe(
      (response) => {
        this.errorHistory = response.result;
        console.log('error history: ', this.errorHistory);
      },
      (error) => {
        console.error('Error fetching product errors:', error);
      }
    );
  }
  saveChangesError(){
    //  this.isLoadding = true;
      const errorFormData = this.editForm.value;
      const jobid = this.selectedProduct.job_id;
      console.log('error edit form saveChanges:', errorFormData);
      console.log('error_job_id: ', this.error_job_id)
      this.errorProductService.editProductError(this.editForm.value.job_id, this.editForm.value).subscribe(
        (response) => {
          if (response.code === 1000) {
            this.toastr.success('Sửa lỗi sản phẩm thành công!', 'Thành công');
            $('[data-dismiss="modal"]').click(); this.isLoadding = false;
            this.ngOnInit();
          } else {
            console.error('Failed to edit product:', response);
            this.toastr.error('Không thể sửa sản phẩm!', 'Lỗi'); this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          }
        },
        (error) => {
          console.error('Error editing product:', error);
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); this.isLoadding = false;
        }
      );
    }
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
}