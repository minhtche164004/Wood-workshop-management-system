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
  selector: 'app-list-job-done-employee',
  templateUrl: './list-job-done-employee.component.html',
  styleUrls: ['./list-job-done-employee.component.scss']
})

export class ListJobDoneEmployeeComponent implements OnInit {
  job_employee: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  isLoadding: boolean = false; 
  constructor(

    private authenListService: AuthenListService,
    private fb: FormBuilder,
    private http: HttpClient,
    private toastr: ToastrService,
    private router: Router) { }
  ngOnInit(): void {


    this.loginToken = localStorage.getItem('loginToken');
    this.loadAllJobByEmployeID();
 

  }
  loadAllJobByEmployeID() {
    this.isLoadding = true;
    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getfindAllJobForDoneByEmployeeID().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.job_employee = data.result;
            this.isLoadding = false;
            console.log('Danh sách đơn hàng:', data);
          } else {
            console.error('Failed to fetch products:', data);
            this.isLoadding = false;
          }
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.isLoadding = false;
        }
      );
    } else {
      console.error('No loginToken found in localStorage.');
      this.isLoadding = false;

    }
  }
}
