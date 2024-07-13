import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AuthenListService } from 'src/app/service/authen.service';
import { OrderRequestService } from 'src/app/service/order-request.service';
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
  orderRqDetails: any;
  isLoadding: boolean = false;
  constructor(

    private authenListService: AuthenListService,
    private orderRequestService: OrderRequestService,
    private fb: FormBuilder,
    private http: HttpClient,
    private toastr: ToastrService,
    private router: Router) {
    this.Accept_Reject_rForm = this.fb.group({
      status_id: ['', Validators.required],
      response: [''],
      fullname: [''],
      code: ['']


    });
  }
  ngOnInit(): void {


    this.loginToken = localStorage.getItem('loginToken');
    this.loadAllJobByEmployeID();
    this.loadMaterials();


  }
  loadAllJobByEmployeID() {
    this.isLoadding = true;
    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getAllRequest().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.list_request_customer = data.result;
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
  getDataRequest(id: string): void {
    this.isLoadding = true;
    this.authenListService.getRequestById(id).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.RequestData = data.result;
          this.selectedStatus = this.status_request.find(sa => sa.status_id === this.RequestData.status_id)?.status_id;
          this.isLoadding = false;

          console.log('Selected dataaaa: ', this.RequestData);
        } else {
          console.error('Failed to fetch supplier data:', data);
          this.isLoadding = false;

        }
      },
      (error) => {
        console.error('Error fetching supplier data:', error);
        this.isLoadding = false;

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
    this.isLoadding = true;

    const editUserRequest: EditUserRequest = this.Accept_Reject_rForm.value;
    const requestid = this.RequestData.request_id; // Lấy requestId từ requestData
    console.log("Data: ", editUserRequest);

    this.authenListService.EditRequest(requestid, editUserRequest).subscribe(
      () => {
        this.toastr.success('Thay đổi thông tin thành công.');
        this.isLoadding = false;

      },
      (error: any) => {
        this.toastr.error('Lỗi');
        console.error('Lỗi khi chỉnh sửa yêu cầu:', error);
        this.isLoadding = false;

      }
    );
  }
  onStatusChange() {
    if (this.selectedStatus !== 3) {
      this.RequestData.response = '';
    }
  }

  viewProductDetails(orderId: number): void {

    this.isLoadding = true;

    this.orderRequestService.getRequestById(orderId).subscribe(
      (data) => {
        if (data.code === 1000) {
          if (data.result && typeof data.result === 'object') {
            this.orderRqDetails = data.result;
            this.orderRqDetails = Array.isArray(data.result) ? data.result : [data.result];
            this.isLoadding = false;

          } else {
            this.orderRqDetails = [];
            console.warn('Unexpected data format for products:', data.result);
            this.isLoadding = false;

          }
          console.log('Product List:', this.orderRqDetails);
        } else {
          console.error('Failed to fetch products:', data);
    this.isLoadding = false;

          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
    this.isLoadding = false;

        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );

  }

}


