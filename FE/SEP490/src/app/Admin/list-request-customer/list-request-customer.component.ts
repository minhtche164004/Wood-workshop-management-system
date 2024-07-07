import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenListService } from 'src/app/service/authen.service';
interface ApiResponse {
  code: number;
  result: any[];
}
interface Request {
  status_id: number;
  status_name: string;
}
interface EditUserRequest {

  status_id: number,
  response: string
}

@Component({
  selector: 'app-list-request-customer',
  templateUrl: './list-request-customer.component.html',
  styleUrls: ['./list-request-customer.component.scss']
})
export class ListRequestCustomerComponent implements OnInit {
  list_request_customer: any[] = [];
  RequestData: any = {};
  loginToken: string | null = null;
  currentPage: number = 1;
  Accept_Reject_rForm: FormGroup;
  status_request: any[] = [];
  selectedStatus: any = null;
  constructor(

    private authenListService: AuthenListService,
    private fb: FormBuilder,
    private http: HttpClient,
    private toastr: ToastrService,
    private router: Router) {
    this.Accept_Reject_rForm = this.fb.group({
      status_id: ['', Validators.required],
      response: ['']


    });
  }
  ngOnInit(): void {


    this.loginToken = localStorage.getItem('loginToken');
    this.loadAllJobByEmployeID();
    this.loadMaterials();


  }
  loadAllJobByEmployeID() {

    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getAllRequest().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.list_request_customer = data.result;
            console.log('Danh sách đơn hàng:', data);
          } else {
            console.error('Failed to fetch products:', data);
          }
        },
        (error) => {
          console.error('Error fetching products:', error);
        }
      );
    } else {
      console.error('No loginToken found in localStorage.');

    }
  }
  getDataRequest(id: string): void {
    this.authenListService.getRequestById(id).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.RequestData = data.result;
          this.selectedStatus = this.status_request.find(sa => sa.status_id === this.RequestData.status_id)?.status_id;
          console.log('Selected dataaaa: ', this.RequestData);
        } else {
          console.error('Failed to fetch supplier data:', data);
        }
      },
      (error) => {
        console.error('Error fetching supplier data:', error);
      }
    );
  }
  loadMaterials(): void {
    this.authenListService.getAllStatusRequest().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.status_request = data.result;

          console.log('Danh sách yêu cầu:', this.status_request);

        } else {
          console.error('Dữ liệu trả về không hợp lệ:', data);
        }
      },
      (error) => {
        console.error('Lỗi khi lấy danh sách Loại:', error);
      }
    );
  }

  EditRequestAccept(): void {
    const editUserRequest: EditUserRequest = this.Accept_Reject_rForm.value;
    const requestid = this.RequestData.requestId; // Lấy requestId từ requestData
    console.log("Data: ", editUserRequest);
  
    this.authenListService.EditRequest(requestid, editUserRequest).subscribe(
      () => {
        this.toastr.success('Thay đổi thông tin thành công.');
      },
      (error: any) => {
        this.toastr.error('Lỗi');
        console.error('Lỗi khi chỉnh sửa yêu cầu:', error);
      }
    );
  }
  
}


