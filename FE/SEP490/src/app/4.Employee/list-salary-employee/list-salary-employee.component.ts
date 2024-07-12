import { HttpClient } from '@angular/common/http';
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
    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
  
      this.authenListService.getSalaryByEmployeeID().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.salary_employee = data.result.sort((a, b) => new Date(b.date).getTime() - new Date(a.date).getTime());
           
          } else {
            console.error('Failed to fetch salary data:', data);
          }
        },
        (error) => {
          console.error('Error fetching salary data:', error);
        }
      );
    } else {
      console.error('No loginToken found in localStorage.');
    }
  }
}


