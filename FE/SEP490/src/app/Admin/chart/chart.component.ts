import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { Chart, registerables } from 'chart.js';
import { ToastrService } from 'ngx-toastr';
import { DataService } from 'src/app/service/data.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { JobService } from 'src/app/service/job.service';
import { ProductService } from 'src/app/service/product.service';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { SalaryService } from 'src/app/service/salary.service';
import { StatisticService } from 'src/app/service/statistic.service';
import ChartDataLabels from 'chartjs-plugin-datalabels';

Chart.register(ChartDataLabels);
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
  chart6: any = [];
  chart10: any = [];
  emmpChart: any = [];
  positionLabels: string[] = [];
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
  year: number = new Date().getFullYear();
  productCounts: number[] = [];
  productRequestCounts: number[] = [];
  totalOrderMY: number[] = [];
  totalOrderSpecMY: number[] = [];
  totalNormalOrder: number = 0;
  totalAmountSubMaterial: number = 0;
  totalEmployee: number = 0; 
  totalEmpPos1: number = 0;
  totalEmpPos2: number = 0;
  totalEmpPos3: number = 0;
  totalEmpPos4: number = 0;

  ngOnInit(): void {
    this.loadData();
  }
  ngOnDestroy() {
    // Destroy existing charts if they exist
    if (this.emmpChart instanceof Chart) {
      this.emmpChart.destroy();
    }
    if (this.chart2 instanceof Chart) {
      this.chart2.destroy();
    }
    if (this.chart3 instanceof Chart) {
      this.chart3.destroy();
    }
    if (this.chart4 instanceof Chart) {
      this.chart4.destroy();
    }
    if (this.chart5 instanceof Chart) {
      this.chart5.destroy();
    }
    if (this.chart6 instanceof Chart) {
      this.chart6.destroy();
    }
  }
   async loadData() {
     this.isLoading = true; // Hiển thị loading

    try {
      await this.getTotalOrder();
      await Promise.all([
         this.getTotalAmountSubMaterial(),
         this.getTotalSpecialOrder(),
         this.percenProduct(),
        this.getAllEmployee(),
        this.getTotalProductNormal(),
        this.getAllPostionEmp(),
        this.getAllDataForYear(),
        this.getTotalAmountHaveDone()
      ]);
      await this.updateEmployeePositions();
      this.initializeCharts();
    } catch (err) {
      this.isLoading = false;
      console.error(err);
    } finally {
      this.isLoading = false;
    }
  }

  async getAllPostionEmp(): Promise<void> {
    try {
      const data = await this.employeeService.getAllPostionEmp().toPromise();
      if (data.code === 1000) {
        this.positionLabels = data.result.map((item: any) => item.position_name);
      } else {
        console.error('Failed to fetch positions:', data);
      }
    } catch (error) {
      console.error('Error fetching employee positions:', error);
    }
  }
  async getTotalAmountHaveDone(): Promise<void> {
    try {
      const data = await this.statistic.getTotalAmountOrderHaveDone().toPromise();
      this.totalAmouneOrderHaveDone = data.result;
      console.log('Doanh thu:', this.totalAmouneOrderHaveDone);
    } catch (err) {
      console.error(err);
    }
  }
  initializeCharts() {
  
    this.emmpChart = new Chart("canvas1", {
      type: 'pie',
      data: {
        labels: this.positionLabels,
        datasets: [
          {
            label: 'Nhân viên',
            data: [this.percentEmpPos1, this.percentEmpPos2, this.percentEmpPos3],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
              'rgb(255, 205, 86)',
            ],
            hoverOffset: 3
          },
        ]
      },
      options: { //cau hin`h hien? thi chart
        plugins: {
          datalabels: {
            color: '#fff',
            font: {
              weight: 'bold',
              size: 10
            },
            formatter: (value) => {
              return value + '%';
            }
          }
        }
      }
    });
  
    this.chart2 = new Chart('canvas2', {
      type: 'pie',
      data: {
        labels: ['Đơn hàng có sẵn', 'Đơn hàng đặc biệt'],
        datasets: [
          {
            label: 'Đơn hàng',
            data: [this.percentSpecialOrder, this.percentOrder],
            backgroundColor: [
              'rgb(255, 99, 132)',
              'rgb(54, 162, 235)',
            ],
            hoverOffset: 2
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
            data: [this.normalProductPercentage, this.rqProductPercentage],
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
        }] 
      }
    });

    this.chart6 = new Chart('canvas6', {
      type: 'line',
      data: {
        labels: this.months,
        datasets: [{
          label: 'Tổng giá vật liệu đã bán',
          data: this.totalInput,
          fill: false,
          borderColor: 'rgb(75, 192, 192)',
          backgroundColor: 'rgb(75, 192, 192)',
          tension: 0.1
        },
        {
          label: 'Tổng lương',
          data: this.totalSalary,
          fill: false,
          borderColor: 'rgb(255, 99, 132)',
          backgroundColor: 'rgb(255, 99, 132)',
          tension: 0.1
        }] 
      }
    });
  }

  async getTotalOrder(): Promise<void> {
    try {
      const data = await this.statistic.getTotalOrder().toPromise();
      this.totalOrder = data.result;
    } catch (err) {
      console.error(err);
    }
  }

  async getTotalAmountSubMaterial(): Promise<void> {
    try {
      const data = await this.statistic.getTotalAmountSubMaterial().toPromise();
      this.totalAmountSubMaterial = data.result;
    } catch (err) {
      console.error(err);
    }
  }

  async getTotalSpecialOrder(): Promise<void> {
    try {
      const data = await this.statistic.getCountSpecialOrder().toPromise();
      this.totalSpecialOrder = data.result;
      if (this.totalOrder > 0) {
        this.percentSpecialOrder = Math.round((this.totalSpecialOrder / this.totalOrder) * 100);
        this.percentOrder = Math.round(100 - this.percentSpecialOrder);
      //  console.log('Phần trăm đơn hàng đặc biệt:', this.percentSpecialOrder.toFixed(2) + '%');
      //  console.log('Phần trăm đơn hàng có sẵn:', this.percentOrder.toFixed(2) + '%');

      }
    } catch (err) {
      console.error(err);
    }
  }

  async getTotalProduct(): Promise<void> {
    try {
      const data = await this.statistic.getCountProduct().toPromise();
      this.totalNormalProduct = data.result;
     // console.log('Tổng sản phẩm thường:', this.totalNormalProduct);
      await this.getTotalRQProduct(); // Wait for getTotalRQProduct() to complete
      await this.getTotalProductNormal(); // Wait for getTotalProductNormal() to complete
    } catch (err) {
      console.error(err);
    }
  }
  
  async getTotalRQProduct(): Promise<void> {
    try {
      const data = await this.statistic.getCountRQProduct().toPromise();
      this.totalRQProduct = data.result;
    } catch (err) {
      console.error(err);
    }
  }
  totalNormalProduct: number = 0;
  normalProductPercentage: number = 0;
  rqProductPercentage: number = 0;
  async getTotalProductNormal(): Promise<void> {
    try {
      this.totalProduct = this.totalNormalProduct + this.totalRQProduct;
     // console.log('Tổng sản phẩm normal:', this.totalNormalProduct);
    //  console.log('Tổng sản phẩm RQ:', this.totalRQProduct);
     // console.log('Tổng sản phẩm:', this.totalProduct);
      this.normalProductPercentage = Math.round((this.totalNormalProduct / this.totalProduct) * 100);
      this.rqProductPercentage = Math.round((this.totalRQProduct / this.totalProduct) * 100);
     // console.log('Phần trăm sản phẩm normal:', this.normalProductPercentage.toFixed(2) + '%');
    //  console.log('Phần trăm sản phẩm RQ:', this.rqProductPercentage.toFixed(2) + '%');
    } catch (err) {
      console.error(err);
    }
  }
  async getAllEmployee(): Promise<void> {
    try {
      
      const data = await this.employeeService.getAllEmployee().toPromise();
      this.countEmp = data.result.length;
    } catch (err) {
      console.error(err);
    }
  }
   numberProduct: number = 0;
  async percenProduct(): Promise<void> {
    try {
      this.getTotalProduct();
      this.getTotalRQProduct();
      this.numberProduct = this.totalProduct + this.totalRQProduct;
     // console.log('Tổng sản phẩm:', this.numberProduct);
     } catch (err) {
      console.error(err);
    }
  }
  async updateEmployeePositions(): Promise<void> {
    try {
     // console.log('Tinh phan tram nhan vien');
      const data = await this.employeeService.getAllEmployee().toPromise();
      const employees = data.result;
      this.totalEmployee = employees.length;
      this.totalEmpPos1 = employees.filter((emp: User) => emp.position.position_id === 1).length;
      this.totalEmpPos2 = employees.filter((emp: User) => emp.position.position_id === 2).length;
      this.totalEmpPos3 = employees.filter((emp: User) => emp.position.position_id === 3).length;
      this.totalEmpPos4 = employees.filter((emp: User) => emp.position.position_id === 4).length;

      if (this.totalEmployee > 0) {
        this.percentEmpPos1 = Math.round((this.totalEmpPos1 / this.totalEmployee) * 100);
        this.percentEmpPos2 = Math.round((this.totalEmpPos2 / this.totalEmployee) * 100);
        this.percentEmpPos3 = Math.round((this.totalEmpPos3 / this.totalEmployee) * 100);
        this.percentEmpPos4 = Math.round((this.totalEmpPos4 / this.totalEmployee) * 100);
      }
   //   console.log('Phần trăm nhân viên vị trí 1:', this.percentEmpPos1.toFixed(2) + '%');
    //  console.log('Phần trăm nhân viên vị trí 2:', this.percentEmpPos2.toFixed(2) + '%');
    //  console.log('Phần trăm nhân viên vị trí 3:', this.percentEmpPos3.toFixed(2) + '%');
    //  console.log('Phần trăm nhân viên vị trí 4:', this.percentEmpPos4.toFixed(2) + '%');
    } catch (err) {
      console.error(err);
    }
  }

  async getProductByMonthAndYear(month: number, year: number): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.statistic.countCompletedProductOnOrderByMonthAndYear(month, year).subscribe((data) => {
        if (data.result === null || data.result === undefined) {
          data.result = 0;
        }
      console.log(`getProductByMonthAndYear result for ${month}/${year}: `, data.result);
        this.productCounts.push(data.result);
        resolve();
      }, err => {
        console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
      this.statistic.countCompletedRequestProductOnOrderByMonthAndYear(month, year).subscribe((data) => {
        if (data.result === null || data.result === undefined) {
          data.result = 0;
        }
     console.log(`getProductByMonthAndYear result for ${month}/${year}: `, data.result);
        this.productRequestCounts.push(data.result);
        resolve();
      }, err => {
        console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
    }
   
  );

  }
  async getAllDataForYear() {
  
    const promises = [];
    for (let month = 1; month <= 12; month++) {
      promises.push(this.getProductByMonthAndYear(month, this.year));
      
      promises.push(this.countTotalOrder(month, this.year));
      promises.push(this.countTotalSpecOrder(month, this.year));
      promises.push(this.findTotalInputSmt(month, this.year));
      promises.push(this.findTotalSalary(month, this.year));
    }
    Promise.all(promises).then(() => {
     // console.log('Tổng tiền luowng theo tháng:', this.totalSalary);
     // console.log('Tổng tiền vật liệu:', this.totalInput);
      // console.log('All data retrieved successfully.');
      // console.log('Product Counts:', this.productCounts);
      // console.log('Product Request Counts:', this.productRequestCounts);
    //  console.log('Total Order:', this.totalOrderMY);
    //  console.log('Total Special Order:', this.totalOrderSpecMY);
      //this.initializeCharts(); // Call to initialize charts with the fetched data
    }).catch(err => {
      console.error('Error retrieving data:', err);
    });
  }
  totalInput: number[] = []; //tính số tiền vật liệu theo tháng
  totalSalary: number[] = []; //tính số tiền lương theo tháng
  async findTotalInputSmt(month: number, year: number): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.statistic.findTotalInputSubMaterialByMonthAndYear(month, year).subscribe((data) => {
        // if (data.result === null || data.result === undefined) {
        //   data.result = 0;
        // }
      //  console.log(`total order ${month}/${year}: `, data.result);
        this.totalInput.push(data.result);
        
        resolve();
      }, err => {
      //  console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
    });
    
  }
  async findTotalSalary(month: number, year: number): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.statistic.findTotalSalaryByMonthAndYear(month, year).subscribe((data) => {
        if (data.result === null || data.result === undefined) {
          data.result = 0;
        }
      //  console.log(`total order ${month}/${year}: `, data.result);
        this.totalSalary.push(data.result);
        
        resolve();
      }, err => {
      //  console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
    });
    
  }
  async getProductRequestByMonthAndYear(month: number, year: number): Promise<void> {
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
  async countTotalOrder(month: number, year: number): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.statistic.countTotalOrderByMonthAndYear(month, year).subscribe((data) => {
        // if (data.result === null || data.result === undefined) {
        //   data.result = 0;
        // }
       // console.log(`total order ${month}/${year}: `, data.result);
        this.totalOrderMY.push(data.result);
        resolve();
      }, err => {
      //  console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
    });
  }
  async countTotalSpecOrder(month: number, year: number): Promise<void> {
    return new Promise<void>((resolve, reject) => {
      this.statistic.countTotalSpecOrderByMonthAndYear(month, year).subscribe((data) => {
        // if (data.result === null || data.result === undefined) {
        //   data.result = 0;
        // }
       // console.log(`total spec order result for ${month}/${year}: `, data.result);
        this.totalOrderSpecMY.push(data.result);
        resolve();
      }, err => {
      //  console.error(`getProductByMonthAndYear error for ${month}/${year}: `, err);
        reject(err);
      });
    });
  }
 }
