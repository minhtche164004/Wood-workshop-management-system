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
import { StatisticService } from 'src/app/service/statistic.service';

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
  constructor(
    private dataService: DataService, 
    private statistic: StatisticService, 
    private http: HttpClient, 
    private toastr: ToastrService, 
    private employeeService: EmployeeService, 
    private jobService: JobService, 
    private fb: FormBuilder, 
    private productListService: ProductListService, 
    private sanitizer: DomSanitizer, 
    private productService: ProductService, 
    private salaryService: SalaryService
  ) { }

  chart: any = [];
  positionLabels = ['Thợ Mộc', 'Thợ Nhám', 'Thợ Sơn'];
  countEmp: number = 0;
  totalAmouneOrderHaveDone: number = 0;
  totalOrder: number = 0;
  totalSpecialOrder: number = 0;
  percentSpecialOrder: number = 0;
  percentOrder: number = 0;
  totalProduct: number = 0;
  percentEmpPos1: number = 0;
  percentEmpPos2: number = 0;
  percentEmpPos3: number = 0;
  percentEmpPos4: number = 0;
  isLoading: boolean = false;
  months = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];
  data: any[] = []; // Array to store data for each month
  chartData: any;
  ngOnInit(): void {
    this.loadData();
  }
  getJobByMonthAndYear() {
  }
  loadData() {
    this.isLoading = true; // Hiển thị loading
    this.getTotalOrder().then(() => {
      Promise.all([
        this.TotalAmountOrderHaveDone(),
        this.getTotalAmountSubMaterial(),
        this.getTotalSpecialOrder(),
        this.getTotalProduct(),
        this.getAllEmployee()
      ]).then(() => {
        this.updateEmployeePositions().then(() => {
          this.initializeCharts();
          this.isLoading = false; // Ẩn loading
        }).catch(err => {
          this.isLoading = false;
          console.error(err);
        });
      }).catch(err => {
        this.isLoading = false;
        console.error(err);
      });
    }).catch(err => {
      this.isLoading = false;
      console.error(err);
    });
    
  }

  initializeCharts() {
    this.chart = new Chart('canvas2', {
      type: 'pie',
      data: {
        labels: this.positionLabels,
        datasets: [
          {
            label: 'Nhân viên',
            data: [this.totalEmpPos1, this.totalEmpPos2, this.totalEmpPos3],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
              'rgb(255, 205, 86)',
             
            ],
            hoverOffset: 4
          }
        ]
      },
    });

    this.chart = new Chart('canvas3', {
      type: 'pie',
      data: {
        labels: ['Đơn hàng thường', 'Đơn hàng đặc biệt'],
        datasets: [
          {
            label: 'Đơn hàng',
            data: [this.totalNormalOrder, this.totalSpecialOrder],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
            ],
            hoverOffset: 4
          }
        ]
      },
    });
    this.chart = new Chart('canvas5', {
      type: 'pie',
      data: {
        labels: ['Sản phẩm có sẵn', 'Sản phẩm theo yêu cầu'],
        datasets: [
          {
            label: 'Sản phẩm',
            data: [this.totalNormalOrder, this.totalSpecialOrder],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
            ],
            hoverOffset: 4
          }
        ]
      },
    });

    this.chart = new Chart('canvas', {
      type: 'bar',
      data: {
        labels: this.months,
        datasets: [
          {
            label: 'Sản phẩm có sẵn',
            data: [12, 19, 3, 5, 2, 3, 4, 2, 3, 1, 3, 2],
            backgroundColor: 'rgb(54, 162, 235)',
          },
          {
            label: 'Sản phẩm đặt riêng',
            data: [5, 3, 13, 8, 11, 9, 14, 6, 10, 12, 14, 7],
            backgroundColor: 'rgb(255, 99, 132)',
          },
        ]
      },
    });

    this.chart = new Chart('canvas4', {
      type: 'line',
      data: {
        labels: this.months,
        datasets: [{
          label: 'Đơn hàng sản phẩm có sẵn',
          data: [12, 19, 3, 5, 10, 12, 8, 5, 9, 10, 12, 10],
          fill: false,
          borderColor: 'rgb(75, 192, 192)',
          backgroundColor: 'rgb(75, 192, 192)',
          tension: 0.1
        },
        {
          label: 'Đơn đặt hàng sản phẩm theo yêu cầu',
          data: [5, 3, 13, 8, 11, 9, 14, 6, 10, 12, 14, 7],
          fill: false,
          borderColor: 'rgb(255, 99, 132)',
          backgroundColor: 'rgb(255, 99, 132)',
          tension: 0.1
        },    
      ]},
        //stepSixe = 5
    //   options: {
    //     scales: {
    //         y: {
    //             beginAtZero: true, // Bắt đầu từ 0
    //             ticks: {
    //                 stepSize: 5 // Đặt bước nhảy là 5
    //             }
    //         }
    //     }
    // }
    });

  }

  TotalAmountOrderHaveDone() {
    return new Promise<void>((resolve, reject) => {
      this.statistic.getTotalAmountOrderHaveDone().subscribe((data) => {
        this.totalAmouneOrderHaveDone = data.result;
        resolve();
      }, err => reject(err));
    });
  }

  getAllEmployee() {
    return new Promise<void>((resolve, reject) => {
      this.employeeService.getAllEmployee().subscribe((data) => {
        this.countEmp = data.result.length;
        resolve();
      }, err => reject(err));
    });
  }
  totalAmountSubMaterial: number = 0;
  getTotalAmountSubMaterial() {
    return new Promise<void>((resolve, reject) => {
      this.statistic.getTotalAmountSubMaterial().subscribe((data) => {
        console.log("totalAmountSubMaterial: ", data.result);
        this.totalAmountSubMaterial = data.result;
        resolve();
      }, err => reject(err));
    });
  }

  getTotalOrder() {
    return new Promise<void>((resolve, reject) => {
      this.statistic.getTotalOrder().subscribe((data) => {
        this.totalOrder = data.result;
        console.log("totalOrder: ", this.totalOrder);
        resolve();
      }, err => reject(err));
    });
  }

  getTotalSpecialOrder() {
    return new Promise<void>((resolve, reject) => {
      this.statistic.getCountSpecialOrder().subscribe((data) => {
        this.totalSpecialOrder = data.result;
        console.log("totalSpecialOrder", this.totalSpecialOrder);
        
        if (this.totalOrder !== 0) {
          this.percentSpecialOrder = (this.totalSpecialOrder / this.totalOrder) * 100;
          this.percentOrder = 100 - this.percentSpecialOrder;
          this.totalNormalOrder = this.totalOrder - this.totalSpecialOrder;
        } else {
          this.percentSpecialOrder = 0;
          this.percentOrder = 0;
        }

        console.log("percentSpecialOrder", this.percentSpecialOrder);
        console.log("percentOrder", this.percentOrder);
        resolve();
      }, err => reject(err));
    });
  }
  totalNormalOrder: number = 0;
  
  getTotalProduct() {
    return new Promise<void>((resolve, reject) => {
      this.statistic.getCountProduct().subscribe((data) => {
        this.totalProduct = data.result;
        resolve();
      }, err => reject(err));
    });
  }
  totalEmpPos1: number = 0;
  totalEmpPos2: number = 0;
  totalEmpPos3: number = 0;
  totalEmpPos4: number = 0;
  
  updateEmployeePositions() {
    return Promise.all([
      this.statistic.countEmployeeWithTypePosition(1).toPromise(),
      this.statistic.countEmployeeWithTypePosition(2).toPromise(),
      this.statistic.countEmployeeWithTypePosition(3).toPromise(),
      this.statistic.countEmployeeWithTypePosition(4).toPromise()
    ]).then(results => {
      this.totalEmpPos1 = results[0].result;
      this.totalEmpPos2 = results[1].result ;
      this.totalEmpPos3 = results[2].result ;
      this.totalEmpPos4 = results[3].result ;
    }).catch(err => {
      console.error(err);
    });
  }

  
}
