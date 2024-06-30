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
  productRQs: any[] = [];
  currentPage: number = 1;
  searchKey: string = '';
  selectedEmployee: number = 0;
  selectedCategory: number = 0;
  selectedProduct: any = {}; // Biến để lưu trữ sản phẩm được chọn
  positionEmployees: any[] = [];
  type : any = {}
  
  constructor(private jobService: JobService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.loadProductRQForJob();
    this.createNewJob();
  }
  createNewJob() {
    const user_id = 1; // Thay đổi giá trị tùy theo người dùng
    const p_id = 1; // Thay đổi giá trị tùy theo sản phẩm
    const status_id = 1; // Thay đổi giá trị tùy theo trạng thái
    const job_id = 1; // Thay đổi giá trị tùy theo công việc
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
    this.loadProductRQForJob();
    this.selectedProduct = { ...product }; // Lưu trữ dữ liệu sản phẩm vào biến selectedProduct
    console.log("Sản phẩm được chọn để giao việc:", this.selectedProduct);
    console.log("Type:", this.selectedProduct.statusJob.type);
    if (this.selectedProduct) {
      // Kiểm tra giá trị của type và gọi các hàm tương ứng
      
          this.loadPosition3();
         
    }
  

  }

  assignJob(): void {
    console.log('Giao việc cho sản phẩm:', this.selectedProduct);

    // Thực hiện gọi API hoặc các hành động khác để lưu thông tin công việc
    // this.jobService.assignJob(this.selectedProduct).subscribe(
    //   (response) => {
    //     console.log('Kết quả giao việc:', response);
    //     this.toastr.success('Giao việc thành công!', 'Thành công');
    //     // Bạn có thể thực hiện thêm các hành động khác như làm mới danh sách sản phẩm
    //   },
    //   (error) => {
    //     console.error('Error assigning job:', error);
    //     this.toastr.error('Giao việc thất bại!', 'Lỗi');
    //   }
    // );
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