import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { JobService } from 'src/app/service/job.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { CalendarModule } from 'primeng/calendar';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ProductService } from 'src/app/service/product.service';
import { SalaryService } from 'src/app/service/salary.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { EmailService } from 'src/app/service/email.service';
@Component({
  selector: 'app-total-salary',
  templateUrl: './total-salary.component.html',
  styleUrls: ['./total-salary.component.scss']
})
export class TotalSalaryComponent implements OnInit{
   keyword = 'username';

  totalSalary: any[]= [];
  currentPage: number = 1;
  employeeList: any[] = [];
  selectedEmp: any = {};
  positionEmpList: any[] = [];  
  selectedPosition: any = 0;

  selectedPositionEmp: any = 0;
  constructor(private http: HttpClient, private toastr: ToastrService, private employeeService: EmployeeService,private jobService: JobService, private fb: FormBuilder, private productListService: ProductListService, private sanitizer: DomSanitizer, private productService: ProductService, private salaryService: SalaryService) { }
  
  
  ngOnInit(): void {
    this.getTotalSalary();
    this.getAllEmployee();
    this.getAllPostionEmp();
  }
  getTotalSalary() {
    this.salaryService.getSalary().subscribe(   
        (data) => {
          if (data.code === 1000) {
            this.totalSalary = data.result;
            console.log('Danh sách luong nhan vien', this.totalSalary);
          } else {
            console.error('Failed to fetch products:', data);
          }
        
      },
      (error) => {
        console.log(error);
      }
    );
  }
  getAllPostionEmp(): void {
    this.employeeService.getAllPostionEmp().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.positionEmpList = data.result;
          console.log('Danh sách chuc vu nhan vien: ', this.positionEmpList);
        } else {
          console.error('Failed to fetch products:', data);
        }
      
    },
    )
  }
  getAllEmployee(): void {
    this.employeeService.getAllEmployee().subscribe(   
      (data) => {
        if (data.code === 1000) {
          this.employeeList = data.result;
          console.log('Danh sách nhan vien: ', this.employeeList);
        } else {
          console.error('Failed to fetch products:', data);
        }
      
    },
    (error) => {
      console.log(error);
    }
  );
  }
  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
  }
  selectEvent(item: any) {
      
  }
  selectEmp(product: any): void {
    this.selectedEmp = product; // Adjust based on your product object structure
    console.log('Selected emp:', this.selectedEmp);
    console.log('Selected emp id:', this.selectedEmp.userId);
    this.countJobByUserId(this.selectedEmp.userId);
  }
  onChangeSearch(event: any) {
    this.selectedEmp = event.target.value;
    console.log('Selected salary emp:', event.target.value);
  }

  onFocused(e: any) {
  }
  onSearchPosition(selectedPosition: any){
    this.selectedPosition = selectedPosition;
    console.log('Selected position:', this.selectedPosition);
    this.countJobByUserId(this.selectedPosition);
  }
  countJobByUserId(id: number): void {
    this.employeeService.countJobByUserId(id).subscribe(
      (data) => {
        if (data.code === 1000) {
          console.log('Count job by user id:', data.result);
        } else {
          console.error('Failed to fetch products:', data);
        }
      
    },
    (error) => {
      console.log(error);
    }
  );
  }
}
