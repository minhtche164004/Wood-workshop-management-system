import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { Chart } from 'chart.js';
import * as e from 'cors';
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
  chart2: any = [];
  chart3: any = [];
  chart4: any = [];
  chart5: any = [];
  chart10: any = [];
  emmpChart: any = [];
  positionLabels = [];
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
 
  totalRQProduct: number = 0;
  months = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];
  data: any[] = []; // Array to store data for each month
  chartData: any;
  ngOnInit(): void {
    this.loadData();
    
  }

  getAllPostionEmp(): void {
    this.employeeService.getAllPostionEmp().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.positionLabels = data.result.map((item: any) => item.position_name);
     //     console.log('Danh sách chức vụ nhân viên: ', this.positionLabels);
        } else {
          console.error('Failed to fetch products:', data);
          // Handle error as needed
        }
      },
      (error) => {
        console.error('Error fetching employee positions:', error);
        // Handle error as needed
      }
    );
    
  }
  loadData() {
    this.isLoading = true; // Hiển thị loading
    this.getTotalOrder().then(() => {
      Promise.all([
        
        this.getTotalAmountSubMaterial(),
        this.getTotalSpecialOrder(),
        this.getTotalProduct(),
        this.getAllEmployee(),
        this.getAllDataForYear(),
        this.getAllPostionEmp(),
        // this.countProductByMonthAndYear(),

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
    if (this.emmpChart instanceof Chart) {
      this.emmpChart.destroy();
    }
  
    this.emmpChart = new Chart("canvas1", {
      type: 'pie',
      data: {
        labels: this.positionLabels,
        datasets: [
          {
            label: 'Nhân viên',
            data: [this.totalEmpPos1, this.totalEmpPos2, this.totalEmpPos3, this.totalEmpPos4],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
              'rgb(255, 205, 86)',
              'rgb(255, 100, 16)',
            ],
            hoverOffset: 4
          }
        ]
      },
    });
    this.emmpChart.update();
    this.chart2 = new Chart('canvas2', {
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
    this.chart3 = new Chart('canvas3', {
      type: 'pie',
      data: {
        labels: ['Sản phẩm có sẵn', 'Sản phẩm theo yêu cầu'],
        datasets: [
          {
            label: 'Sản phẩm',
            data: [this.totalProduct, this.totalRQProduct],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
            ],
            hoverOffset: 2
          }
        ]
      },
    });

    this.chart4 = new Chart('canvas4', {
      type: 'bar',
      data: {
        labels: this.months,
        datasets: [
          {
            label: 'Sản phẩm có sẵn',
            data: this.productCounts,
            backgroundColor: 'rgb(54, 162, 235)',
          },
          {
            label: 'Sản phẩm theo yêu cầu',
            data: this.productRequestCounts,
            backgroundColor: 'rgb(255, 99, 132)',
          },
        ]
      },
    });

    this.chart5 = new Chart('canvas5', {
      type: 'line',
      data: {
        labels: this.months,
        datasets: [{
          label: 'Đơn hàng sản phẩm có sẵn',
          data: this.totalOrderMY,
          fill: false,
          borderColor: 'rgb(75, 192, 192)',
          backgroundColor: 'rgb(75, 192, 192)',
          tension: 0.1
        },
        {
          label: 'Đơn đặt hàng sản phẩm theo yêu cầu',
          data: this.totalOrderSpecMY,
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
  getCountRQProduct() {
    return new Promise<void>((resolve, reject) => {
      this.statistic.getCountRQProduct().subscribe((data) => {
        this.totalRQProduct = data.result;
   //     console.log("totalRQProduct: ", this.totalRQProduct);
        resolve();
      }, err => reject(err));
    });
  }
  
  totalOrderCoSan: number = 0;

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
     //   console.log("totalAmountSubMaterial: ", data.result);
        this.totalAmountSubMaterial = data.result;
        resolve();
      }, err => reject(err));
    });
  }
;
  getTotalOrder() {
    return new Promise<void>((resolve, reject) => {
      this.statistic.getTotalOrder().subscribe((data) => {
        this.totalOrder = data.result;
    //    console.log("totalOrder: ", this.totalOrder);
        resolve();
      }, err => reject(err));
    });
  }

  getTotalSpecialOrder() {
    return new Promise<void>((resolve, reject) => {
      this.statistic.getCountSpecialOrder().subscribe((data) => {
        this.totalSpecialOrder = data.result;
       // console.log("totalSpecialOrder", this.totalSpecialOrder);
        
        if (this.totalOrder !== 0) {
          this.percentSpecialOrder = (this.totalSpecialOrder / this.totalOrder) * 100;
          this.percentOrder = 100 - this.percentSpecialOrder;
          this.totalNormalOrder = this.totalOrder - this.totalSpecialOrder;
          this.totalOrderCoSan = this.totalOrder - this.totalSpecialOrder;
          console.log("total Order: ", this.totalOrder)
        } else {
          this.percentSpecialOrder = 0;
          this.percentOrder = 0;
        }

        // console.log("percentSpecialOrder", this.percentSpecialOrder);
        // console.log("percentOrder", this.percentOrder);
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
      this.statistic.getCountRQProduct().subscribe((data) => {
        this.totalRQProduct = data.result;
     //   console.log("totalRQProduct: ", this.totalRQProduct);
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
      // console.log('totalEmpPos1:', this.totalEmpPos1);
      // console.log('totalEmpPos2:', this.totalEmpPos2);
      // console.log('totalEmpPos3:', this.totalEmpPos3);
      // console.log('totalEmpPos4:', this.totalEmpPos4);
    }).catch(err => {
      console.error(err);
    });
  }
  productCounts: number[] = [];
  productRequestCounts: number[] = [];
 
  getProductRequestByMonthAndYear(month: number, year: number): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.statistic.countProductRequestByMonthYear(13, month, year).subscribe((data) => {
        if (data.result === null || data.result === undefined) {
          data.result = 0;
        }
      //  console.log(`getProductRequestByMonthAndYear result for ${month}/${year}: `, data.result);
        this.productRequestCounts.push(data.result);
        resolve();
      }, err => {
     //   console.error(`getProductRequestByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
    });
  }

  getProductByMonthAndYear(month: number, year: number): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.statistic.countCompletedProductOnOrderByMonthAndYear(month, year).subscribe((data) => {
        if (data.result === null || data.result === undefined) {
          data.result = 0;
        }
     //   console.log(`getProductByMonthAndYear result for ${month}/${year}: `, data.result);
        this.productCounts.push(data.result);
        resolve();
      }, err => {
      //  console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
      this.statistic.countCompletedRequestProductOnOrderByMonthAndYear(month, year).subscribe((data) => {
        if (data.result === null || data.result === undefined) {
          data.result = 0;
        }
   //     console.log(`getProductByMonthAndYear result for ${month}/${year}: `, data.result);
        this.productRequestCounts.push(data.result);
        resolve();
      }, err => {
      //  console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
    }
   
  );

  }
  // countProductByMonthAndYear() {
  //   const year = new Date().getFullYear(); //Lấy năm hiện tại
  //   const promises = [];
  //   for (let month = 1; month <= 12; month++) {
  //     promises.push(this.getProductByMonthAndYear(month, year));
      
  //   }
  //   Promise.all(promises).then(() => {
  //     // console.log('All data retrieved successfully.');
  //      console.log('Product Counts:', this.productCounts);
  //      console.log('Product Request Counts:', this.productRequestCounts);
  //     this.initializeCharts(); // Call to initialize charts with the fetched data
  //   }).catch(err => {
  //     console.error('Error retrieving data:', err);
  //   });
  // }
  year: number = new Date().getFullYear();
  getAllDataForYear() {
  
    const promises = [];
    for (let month = 1; month <= 12; month++) {
      promises.push(this.getProductByMonthAndYear(month, this.year));
     
      promises.push(this.countTotalOrder(month, this.year));
      promises.push(this.countTotalSpecOrder(month, this.year));
    }
    Promise.all(promises).then(() => {
      // console.log('All data retrieved successfully.');
      // console.log('Product Counts:', this.productCounts);
      // console.log('Product Request Counts:', this.productRequestCounts);
    //  console.log('Total Order:', this.totalOrderMY);
    //  console.log('Total Special Order:', this.totalOrderSpecMY);
      this.initializeCharts(); // Call to initialize charts with the fetched data
    }).catch(err => {
      console.error('Error retrieving data:', err);
    });
  }
  totalOrderMY: any[] = [];
  totalOrderSpecMY: any[] = [];
  countTotalOrder(month: number, year: number): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.statistic.countTotalOrderByMonthAndYear(month, year).subscribe((data) => {
        // if (data.result === null || data.result === undefined) {
        //   data.result = 0;
        // }
      //  console.log(`total order ${month}/${year}: `, data.result);
        this.totalOrderMY.push(data.result);
        resolve();
      }, err => {
      //  console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
    });
  }
  countTotalSpecOrder(month: number, year: number): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.statistic.countTotalSpecOrderByMonthAndYear(month, year).subscribe((data) => {
        // if (data.result === null || data.result === undefined) {
        //   data.result = 0;
        // }
      //  console.log(`total spec order result for ${month}/${year}: `, data.result);
        this.totalOrderSpecMY.push(data.result);
        resolve();
      }, err => {
      //  console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
    });
  }
  
}
