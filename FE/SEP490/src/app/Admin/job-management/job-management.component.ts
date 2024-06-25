import { Component, HostListener, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OrderRequestService } from 'src/app/service/order-request.service';
import { ToastrService } from 'ngx-toastr';
import { FormControl } from '@angular/forms';
import { JobService } from 'src/app/service/job.service';

@Component({
  selector: 'app-job-management',
  templateUrl: './job-management.component.html',
  styleUrls: ['./job-management.component.scss']
})
export class JobManagementComponent implements OnInit{
  
  constructor(private jobService: JobService,private toastr: ToastrService) { }
  products: any[] = [];
  productRQs: any[] = [];
  currentPage: number = 1;
  searchKey: string = '';
  selectedCategory: number = 0;
  ngOnInit(): void {
    this.loadProductRQForJob();
    this.loadPosition1();
    this.loadPosition2();
    this.loadPosition3();
  }
  loadProductRQForJob(){
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

  loadProduct(){
    this.jobService.getListProduct().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.productRQs = data.result;
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
  viewProductDetails(orderId: number): void {
  

    
  }
  manageJob(product_id: number): void{
    console.log("tao viec lam theo id: ", product_id)
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
    console.log('Thực hiện tìm kiếm: ', this.searchKey)
    console.log('Category: ', this.selectedCategory)
    if(this.selectedCategory == 1){
      this.loadProductRQForJob();
    } else if(this.selectedCategory == 2){
      this.loadProduct();
    }
   
  }
  loadPosition1() {
    this.jobService.GetPosition1().subscribe(
      (data) => {
        console.log('Position 1 data:', data);
      },
      (error) => {
        console.error('Error fetching Position 1 data:', error);
      }
    );
  }

  loadPosition2() {
    this.jobService.GetPosition2().subscribe(
      (data) => {
        console.log('Position 2 data:', data);
      },
      (error) => {
        console.error('Error fetching Position 2 data:', error);
      }
    );
  }

  loadPosition3() {
    this.jobService.GetPosition3().subscribe(
      (data) => {
        console.log('Position 3 data:', data);
      },
      (error) => {
        console.error('Error fetching Position 3 data:', error);
      }
    );
  }

}
