import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { Chart } from 'chart.js';
import { ToastrService } from 'ngx-toastr';
import { DataService } from 'src/app/service/data.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { JobService } from 'src/app/service/job.service';
import { ProductService } from 'src/app/service/product.service';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { SalaryService } from 'src/app/service/salary.service';
interface UserInfo {
  inforId: number;
  phoneNumber: string;
  fullname: string;
  address: string;
  bank_name: string | null;
  bank_number: string;
  city_province: string;
  district: string;
  wards: string;
  has_Account: number;
}

interface Position {
  position_id: number;
  position_name: string;
}

interface User {
  userId: number;
  username: string;
  password: string;
  email: string;
  position: Position;
  hireDate: string;
  userInfor: UserInfo;
}
@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnInit {
  constructor(private dataService: DataService, private http: HttpClient, private toastr: ToastrService, private employeeService: EmployeeService, private jobService: JobService, private fb: FormBuilder, private productListService: ProductListService, private sanitizer: DomSanitizer, private productService: ProductService, private salaryService: SalaryService) { }
  chart: any = [];
  keyword = 'username';
  searchKey: string = '';
  totalSalary: any[] = [];
  currentPage: number = 1;
  employeeList: any[] = [];
  selectedEmp: any = {};
  positionEmpList: any[] = [];
  selectedPosition: any = '';
  fromDate: string = '';
  toDate: string = '';
  isProduct: boolean = true; // check product or product request
  isLoadding: boolean = false;
  selectedPositionEmp: any = '';
  bankList: any[] = [];
  ndChuyenKhoan: string = 'DoGoSyDung thanh toan tien cong (Ma: {{code}})'
  qrImageUrl: string = '';
  startDate: string = '';
  endDate: string = '';
  position: number = 0;
  selectedBanking: any;
  ngOnInit(): void {
    this.getAllEmployee();
    this.getAllJobRQ(); 
    this.loadProduct();
    this.chart = new Chart('canvas', {
      type: 'line',
      data: {
        labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
        datasets: [
          {
            label: 'My First dataset',
            data: [12, 19, 3, 5, 2, 3],
            borderColor: '#3cba9f',

          },
          {
            label: 'My second dataset',
            data: [2, 3, 5, 2, 3, 12],
            borderColor: '#ffcc00',

          }
        ]
      },
    });

    this.chart = new Chart('canvas3', {
      type: 'bar',
      data: {
        labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
        datasets: [
          {
            label: 'My First dataset',
            data: [12, 19, 3, 5, 2, 3],
            borderColor: '#3cba9f',

          },
          {
            label: 'My second dataset',
            data: [2, 3, 5, 2, 3, 12],
            borderColor: '#ffcc00',

          }
        ]
      },
    });
    this.chart = new Chart('canvas2', {
      type: 'pie',
      data: {
        labels: ['Red', 'Blue', 'Yellow' ],
        datasets: [
          {
            label: 'My First dataset',
            data: [23,43,34],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
              'rgb(255, 205, 86)'
            ],
            hoverOffset: 4
          }
       
        ]
      },

    });

  }
  countEmp: number = 0;
  jobProductRQs: any[] = [];
  countJobProductRQs: number = 0;
  jobProducts: any[] = [];
  countJobProducts: number = 0;
  getAllJobRQ(): void {
    this.jobService.getListProductRQ().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.jobProductRQs = data.result;
          this.countJobProductRQs = this.jobProductRQs.length;
          console.log('Sp đặc biệt cho job:', this.jobProductRQs);
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
          this.jobProducts = data.result;
          this.countJobProducts = this.jobProducts.length;
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
  getAllEmployee(): void {
    this.isLoadding = true;
    this.employeeService.getAllEmployee().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.employeeList = data.result;
          this.countEmp = this.employeeList.length;
          console.log('Tong nhan vien: ', this.countEmp); this.isLoadding = false;
      //    console.log('Danh sách nhan vien: ', this.employeeList); this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data); this.isLoadding = false;
        }

      },
      (error) => {
        console.log(error); this.isLoadding = false;
      }
    );
  }
  
}

