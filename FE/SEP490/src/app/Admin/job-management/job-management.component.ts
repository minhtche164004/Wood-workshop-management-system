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
  selectedCategory: number = 0;
  selectedProduct: any = {}; // Biến để lưu trữ sản phẩm được chọn
  position1Employees: any[] = [];
  position2Employees: any[] = [];
  position3Employees: any[] = [];
  constructor(private jobService: JobService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.loadProductRQForJob();
    this.loadPosition1();
    this.loadPosition2();
    this.loadPosition3();
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
          this.products = data.result;
          console.log('Sp cho job:', this.products);
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
    if (this.selectedProduct) {
      // Kiểm tra giá trị của type và gọi các hàm tương ứng
      switch (this.selectedProduct.statusJob.type) {
        case 4:
          this.loadPosition3();
          break;
        case 3:
          this.loadPosition2();
          break;
        case 2:
          this.loadPosition1();
          break;
        default:
          break;
      }
    }
  

  }
  positon3(){
      this.jobService.GetPosition3();
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

  getButtonClass(product: any): string {
    if (product.type === 2) {
      switch (product.statusId) {
        case 7:
        case 8:
        case 9:
          return 'btn-warning';  // Màu vàng
        case 10:
          return 'btn-success';  // Màu xanh lá
        case 12:
        case 13:
        case 14:
          return 'btn-primary';  // Màu xanh
        case 15:
          return 'btn-info';     // Màu xanh dương
        default:
          return 'btn-secondary'; // Mặc định màu xám
      }
    }
    return 'btn-secondary'; // Mặc định màu xám nếu không thỏa điều kiện
  }

  onSearch(): void {
    console.log('Thực hiện tìm kiếm: ', this.searchKey);
    console.log('Category: ', this.selectedCategory);
    if (this.selectedCategory == 1) {
      this.loadProductRQForJob();
    } else if (this.selectedCategory == 2) {
      this.loadProduct();
    }
  }

  loadPosition1() {
    this.jobService.GetPosition1().subscribe(
      (data) => {
        this.position1Employees = data.result;
        console.log('Position 1 data:', this.position1Employees);
      },
      (error) => {
        console.error('Error fetching Position 1 data:', error);
      }
    );
  }

  loadPosition2() {
    this.jobService.GetPosition2().subscribe(
      (data) => {
        this.position2Employees = data.result;
        console.log('Position 2 data:', this.position2Employees);
      },
      (error) => {
        console.error('Error fetching Position 2 data:', error);
      }
    );
  }

  loadPosition3() {
    this.jobService.GetPosition3().subscribe(
      (data) => {
        this.position3Employees = data.result;
        this.position3Employees.forEach(category => {
          console.log(`Category ID: ${category.userId}, Category Name: ${category.username}`);
        });
        console.log('Position 3 data:', this.position3Employees);
      },
      (error) => {
        console.error('Error fetching Position 3 data:', error);
      }
    );
  }
}

