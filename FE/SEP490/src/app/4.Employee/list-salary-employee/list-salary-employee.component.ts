import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenListService } from 'src/app/service/authen.service';
interface ApiResponse {
  code: number;
  result: any[];
}
@Component({
  selector: 'app-list-salary-employee',
  templateUrl: './list-salary-employee.component.html',
  styleUrls: ['./list-salary-employee.component.scss']
})
export class ListSalaryEmployeeComponent implements OnInit {
  salary_employee: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  isLoadding: boolean = false; 
  selectedSDate: string = '';
  selectedEDate: string = '';
  selectPayment: number = -1;
  searchKey: string = '';
  constructor(

    private authenListService: AuthenListService,
    private fb: FormBuilder,
    private http: HttpClient,
    private toastr: ToastrService,
    private router: Router) { }
  ngOnInit(): void {


    this.loginToken = localStorage.getItem('loginToken');
    this.loadAllSalaryByEmployeID();
 

  }
  loadAllSalaryByEmployeID() {
    this.isLoadding = true;
    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
  
      this.authenListService.getSalaryByEmployeeID().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.isLoadding = false;
            this.salary_employee = data.result.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
          
            console.log('Retrieved loginToken:',this.salary_employee);
  
          } else {
            console.error('Failed to fetch salary data:', data);
            this.isLoadding = false;
          }
        },
        (error) => {
          console.error('Error fetching salary data:', error);
          this.isLoadding = false;
        }
      );
    } else {
      console.error('No loginToken found in localStorage.');
      this.isLoadding = false;
    }
  }
  MultifilterSalaryEmployee(): void{


    this.isLoadding = true;
    let startDate: string = this.selectedSDate || '';
    let endDate: string = this.selectedEDate || '';
    if (this.selectedSDate) {
      startDate = this.selectedSDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }

    if (this.selectedEDate) {
      endDate = this.selectedEDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }

    this.authenListService.getFilterSalaryEmployeeByID(
      this.searchKey.trim(),
      this.selectPayment,
      startDate,
      endDate


    )
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.currentPage = 1;
            this.salary_employee = data.result;
            console.log(this.salary_employee);
            this.isLoadding = false;

          } else if (data.code === 1015) {
            this.salary_employee = [];
            this.isLoadding = false;
            // this.toastr.warning(data.message);
          }
        },
        (error: HttpErrorResponse) => {
          this.isLoadding = false;
          this.toastr.error('Có lỗi xảy ra, vui lòng thử lại sau');


        }
      );
  }  
}


