import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-job-management',
  templateUrl: './job-management.component.html',
  styleUrls: ['./job-management.component.scss']
})
export class JobManagementComponent implements OnInit {
  products: any[] = [];
  keyword = 'productName';

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
  constructor(private jobService: JobService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.loadProductRQForJob();
    this.loadStatusByType();
    
  }
  createNewJob() {
    const user_id = 1; // Thay đổi giá trị tùy theo người dùng
    const p_id = 1; // Thay đổi giá trị tùy theo sản phẩm
    const status_id = 1; // Thay đổi giá trị tùy theo trạng thái
    const job_id = 1; // Thay đổi giá trị tùy theo công việc
    const type_id = 1; //cho sp có sẵn     0 - k có sẵn

    const jobData = {
      job_name: 'New Job',
      quantity_product: 10,
      description: 'Description of the job',
      finish: new Date(),
      start: new Date(),
      cost: 1000
    };

    this.jobService.addJob(user_id, p_id, status_id, job_id, jobData).subscribe(
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
    console.log("Type:", this.selectedProduct.statusJob.type);
    console.log("employee: ", this.selectedProduct.user_name)
    if (this.selectedProduct) {
      // Kiểm tra giá trị của type và gọi các hàm tương ứng
      
          this.loadPosition3();
         
    }
  

  }

  addNewJob() {
    const user_id = 3; // Replace with actual user_id
    const p_id = 3; // Replace with actual p_id
    const status_id = 3; // Replace with actual status_id
    const job_id = 3; // Replace with actual job_id
    const jobData = {
      job_name: 'string',
      quantity_product: 0,
      description: 'string',
      finish: '2024-07-02T16:11:04.300Z',
      start: '2024-07-02T16:11:04.300Z',
      cost: 0
    };

    this.jobService.addJob(user_id, p_id, status_id, job_id, jobData).subscribe(
      (response) => {
        console.log('Thêm công việc thành công:', response);
        // Xử lý logic sau khi thêm công việc thành công
      },
      (error) => {
        console.error('Lỗi khi thêm công việc:', error);
        // Xử lý lỗi khi thêm công việc
      }
    );
  }

 
  onSearch(selectedCategory: number, searchKey: string): void {
    console.log('Thực hiện tìm kiếm: ', searchKey);
    console.log('Category: ', selectedCategory);
  
    if (selectedCategory == 1) {
      this.loadProductRQForJob();
    } else if (selectedCategory == 2) {
      this.loadProduct()
    }
  }

  

  loadPosition3() {
    this.type = this.selectedProduct.statusJob.type;
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