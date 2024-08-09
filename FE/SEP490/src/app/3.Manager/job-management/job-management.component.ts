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
import { get } from 'jquery';

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
  keyword = 'productName';
  createJobs: FormGroup;
  productRQs: any[] = [];
  currentPage: number = 1;
  searchKey: string = '';
  selectedEmployee: string = 'chon1nhanvien';
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
  updateForm: FormGroup;
  confirmStatus: string = '';
  selectedProductNameCurrentDelele: number = 0;
  showModal = false;
  position: number = 0;
  quantityJobs: FormGroup;
  subMaterialProduct: any[] = [];
  isProduct: boolean = true; // check product or product request
  isLoadding: boolean = false;
  jobList: any[] = [];
  isChooseProduct: boolean = false; // dung de kiem tra quantity
  showPrimaryModal: boolean | undefined;
  showWarningModal: boolean | undefined;
  @ViewChild('errorProduct') errorProduct: any;
  @ViewChild('otherModal') otherModal: any;
  @ViewChild('employeeSelect') employeeSelect!: ElementRef<HTMLSelectElement>;
  @ViewChild('employeeSelect2') employeeSelect2!: ElementRef<HTMLSelectElement>;
  constructor(private fb: FormBuilder, private errorProductService: ErrorProductService, private employeeService: EmployeeService, private productList: ProductService, private productListService: ProductListService, private jobService: JobService, private toastr: ToastrService, private sanitizer: DomSanitizer) {
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
    this.updateForm = this.fb.group({
      description: ['', Validators.required],
      solution: ['', Validators.required],
      isFixed: [true, Validators.required],
      quantity: [0, [Validators.required, Validators.min(0)]]
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
      isFixed: [],
      quantity: [''],
    });
    this.productForm = this.fb.group({
      quantity: ['']
    });
    this.quantityJobs = this.fb.group({
      quantity: ['']
    });
  }

  ngOnInit(): void {
    this.showLoadingIndicator();
    this.selectedEmployee = '';
    Promise.all([
      this.loadProductRQForJob(),
      this.loadStatusByType(),
      //  this.loadProductNgOn(),
      this.getAllPostionEmp(),
      this.getAllStatusJob(),
      this.loadPosition(),
      // this.loadAutoSearchProduct()
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
  indexStatus:any;

  openModal(event: Event, jobId: number, index: number): void {
    this.indexStatus = 0;
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
          this.toastr.info('Cập nhật trạng thái làm việc thất bại!', 'Lỗi');
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
  positionName: string = '';
  getPositionNameById(id: number): string {
    this.positionName = '';
    console.log('Position id:', id);
    if (id === 0) {
      this.positionName = 'Làm mộc'
    }
    if (id === 1) {
      this.positionName = 'Làm nhám'
    }
    if (id === 2) {
      this.positionName = 'Phun sơn'
    }
   // console.log('Position Name:', this.positionName);
    return this.positionName;
  }
  positionCreateJob: any;
  createNewJob() {
    this.isCancel = true;
   
    this.isLoadding = true;
    console.log('Selected Employee:', this.selectedEmployee);
    //console.log('Selected Product:', this.selectedProduct);
    const user_id = this.selectedEmployee;
    const p_id = this.selectedProduct.product_id; // Thay đổi giá trị tùy theo sản phẩm
    let status_id = this.selectedProduct.statusJob?.status_id; // Thay đổi giá trị tùy theo trạng thái
    const job_id = this.selectedProduct.job_id; // Thay đổi giá trị tùy theo công việc
    const type_id = this.selectedCategory; // cho sp có sẵn     0 - k có sẵn
    let position_id = this.selectedProduct.position_id + 1;
    this.positionCreateJob = this.selectedProduct.position_id + 1;
    let modifiedPositionName = '';

  //  console.log('Position Name creaetNewJob:', this.positionCreateJob);
    // if (!user_id || !p_id || !status_id || !job_id || !type_id || !position_id) {
    //   // Nếu bất kỳ trường nào là null hoặc không được cung cấp
    //   this.toastr.error('Vui lòng nhập đầy đủ thông tin trước khi tiếp tục.', 'Lỗi');
    //   // Có thể thêm logic để dừng tiến trình nếu cần
    //   return;
    // }
    const quantity = this.selectedProduct.quantity;
    const cost = this.createJobs.get('cost')?.value;
    const start = this.createJobs.get('start')?.value;
    const finish = this.createJobs.get('finish')?.value;
    const description = this.createJobs.get('description')?.value;
    const today = new Date();
    if (!user_id) {
      this.toastr.info('Vui lòng chọn nhân viên', 'Thông báo');
      this.isLoadding = false;
      return;
    }
    if (!quantity || quantity <= 0) {
      this.toastr.info('Số lượng sản phẩm phải lớn hơn 0.', 'Thông báo'); this.isLoadding = false;
      return;
    }

    if (!cost || cost <= 0) {
      this.toastr.info('Giá công khoán phải lớn hơn 0.', 'Thông báo'); this.isLoadding = false;
      return;
    }
    if (!start) {
      this.toastr.info('Không được để trống ngày yêu cầu nhận công việc.', 'Thông báo'); this.isLoadding = false;
      return;
    }
    if (!finish) {
      this.toastr.info('Không được để trống ngày yêu cầu hoàn thành công việc.', 'Thông báo'); this.isLoadding = false;
      return;
    }
    if (start < today) {
      this.toastr.info('Ngày nhận việc phải chọn sau hôm nay.', 'Thông báo'); this.isLoadding = false;
      return;
    }

    if (finish < start) {
      this.toastr.info('Ngày yêu cầu hoàn thành công việc phải sau ngày yêu cầu nhận việc.', 'Thông báo'); this.isLoadding = false;
      return;
    }

    if (!description || description.length < 3) {
      this.toastr.info('Mô tả không được để trống hoặc ít hơn 3 ký tự.', 'Thông báo'); this.isLoadding = false;
      return;
    }
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

  //  console.log('Modified Position Name:', modifiedPositionName);
    const createJobs = {
      quantity_product: quantity,
      job_name: modifiedPositionName,

    };
  //  console.log('createJobs before API call:', this.createJobs.value);

  //  console.log('cost :', this.createJobs.value.cost);
    if (this.selectedProduct.code == null) {
      //    this.isLoadding = true
      //  console.log("consolo lod: ", this.isLoadding)
     // console.log("create form:", createJobs);
    //  console.log('API parameters:', user_id, p_id, status_id, job_id, type_id, createJobs);
      this.jobService.createExportMaterialProductTotalJob(p_id, position_id, user_id, createJobs).subscribe(
        (data) => {
          if (data.code === 1000) {
            console.log('Xuất nguyên liệu thành công 1');
            this.jobService.addJob(user_id, p_id, status_id, job_id, type_id, this.createJobs.value).subscribe(
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
                this.toastr.warning(error.error.message, 'Thông báo');
                this.isLoadding = false;
              //  $('[data-dismiss="modal"]').click();
                // console.log('error: ', error)
                // console.log('Error Code:',error.error.code);
                // console.log('Error Message:', error.error.message);

              }
            );
          } else if (data.code === 1015) {
            console.error('Failed to fetch products:', data);
            this.toastr.warning('Số lượng nguyên vật liệu trong kho không đủ', 'Thông báo');
          //  $('[data-dismiss="modal"]').click(); 
          this.isLoadding = false;

          }
        },
        (error) => {
          console.error('Error:', error);
          if (error && error.error && error.error.errors) {
            Object.entries(error.error.errors).forEach(([key, value]) => {
              this.toastr.warning(`${key}: ${value}`, 'Thông báo');
              $('[data-dismiss="modal"]').click(); this.isLoadding = false;
            });

          } else if (error && error.message) {
            this.toastr.info(`Có lỗi xảy ra: ${error.message}`, 'Thông báo'); this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          } else {
            this.toastr.info(`Có lỗi xảy ra, vui lòng thử lại.`, 'Thông báo'); this.isLoadding = false;
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
           // console.log('Xuất nguyên liệu thành công');
            this.jobService.addJob(user_id, p_id, status_id, job_id, type_id, jobData).subscribe(
              (data) => {
                if (data.code === 1000) {
                  this.pForJob = data.result;

                  $('[data-dismiss="modal"]').click(); this.isLoadding = false;

                  this.loadProductRQForJob();
                } 
              },
              (error) => {
                // console.error('Error fetching products:', error);
                this.toastr.warning(error.error.message, 'Thông báo');
                this.isLoadding = false;
                $('[data-dismiss="modal"]').click();
                // console.log('error: ', error)
                // console.log('Error Code:',error.error.code);
                // console.log('Error Message:', error.error.message);

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
              this.toastr.warning(`${key}: ${value}`, 'Thông báo');
             // $('[data-dismiss="modal"]').click(); 
              this.isLoadding = false;
            });

          } else if (error && error.message) {
            this.toastr.info(`Có lỗi xảy ra: ${error.message}`, 'Thông báo'); this.isLoadding = false;
          //  $('[data-dismiss="modal"]').click();
          } else {
            this.toastr.info(`Có lỗi xảy ra, vui lòng thử lại.`, 'Thông báo'); this.isLoadding = false;
         //   $('[data-dismiss="modal"]').click();
          }
        }
      );
    }

  }
  selectEvent(item: any) {
    this.selectedProduct = item;
  }
  ngAfterViewInit() {
    // Bạn có thể sử dụng ViewChild sau khi view đã được khởi tạo
    console.log('Employee Select Element:', this.employeeSelect);
  }
  callViewChild() {
    this.createJobs.get('employeeSelect')?.setValue(''); // Reset giá trị của FormControl
  }

  selectedAddProduct: string = '';
  onSubmit() {
    console.log('Sản phẩm autoSearch: ', this.selectedAddProduct)
    // this.isLoadding = true;
    if (this.productForm.invalid) {
      return;
    }
    console.log('Sản phẩm autoSearch: ', this.productAutoSearch)
    console.log('Selected product for job:', this.productIdCoSan);
    console.log('Selected product for job:', this.selectedProduct);
    console.log('searchKeyword:', this.selectedAddProduct);
   // console.log('Selected AddProduct:', this.selectedAddProduct);
    this.createJobs.reset();
    const quantity = this.productForm.get('quantity')?.value;
    console.log('productForm:', this.productForm.value);
    if (this.productIdCoSan === 0) {
      this.toastr.info('Sai tên sản phẩm có sẵn');
      console.log("Kiểm tra lại tên sản phẩm có sẵn");
      return;
    }
    if (quantity <= 0) {
      this.toastr.info('Số lượng sản phẩm sản xuất phải lớn hơn 0');
      return;
    }
    
    this.selectedProduct = '';
    this.jobService.addProductForJob(this.productIdCoSan, quantity).subscribe(
      (data) => {

        if (data.code === 1000) {
          this.pForJob = data.result;
          // console.log('Add product for job:', this.pForJob);
          this.toastr.success('Thêm sản phẩm sản xuất thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          this.loadProduct();
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.info('Thêm sản phẩm sản xuất thất bại!', 'Thông báo');
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
          this.loadProduct();
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.info('Có lỗi xảy ra!', 'Thông báo');
        $('[data-dismiss="modal"]').click(); this.isLoadding = false;
      }
    );
  }
  selectedPosEmp: any = {};
  empId: number = 0;
  onChangeSelectedEmployee(event: any) {
    this.selectedEmployee = event.target.value;
   // console.log('Selected employee ID:', this.selectedEmployee);
    this.empId = event.target.value;
    this.getPositionNameById(this.empId + 1);
  }
  selectedEmployee2: any;
  empId2: number = 0;
  onChangeSelectedEmployee2(event: any) {
    this.selectedEmployee2 = event.target.value;
  //  console.log('Selected employee joy 2:', this.selectedEmployee2);
    this.empId2 = event.target.value;
    this.getPositionNameById(this.empId2 + 1);
  }
  huyTaoSanPhamCoSan(): void {
    this.productForm.reset();
    this.isChooseProduct = false

  }
  onChangeSearch(search: string) {
    this.searchKey = search;
  }

  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
  }

  productIdCoSan: number = 0;
  selectProduct(product: any): void {
    this.selectedProduct = product;
    console.log('Sản phẩm được chọn:', this.selectedProduct.productId);
    this.productIdCoSan = this.selectedProduct.productId;
    this.isChooseProduct = true;
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
          this.toastr.info('Không thể lấy danh sách sản phẩm!', 'Thông báo');
          this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        }
      },
      (error) => {
        console.error('Error accepting job:', error);
        this.toastr.info('Có lỗi xảy ra!', 'Thông báo'); this.isLoadding = false;
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

                    // Gọi checkJobsForErrors và chỉ resolve sau khi hoàn tất
                    this.checkJobsForErrors().then(() => {
                        resolve();
                    }).catch((error) => {
                        console.error('Error checking jobs for errors:', error);
                        reject(error);
                    });

                } else {
                    console.error('Failed to fetch products:', data);
                    this.toastr.info('Không thể lấy danh sách sản phẩm!', 'Thông báo');
                    this.isLoadding = false;
                    resolve(); // resolve dù không thành công để không treo Promise
                }
            },
            (error) => {
                console.error('Error fetching products:', error);
                this.toastr.info('Có lỗi xảy ra get data request!', 'Thông báo');
                this.isLoadding = false;
                reject(error);
            }
        );
    });
}

checkJobsForErrors(): Promise<void> {
    this.listErrorJob = [];
    
    // Tạo một Promise để đợi tất cả các kiểm tra lỗi hoàn thành
    return new Promise((resolve, reject) => {
        const errorCheckPromises = this.productRQs.map((product) => {
            return this.jobService.checkErrorOfJob(product.job_id).toPromise().then(
                (data) => {
                    if (data.result === false) {
                        this.listErrorJob.push(product.job_id);
                    }
                }
            );
        });

        Promise.all(errorCheckPromises).then(() => {
            this.isLoadding = false;
            // Sau khi kiểm tra lỗi hoàn tất, thực hiện in dữ liệu ra màn hình
            this.displayProducts();
            resolve();
        }).catch((error) => {
            this.isLoadding = false;
            reject(error);
        });
    });
}

// Hàm để thực hiện in dữ liệu ra màn hình sau khi kiểm tra lỗi hoàn tất
displayProducts() {
    console.log('Jobs with errors:', this.listErrorJob);
    // Thực hiện các hành động cần thiết để in dữ liệu ra màn hình
}

loadProduct() {
    this.currentPage = 1;
    this.jobService.getListProduct().subscribe(
        (data) => {
            if (data.code === 1000) {
                this.productRQs = data.result;
                this.selectedCategory = 1;
                // console.log('Sp cho job:', this.productRQs);

                // Gọi checkJobsForErrors sau khi nhận dữ liệu
                this.checkJobsForErrors();
            } else {
                this.toastr.info('Không thể lấy danh sách sản phẩm!', 'Thông báo');
            }
        },
        (error) => {
            this.toastr.info('Có lỗi xảy ra!', 'Thông báo');
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
  loadAutoSearchProduct(): void {
    this.isLoadding = true;
    this.productIdCoSan = 0;
    console.log("loadAutoSearchProduct: ", this.productIdCoSan);
    this.productListService.getProducts().subscribe(
      (data) => {

        if (data.code === 1000) {
          this.productAutoSearch = data.result;
          console.log('Danh sách sản phẩm auto complete:', this.productAutoSearch);
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.info('Không thể lấy danh sách sản phẩm!', 'Thông báo');
          this.isLoadding = false;
        }
      },
      (error) => {

        console.error('Error fetching products:', error);
        this.toastr.info('Có lỗi xảy ra!', 'Thông báo');
        this.isLoadding = false;
      }
    );
  }

  manageJob(product: any): void {
    this.employeeSelect.nativeElement.value = '';

    this.isLoadding = true;
    this.selectedEmployee = ''
    this.selectedProduct = { ...product };

    //  console.log('Product:', this.selectedProduct);
    this.jobId = this.selectedProduct.job_id;
    this.jobService.getJobDetailById(this.jobId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.selectedJob = data.result;
          console.log('selectedJob:', this.selectedJob);
          console.log('selectedProduct:', this.selectedProduct);
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.info('Không thể lấy danh sách sản phẩm!', 'Lỗi');
          this.isLoadding = false;
        }
      },
    );
    //   console.log("Sản phẩm được chọn để giao việc:", this.selectedProduct);
    //   console.log("thiis.type_id: ", this.selectedProduct.code);
    // console.log("Trạng thái của sản phẩm:", this.selectedProduct.statusJob?.status_id);
    this.createJobs.reset();
    //console.log("employee:", this.selectedProduct.user_name);

      this.loadPosition3(product);
    
    // Sử dụng regex để thay thế chữ "Thợ" bằng chữ "Làm"
    let modifiedPositionName = '';
    if (this.selectedProduct.position_id === 0) {
      modifiedPositionName = 'Làm mộc';
    }
    if (this.selectedProduct.position_id === 1) {
      modifiedPositionName = 'Làm nhám';
    }
    if (this.selectedProduct.position_id === 2) {
      modifiedPositionName = 'Phun sơn';
    }
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

  cancelAssign(cancel: boolean): void {
    this.employeeSelect.nativeElement.value = '';
  
    if(this.isCancel == true){
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
          this.toastr.info('Không thể lấy danh sách vật liệu sản phẩm!', 'Thông báo');
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
            //  console.log('Danh sách chức vụ nhân viên: ', this.positionEmpList);
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
      console.log('Mate ID:', mate_id);
     console.log('Product ID:', job.product_id);
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
          this.toastr.info('Không thể lấy danh sách vật liệu sản phẩm!', 'Lỗi');
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
          console.log('Form Assign value:', this.selectedJob);

          this.isLoadding = false;
        } else {
          //      console.error('Failed to fetch products:', data);
          this.toastr.info('Không thể lấy danh sách sản phẩm!', 'Thông báo');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error fetching products:', error);

        this.isLoadding = false;
      }
    );

  }
  selectedJob: any = {};
  user_name: string = '';
  JOBID: number = 0;
  formatDate(date: string): string {
    const d = new Date(date);
    const day = ('0' + d.getDate()).slice(-2);
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const year = d.getFullYear();
    return `${day} / ${month} / ${year}`;
  }
  employeeAbsentCost: any = 0;
  employeeAbsentId: any = 0;
  employeeAbsentCode: any = '';
  quantityProduct: any;
  formatDateToYYYYMMDD(date: string): string {
    const d = new Date(date);
    const year = d.getFullYear();
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }
  editJobDetail(job: any, job_id: number): void {
    console.log('Job:', job);
    this.employeeAbsentCode = job.code;
    this.JOBID = job_id;
    this.isLoadding = true;
    this.user_name = job.user_name;
    this.quantityProduct = job.quantity;
    console.log('quantityProduct:', this.quantityProduct);
    this.jobService.getJobDetailById(job_id).subscribe(
      (data) => {
        console.log('Job detail api:', data.result);
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
            finish: this.formatDateToYYYYMMDD(data.result.timeFinish),
            start: this.formatDateToYYYYMMDD(data.result.timeStart),
            cost: data.result.cost
          });
          console.log('editJob form:', this.editJob.value);
          console.log('Job Cost:', this.editJob.value.cost);
          this.employeeAbsentCost = this.editJob.value.cost;
          this.employeeAbsentId = job.user_id;
          console.log("employeeAbsentCost: ", this.employeeAbsentCost);
          console.log("employeeAbsentId: ", this.employeeAbsentId);
          this.isLoadding = false;
        } else {
          //   console.error('Failed to fetch job detail:', data);
          //   this.toastr.error('Không thể lấy thông tin công việc!', 'Lỗi');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error fetching job detail:', error);
        //  this.toastr.error('Có lỗi xảy ra khi lấy thông tin công việc!', 'Lỗi');
        this.isLoadding = false;
      }
    );
  }

  errorJob(product: any): void {
    this.selectedProduct = { ...product };
    console.log("Sản phẩm được chọn để báo cáo lỗi:", this.selectedProduct);

  }
  quantityProductDone: any;
  costEmplopyee: any;
  changeEmployeeAbsent(): void {
    this.isLoadding = true;
    console.log("employeeAbsentId: ", this.employeeAbsentId);
    console.log("employeeAbsentCost: ", this.employeeAbsentCost);
    console.log("jobID: ", this.JOBID);
    console.log("quantityProductDone: ", this.quantityProductDone);
    console.log("costEmplopyee: ", this.costEmplopyee);
    if (this.quantityProductDone == null) {
      this.toastr.info('Vui lòng nhập số lượng sản phẩm nhân viên đã hoàn thành!', 'Thông báo');
      this.isLoadding = false;
      return;
    }
    if (this.quantityProductDone == null) {
      this.toastr.info('Vui lòng nhập lương nhân viên nhận được!', 'Thông báo');
      this.isLoadding = false;
      return;
    }
    if (this.quantityProductDone <= 0) {
      this.toastr.info('Số lượng sản phẩm nhân viên đã hoàn thành phải lớn hơn 0!', 'Thông báo');
      this.isLoadding = false;
      return;
    }
    if (this.costEmplopyee <= 0) {
      this.toastr.info('Lương phải lớn hơn!', 'Thông báo');
      this.isLoadding = false;
      return;
    }
    if (this.costEmplopyee > this.employeeAbsentCost) {
      this.toastr.info('Lương được nhận không thể lớn hơn tổng công khoán !', 'Thông báo');
      this.isLoadding = false;
      return;
    }

    if (this.quantityProductDone > this.quantityProduct) {
      this.toastr.info('Số lượng sản phẩm nhân viên đã hoàn thành không thể lớn hơn số lượng sản phẩm cần làm!', 'Lỗi');
      this.isLoadding = false;
      return;
    }
    this.jobService.getEmployeeSick(this.employeeAbsentId, this.JOBID, this.costEmplopyee, this.quantityProductDone).subscribe(
      (data) => {
        if (data.code === 1000) {
          console.log('Change employee absent success:', data.result);
          this.toastr.success('Thay đổi nhân viên nghỉ thành công!', 'Thành công');
          this.onSearch(this.selectedCategory, this.searchKey);
          this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.info('Thay đổi nhân viên nghỉ thất bại!', 'Thông báo');
          this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.info('Có lỗi xảy ra!', 'Thông báo');
        this.isLoadding = false;
        $('[data-dismiss="modal"]').click();
      }
    );
  }
  saveChanges2(): void {
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
  saveChanges(): void {
    // this.isLoadding = true;
     console.log('Form Values:', this.errorForm.value);
    const errorFormData = this.editJob.value;
    if (!errorFormData.description || errorFormData.cost == null || !errorFormData.start || !errorFormData.finish) {
      this.isLoadding = false;
      this.toastr.info('Vui lòng điền đầy đủ thông tin, không được để trống!', 'Lỗi');
      return;
    }
    if (errorFormData.description.length < 3) {
      this.isLoadding = false;
      this.toastr.info('Mô tả phải lớn hơn 3 ký tự!', 'Lỗi');
      return;
    }



    if (errorFormData.cost <= 0) {
      this.isLoadding = false;
      this.toastr.info('Chi phí phải lớn hơn 0!', 'Thông báo');
      return;
    }
    const startDate = new Date(errorFormData.start);
    const finishDate = new Date(errorFormData.finish);
    const today = new Date();
    if (startDate < today) {
      this.isLoadding = false;
      this.toastr.info('Ngày nhận công việc không thể là ngày trước ngày hôm nay!', 'Thông báo');
      return;
    }

    // Kiểm tra nếu finish bé hơn start
    if (finishDate < startDate) {
      this.isLoadding = false;
      this.toastr.info('Ngày yêu cầu hoàn thành không thể bé hơn ngày nhận công việc!', 'Thông báo');
      return;
    }
    const jobid = this.JOBID;
    //  console.log('JOBID: ', this.JOBID)
    console.log('error edit form saveChanges:', errorFormData);
    this.jobService.editJob(jobid, errorFormData).subscribe(
      (response) => {
        if (response.code === 1000) {
          this.toastr.success('Cập nhật thông tin sản xuất thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click();
          this.isLoadding = false;
          if (this.selectedCategory === 0) {
            this.loadProductRQForJob();
          } else {
            this.loadProduct();
          }
        } else {
          console.error('Failed to edit product:', response);
          this.toastr.error('Không thể cập nhật thông tin sản xuất!', 'Thông báo');
          this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        }
      },
      (error) => {
        console.error('Error editing product:', error);
        //    this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); this.isLoadding = false;
      }
    );

  }

  createErrorJob() {
    // this.isLoadding = true;

    const jobId = this.selectedProduct.job_id; // Hoặc lấy từ giá trị khác nếu cần
    //  console.log('Job ID:', jobId);

    const formValues = this.errorForm.value;
    console.log('Form create job error report:', formValues);
    if (!formValues.description || formValues.description.length < 3) {
      this.isLoadding = false;
      this.toastr.info('Mô tả phải lớn hơn 3 ký tự!', 'Thông báo');
      return;
    }
    if (!formValues.description || !formValues.solution || formValues.quantity == null) {
      this.isLoadding = false;
      this.toastr.info('Vui lòng điền đầy đủ thông tin, không được để trống!', 'Thông báo');
      return;
    }
    if (!formValues.solution || formValues.solution.length < 3) {
      this.isLoadding = false;
      this.toastr.info('Giải pháp phải lớn hơn 3 ký tự!', 'Thông báo');
      return;
    }

    if (formValues.quantity <= 0) {
      this.isLoadding = false;
      this.toastr.info('Số lượng phải lớn hơn 0!', 'Thông báo');
      return;
    }

    console.log('Form create job error report:', formValues);
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

            console.log('Thêm lỗi sản phẩm thành công:', this.selectedCategory);
            this.toastr.success('Thêm lỗi sản phẩm thành công!', 'Thành công');
            if (this.selectedCategory === 1)

              this.loadProduct();
          }
          if (this.selectedCategory === 0) {
            this.loadProductRQForJob();
          }
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;


        },
        (error: HttpErrorResponse) => {
          console.error('Error details:', error);
          if (error.error) {
            //  console.error('Error code:', error.error.code);
            //  console.error('Error message:', error.error.message);
            this.toastr.info(error.error.message, 'Thông báo'); this.isLoadding = false;
          }

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
          this.toastr.info('Không thể lấy danh sách vật liệu sản phẩm!', 'Thông báo');
        }
      },
      error => {
        console.error("Error fetching sub-material data:", error);
      }
    );
  }
  checkNotFound: boolean = false;
  selectedPositionId: any = '';

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
    console.log('Category:', selectedCategory);
    this.isLoadding = true;
    if (selectedCategory === 1) {
      this.jobService.multiSearchJob(searchKey, this.selectedStatusJob, this.selectedPosSearch).subscribe(
        (data) => {
          console.log('Data:', data);
          if (data.code === 1000) {
            this.productRQs = data.result;
            this.currentPage = 1;
            console.log('Danh sách sản phẩm search:', this.productRQs);
            this.checkJobsForErrors();
          } else if (data.code === 1015) {
            //     console.log('Danh sách sản phẩm search:', this.productRQs);
            this.checkNotFound = true;
            this.productRQs = [];
            //    console.log("checkNotFound job:", this.checkNotFound);
          }

          this.isLoadding = false;
          this.checkNotFound = true;
        },
        (error) => {
          console.log('Error fetching products multiSearchJob:', error);

          this.isLoadding = false;
          this.checkNotFound = true;

        }
      );

    } else if (selectedCategory === 0) {

      this.jobService.multiSearchJobRequest(searchKey, this.selectedStatusJob, this.selectedPosSearch).subscribe(
        (data) => {
          if (data.code === 1000) {
            this.productRQs = data.result;
            this.checkJobsForErrors();
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


    }
  }

  // onSearchInput(cate: number,searchKey: any){
  //   console.log("Selected cate: ", cate)
  //   console.log("Search key: ", searchKey)
  // }
  checkEmployeeList: boolean = false;
  loadPosition3(product: any) {
    this.selectedProduct = { ...product };
    this.type = this.selectedProduct.statusJob.type;
    let positon = this.selectedProduct.position_id;

    const statusId = this.selectedProduct.statusJob?.status_id;
    //  console.log('Status ID:', this.selectedProduct.statusJob?.status_id);
    positon += 1;
   // console.log('posion api:', positon);
    this.jobService.GetPosition3(positon).subscribe(
      (data) => {
        this.positionEmployees = data.result;
        console.log('manageJob employeeList load position 3:', this.positionEmployees);
       
      },
      (error) => {
        console.error('Error fetching:', error);
      }
    );
  }
  loadPositionManageJob2(product: any) {
    this.selectedProduct = { ...product };
    this.type = this.selectedProduct.statusJob.type;
    let positon = this.selectedProduct.position_id;

    const statusId = this.selectedProduct.statusJob?.status_id;
    //  console.log('Status ID:', this.selectedProduct.statusJob?.status_id);

    positon += 1;
    console.log('posion api:', positon);
    this.jobService.GetPosition3(positon).subscribe(
      (data) => {
        this.positionEmployees = data.result;
        console.log('manageJob2 employeeList:', this.positionEmployees);

      },
      (error) => {
        console.error('Error fetching:', error);
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


  isEditing: boolean = false;
  editProfile(): void {
    this.isEditing = true;
  }
  cancelChanges(): void {
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
  error_job_id2: any;
  errorDetailModal: any;
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
  editProduct2(errorid: number) {
    this.isLoadding = true;
    //console.log('other modal:', errorid);
    this.errorProductService.getRrrorDetailById(errorid)
      .subscribe((response: any) => {

        if (response.code === 1000) {
          const errorEdit = response.result;

          console.log('Get Error2 Edit:', errorEdit);
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
            isFixed: errorEdit.fix,
            quantity: errorEdit.quantity
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
            isFixed: this.selectedError.isFixed,
            quantity: this.selectedError.quantity
          });
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', response); this.isLoadding = false;
          // this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }

      })
    //  console.log('Form Values after patchValue:', this.errorForm.value);
  }
  editProduct(errorid: number, product: any) {
    this.errorId = errorid;
   // console.log('report product function:', errorid);
    console.log('error detail:', product);
    this.errorDetailModal = product;
    this.error_job_id = product.product_error_id;
    this.jobService.getAllProductErrorsByJobId(errorid).subscribe(
      (response) => {
        this.jobErrors = response.result;
       console.log('error report list: ', this.jobErrors);
        console.log('error report detail: ', this.editForm.value);
        if (this.jobErrors.length > 0) {
          this.editForm.patchValue(this.jobErrors[this.jobErrors.length - 1]);
        }
        //console.log("error history: ", this.editForm.value);
      },
      (error) => {
        console.error('Error fetching product errors:', error);
      }
    );
  }
  errorHistoryjob(jobId: number) {
    this.jobService.getAllProductErrorsByJobId(jobId).subscribe(
      (response) => {
        this.errorHistory = response.result;
      //  console.log('error history: ', this.errorHistory);
      },
      (error) => {
        console.error('Error fetching product errors:', error);
      }
    );
  }
  saveChangesError() {
    this.isLoadding = true; 
    console.log("error current: ", this.editForm.value);
    const current_error_id = this.editForm.get('id')?.value;
    console.log("Current Error ID:", current_error_id);
    const errorFormData = {
      description: this.editForm.value.des,
      solution: this.editForm.value.solution,
      isFixed: this.editForm.value.fix,
      quantity: this.editForm.value.quantity
    };
    // console.log('error edit form saveChanges:', errorFormData);
    if (!errorFormData.description || !errorFormData.solution || errorFormData.quantity == null) {
      this.isLoadding = false;
      this.toastr.info('Vui lòng điền đầy đủ thông tin, không được để trống!', 'Thông báo');
      return;
    }

    // Kiểm tra các điều kiện cụ thể
    if (errorFormData.description.length < 3) {
      this.isLoadding = false;
      this.toastr.info('Mô tả phải lớn hơn 3 ký tự!', 'Thông báo');
      return;
    }

    if (errorFormData.solution.length < 3) {
      this.isLoadding = false;
      this.toastr.info('Giải pháp phải lớn hơn 3 ký tự!', 'Thông báo');
      return;
    }

    if (errorFormData.quantity <= 0) {
      this.isLoadding = false;
      this.toastr.info('Số lượng phải lớn hơn 0!', 'Thông báo');
      return;
    }

    console.log('form api EditProductError', errorFormData);
    console.log('error_job_id: ', this.error_job_id)
    this.errorProductService.editProductError(current_error_id, errorFormData).subscribe(
      (response) => {
        if (response.code === 1000) {
          this.toastr.success('Sửa lỗi sản phẩm thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click(); this.isLoadding = false;
       //   console.log("after edit error, selectedCategory: ", this.selectedCategory);
          if (this.selectedCategory === 1) {
            
            this.loadProduct();
          } else if (this.selectedCategory === 0) {
            this.loadProductRQForJob();
          }
          
        } else {
          console.error('Failed to edit product:', response);
          this.toastr.info('Không thể sửa sản phẩm!', 'Thông báo'); this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        }
      },
      (error) => {
        console.error('Error editing product:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Thông báo'); this.isLoadding = false;
      }
    );
    this.editForm.reset();
  }
  cancelChangeEmployee(): void {
    this.quantityProductDone = '';
  }
  assignedJob() {
    this.isCancel = true;
    if (this.selectedEmployee2 === '') {

    }
    this.isLoadding = true;
    console.log('Selected Employee:', this.selectedEmployee2);
    //console.log('Selected Product:', this.selectedProduct);
    const user_id = this.selectedEmployee2;
    const p_id = this.selectedProduct.product_id; // Thay đổi giá trị tùy theo sản phẩm
    let status_id = this.selectedProduct.statusJob?.status_id; // Thay đổi giá trị tùy theo trạng thái
    const job_id = this.selectedProduct.job_id; // Thay đổi giá trị tùy theo công việc
    const type_id = this.selectedCategory; // cho sp có sẵn     0 - k có sẵn
    let position_id = this.selectedProduct.position_id + 1;
    this.positionCreateJob = this.selectedProduct.position_id + 1;
    let modifiedPositionName = '';
    console.log('selected cosse:', this.selectedJob.cost);
    const costJob = this.selectedJob.cost

    // if (!user_id || !p_id || !status_id || !job_id || !type_id || !position_id) {
    //   // Nếu bất kỳ trường nào là null hoặc không được cung cấp
    //   this.toastr.error('Vui lòng nhập đầy đủ thông tin trước khi tiếp tục.', 'Lỗi');
    //   // Có thể thêm logic để dừng tiến trình nếu cần
    //   return;
    // }
    this.createJobs.patchValue({
      cost: costJob
    });
    console.log('createJobs before API call:', this.createJobs.value);
    const quantity = this.selectedProduct.quantity;
    const cost = this.createJobs.get('cost')?.value;
    const start = this.createJobs.get('start')?.value;
    const finish = this.createJobs.get('finish')?.value;
    const description = this.createJobs.get('description')?.value;
    const today = new Date();
    if (!user_id) {
      this.toastr.info('Vui lòng chọn nhân viên', 'Thông báo'); this.isLoadding = false;
      return;
    }
    if (!quantity || quantity <= 0) {
      this.toastr.info('Số lượng sản phẩm phải lớn hơn 0.', 'Thông báo');
      this.isLoadding = false;
      return;
    }

    if (!cost || cost <= 0) {
      this.toastr.info('Giá công khoán phải lớn hơn 0.', 'Thông báo'); this.isLoadding = false;
      return;
    }
    if (!start) {
      this.toastr.info('Không được để trống ngày yêu cầu nhận công việc.', 'Thông báo'); this.isLoadding = false;
      return;
    }
    if (!finish) {
      this.toastr.info('Không được để trống ngày yêu cầu hoàn thành công việc.', 'Thông báo'); this.isLoadding = false;
      return;
    }
    if (start < today) {
      this.toastr.info('Ngày nhận việc phải chọn sau hôm nay.', 'Thông báo'); this.isLoadding = false;
      return;
    }

    if (finish < start) {
      this.toastr.info('Ngày yêu cầu hoàn thành công việc phải sau ngày yêu cầu nhận việc.', 'Thông báo'); this.isLoadding = false;
      return;
    }

    if (!description || description.length < 3) {
      this.toastr.info('Mô tả không được để trống hoặc ít hơn 3 ký tự.', 'Thông báo'); this.isLoadding = false;
      return;
    }
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




    console.log('Modified Position Name:', modifiedPositionName);
    const createJobs = {
      quantity_product: quantity,
      job_name: modifiedPositionName,

    };
    console.log('createJobs before API call:', this.createJobs.value);

    console.log('cost :', this.createJobs.value.cost);
    if (this.selectedProduct.code == null) {
      //    this.isLoadding = true
      //  console.log("consolo lod: ", this.isLoadding)
      console.log("create form:", createJobs);
      console.log('API parameters:', user_id, p_id, status_id, job_id, type_id, createJobs);
      this.jobService.createExportMaterialProductTotalJob(p_id, position_id, user_id, createJobs).subscribe(
        (data) => {
          if (data.code === 1000) {
            console.log('Xuất nguyên liệu thành công');
            this.jobService.addJob(user_id, p_id, status_id, job_id, type_id, this.createJobs.value).subscribe(
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
                this.toastr.warning(error.error.message, 'Thông báo'); this.isLoadding = false;
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
              this.toastr.warning(`${key}: ${value}`, 'Thông báo');
              $('[data-dismiss="modal"]').click(); this.isLoadding = false;
            });

          } else if (error && error.message) {
            this.toastr.warning(`Có lỗi xảy ra: ${error.message}`, 'Thông báo'); this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          } else {
            this.toastr.error(`Có lỗi xảy ra, vui lòng thử lại.`, 'Thông báo'); this.isLoadding = false;
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

                  $('[data-dismiss="modal"]').click(); this.isLoadding = false;

                  this.loadProductRQForJob();
                } else {
                  console.error('Failed to fetch products:', data);
                  this.toastr.info('Thêm sản phẩm sản xuất thất bại!', 'Thông báo');
                  $('[data-dismiss="modal"]').click(); this.isLoadding = false;
                  this.loadProductRQForJob();
                }
              },
              (error) => {
                // console.error('Error fetching products:', error);
                this.toastr.info('Có lỗi xảy ra!', 'Lỗi'); this.isLoadding = false;

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
              this.toastr.warning(`${key}: ${value}`, 'Thông báo');
              $('[data-dismiss="modal"]').click(); this.isLoadding = false;
            });

          } else if (error && error.message) {
            this.toastr.warning(`Có lỗi xảy ra: ${error.message}`, 'Thông báo'); this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          } else {
            this.toastr.error(`Có lỗi xảy ra, vui lòng thử lại.`, 'Thông báo'); this.isLoadding = false;
            $('[data-dismiss="modal"]').click();
          }
        }
      );
    }

  }
  manageJob2(product: any): void {
    this.selectedEmployee2 = ''
    this.employeeSelect2.nativeElement.value = '';
    this.isLoadding = true;

    this.selectedProduct = { ...product };

    console.log('job 2:', this.selectedProduct);
    this.jobId = this.selectedProduct.job_id;
    this.jobService.getJobDetailById(this.jobId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.selectedJob = data.result;
          console.log('Form detailJob2 value:', this.selectedJob);
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.info('Không thể lấy danh sách sản phẩm!', 'Lỗi');
          this.isLoadding = false;
        }
      },
    );
    //   console.log("Sản phẩm được chọn để giao việc:", this.selectedProduct);
    //   console.log("thiis.type_id: ", this.selectedProduct.code);
    // console.log("Trạng thái của sản phẩm:", this.selectedProduct.statusJob?.status_id);
    this.createJobs.reset();
    //console.log("employee:", this.selectedProduct.user_name);
    console.log("status_id:", this.selectedProduct.statusJob?.status_id);
    if (this.selectedProduct.statusJob?.status_id === 4) {
      this.getPositionEmpAbsent(1);
     
    }
    if (this.selectedProduct.statusJob?.status_id === 7) {
      this.getPositionEmpAbsent(2);
    }
    if (this.selectedProduct.statusJob?.status_id === 10) {
      this.getPositionEmpAbsent(3);
    }

    // Sử dụng regex để thay thế chữ "Thợ" bằng chữ "Làm"
    let modifiedPositionName = '';
    if (this.selectedProduct.position_id === 0) {
      modifiedPositionName = 'Làm mộc';
    }
    if (this.selectedProduct.position_id === 1) {
      modifiedPositionName = 'Làm nhám';
    }
    if (this.selectedProduct.position_id === 2) {
      modifiedPositionName = 'Phun sơn';
    }
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
  positionEmpLength: number = 0;
  getPositionEmpAbsent(type_id: number): void {
    this.jobService.GetPosition3(type_id).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.positionEmployees = data.result;
          console.log('Position employee absent 2:', this.positionEmployees);
         this.positionEmpLength = this.positionEmployees.length;
        } else {
          console.error('Failed to fetch positions 2:', data);
        }
      },
      (error) => {
        console.error('Error fetching positions 2:', error);
      }
    );
  }
}