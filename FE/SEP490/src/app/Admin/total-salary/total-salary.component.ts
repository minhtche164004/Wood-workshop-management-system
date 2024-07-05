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
@Component({
  selector: 'app-total-salary',
  templateUrl: './total-salary.component.html',
  styleUrls: ['./total-salary.component.scss']
})
export class TotalSalaryComponent implements OnInit{
  
  totalSalary: any[]= [];
  currentPage: number = 1;
  constructor(private http: HttpClient, private toastr: ToastrService, private jobService: JobService, private fb: FormBuilder, private productListService: ProductListService, private sanitizer: DomSanitizer, private productService: ProductService, private salaryService: SalaryService) { }
  
  
  ngOnInit(): void {
    this.getTotalSalary();
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
}
