import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenListService } from 'src/app/service/authen.service';
interface ApiResponse {
  code: number;
  result: any[];
}
@Component({
  selector: 'app-list-product-error-employee',
  templateUrl: './list-product-error-employee.component.html',
  styleUrls: ['./list-product-error-employee.component.scss']
})

export class ListProductErrorEmployeeComponent {
  isLoadding: boolean = false;
  loginToken: string | null = null;
  product_error: any[] = [];
  searchKey: string = '';
  currentPage: number = 1;
  selectProductError: number = -1;
  constructor(

    private authenListService: AuthenListService,
    private fb: FormBuilder,
    private http: HttpClient,
    private toastr: ToastrService,
    private router: Router) { }
  ngOnInit(): void {



    this.loadAllProductErrorByEmployeID();


  }
  loadAllProductErrorByEmployeID() {
    this.isLoadding = true;
    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getProductErrorByID().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.product_error = data.result;
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
  MultifilterProductError(): void{


    this.isLoadding = true;

    this.authenListService.getFilterProductError(
      this.searchKey.trim(),
      this.selectProductError,



    )
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.currentPage = 1;
            this.product_error = data.result;
            console.log(this.product_error);
            this.isLoadding = false;

          } else if (data.code === 1015) {
            this.product_error = [];
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
