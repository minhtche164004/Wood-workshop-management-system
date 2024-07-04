import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { JobService } from 'src/app/service/job.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import {MatDatepickerModule} from '@angular/material/datepicker';
import { CalendarModule } from 'primeng/calendar';

@Component({
  selector: 'app-job-management',
  templateUrl: './job-management.component.html',
  styleUrls: ['./job-management.component.scss']
})
export class JobManagementComponent implements OnInit {
  products: any[] = [];
  keyword = 'productName';
  createJobs: FormGroup;
  productRQs: any[] = [];
  currentPage: number = 1;
  searchKey: string = '';
  selectedEmployee: number = 0;
  selectedCategory: number = 0;
  selectedStatus: number = 0;
  selectedProduct: any = {}; // Biến để lưu trữ sản phẩm được chọn
  positionEmployees: any[] = [];
  type : any = {}
  statusType: any[] = [];
  statusOptions: any[] = [];
  
  costJob: any; // Cost of the job
  jobDescription: string = ''; // Job description
  constructor( private fb: FormBuilder,private jobService: JobService, private toastr: ToastrService) { 

    this.createJobs = this.fb.group({
      job_name: [''],
      quantity_product: this.selectedProduct.quantity,
      cost: [],
      description: [''],
      finish: [''],
      start: [''],
    });
  }

  ngOnInit(): void {
    this.loadProductRQForJob();
    this.loadStatusByType();
    
  }
  createNewJob(selectedProduct: any) {
    const user_id = this.selectedEmployee;
    // Thay đổi giá trị tùy theo người dùng
    const p_id = this.selectedProduct.product_id; // Thay đổi giá trị tùy theo sản phẩm
    const status_id = this.selectedProduct.statusJob?.status_id; // Thay đổi giá trị tùy theo trạng thái
    const job_id = this.selectedProduct.job_id; // Thay đổi giá trị tùy theo công việc
    const type_id = this.selectedCategory; //cho sp có sẵn     0 - k có sẵn
    console.log("user_id: ", user_id)
    console.log("p_id: ", p_id)
    console.log("status_id: ", status_id)
    console.log("job_id: ", job_id)
    console.log("type: ", type_id)
    const jobData = this.createJobs.value;
    
    console.log('Job data:', jobData);
    this.jobService.addJob(user_id, p_id, status_id, job_id, type_id, jobData).subscribe(
      response => {
        console.log('Job created successfully!', response);
        // Xử lý response hoặc thực hiện các hành động tiếp theo
      },
      error => {
        console.error('Error creating job:', error);
        // Xử lý lỗi
      }
    );
  }
  selectEvent(item: any) {
      
  }

  onChangeSearch(search: string) {

  }

  onFocused(e: any) {
  }
  loadStatusByType() {

    this.jobService.getStatusByType().subscribe(
      (data) => {
        if(data.code === 1000) {
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
    const selectedStatusId = this.selectedStatus; // Assuming selectedStatus is bound to ngModel
    if (selectedStatusId === 10 || selectedStatusId === 7 || selectedStatusId === 4) {
        return true; // Disable the button
    }
    return false; // Enable the button
}

  getStatusOptions(product: any) {
    console.log('Product:', product);
    this.jobService.getStatusJobByType(product.statusJob.type).subscribe(
      (data) => {
        console.log('Status Job:', data);
        this.statusOptions = data; // Store the fetched data in the component property
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
          console.log('Sp cho job:', this.productRQs);
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
  
    this.selectedProduct = { ...product }; // Lưu trữ dữ liệu sản phẩm vào biến selectedProduct
    console.log("Sản phẩm được chọn để giao việc:", this.selectedProduct);
    console.log("Trạng thái của sản phẩm:", this.selectedProduct.statusJob.status_id);
    this.selectedProduct.statusJob?.statusId;
    if (this.selectedProduct.code !== null) {
      this.selectedProduct.type_id = 0;
  } else {
      this.selectedProduct.type_id = 1;
  }
    console.log("employee: ", this.selectedProduct.user_name)
    if (this.selectedProduct) {
      // Kiểm tra giá trị của type và gọi các hàm tương ứng
      
          this.loadPosition3(product);
         
    }
  

  }


 
  onSearch(selectedCategory: number, searchKey: string): void {
    console.log('Thực hiện tìm kiếm: ', searchKey);
    console.log('Category: ', selectedCategory);
  
    if (selectedCategory == 0) {
      this.loadProductRQForJob();
    } else if (selectedCategory == 1) {
      this.loadProduct()
    }
  }

  

  loadPosition3(product: any) {
    this.selectedProduct = { ...product };
    this.type = this.selectedProduct.statusJob.type;
    console.log('Type truoc khi cong:', this.type);
    // Increase type by 1 if statusId is 6, 9, or 12
    const statusId = this.selectedProduct.statusJob?.status_id;
    console.log('Status ID:', this.selectedProduct.statusJob?.status_id);
    if (statusId === 6 || statusId === 9 || statusId === 12) {
      this.type += 1;
    }
  
    console.log('Type:', this.type);
    this.jobService.GetPosition3(this.type).subscribe(
      (data) => {
        this.positionEmployees = data.result;
        this.positionEmployees.forEach(category => {
          console.log(`Category ID: ${category.userId}, Category Name: ${category.username}`);
        });
        console.log('Position 3 data:', this.positionEmployees);
      },
      (error) => {
        console.error('Error fetching Position 3 data:', error);
      }
    );
  }
  
}