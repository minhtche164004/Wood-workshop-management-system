import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenListService } from 'src/app/service/authen.service';
import { ProductListService } from 'src/app/service/product/product-list.service';
interface ApiResponse {
  code: number;
  result: any[];
}
@Component({
  selector: 'app-list-job-done',
  templateUrl: './list-job-done.component.html',
  styleUrls: ['./list-job-done.component.scss']
})
export class ListJobDoneComponent implements OnInit {
  job_admin: any[] = [];
  position: any[] = [];
  job: any[] = [];
  selectedRoleFilter:string = '';
  selectedJob:string = '';
  searchKey: string = '';
  loginToken: string | null = null;
  currentPage: number = 1;
  isLoadding: boolean = false;
  constructor(

    private authenListService: AuthenListService,
    private fb: FormBuilder,
    private productListService: ProductListService,
    private http: HttpClient,
    private toastr: ToastrService,
    private router: Router) { }
  ngOnInit(): void {


    this.loginToken = localStorage.getItem('loginToken');
    this.loadAllJobByEmployeID();
    this.loadPosition();
    this.loadAllStatusJob();

  }
  loadPosition(): void {
    this.productListService.getAllPosition().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.position = data.result;

        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching positions:', error);
      }
    );
  }
  loadAllStatusJob(): void {
    this.productListService.getAllStatusJob().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.job = data.result;

        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching positions:', error);
      }
    );
  }
  loadAllJobByEmployeID() {
    this.isLoadding = true;
    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getListJobWasDoneAdmin().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.job_admin = data.result;
            console.log('Danh sách đơn hàng:', data);
            this.isLoadding = false;
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
  MultifiterJobAdmin(): void {

 
    this.authenListService.getFilterJobWasDone(
      this.searchKey,
     
      this.selectedJob,
      this.selectedRoleFilter,

    )
      .subscribe(
        (data) => {

          if (data.code === 1000) {
            this.currentPage = 1;
            this.job_admin = data.result;
            console.log(this.job_admin);
            this.isLoadding = false;

          } else if (data.code === 1015) {
            this.job_admin = [];
            this.isLoadding = false;
            // this.toastr.warning(data.message);
          }



        },
        (error: HttpErrorResponse) => {
          this.isLoadding = false;
          // this.toastr.error('Có lỗi xảy ra, vui lòng thử lại sau');


        }
      );

  }
}

