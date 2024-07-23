import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthenListService } from 'src/app/service/authen.service';
import { OrderService } from 'src/app/service/order.service';
interface ApiResponse {
  code: number;
  result: any[];
}
@Component({
  selector: 'app-history-order',
  templateUrl: './history-order.component.html',
  styleUrls: ['./history-order.component.scss']
})
export class HistoryOrderComponent {
  history_order: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  userStatus: any[] = [];
  orderData: any = {};
  isLoadding: boolean = false;
  selectedCategory: string = '';
  searchKey: string = '';
  selectedSDate: string = '';
  selectedEDate: string = '';
  constructor(private toastr: ToastrService,private authenListService: AuthenListService,private orderService: OrderService) { }
  ngOnInit(): void {
    this.orderData = {
      order_detail_id: '',
      product_id: '',
      product_name: '',
      request_product_id: '',
      request_product_name: '',
      price: '',
      quantity: '',

    };

    this.getHistoryOrder();
    this.getOrderStatus();

  }
  filterStatus(): void {
    console.log(this.selectedCategory);

    this.isLoadding = true;
    let startDate: string = this.selectedSDate || '';
    let endDate: string = this.selectedEDate || '';
    if (this.selectedSDate) {
      startDate = this.selectedSDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }

    if (this.selectedEDate) {
      endDate = this.selectedEDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }
    console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory,
      "DateS:", startDate, "DateE:", endDate
    );
    this.authenListService.getFilterStatus(
      this.searchKey,
      this.selectedCategory,
      startDate,
      endDate
    )
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.currentPage = 1;
            this.history_order = data.result;
            this.isLoadding = false;

          } else if (data.code === 1015) {
            this.history_order = [];
            this.isLoadding = false;
            this.toastr.warning(data.message);
          }
        },
        (error: HttpErrorResponse) => {
          this.isLoadding = false;
          this.toastr.error('Có lỗi xảy ra, vui lòng thử lại sau');


        }
      );
  }
  getOrderStatus(): void {
    this.orderService.getOrderStatus().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.userStatus = data.result;
            console.log('Danh sách status don hang', this.userStatus);
        } else {
          console.error('Dữ liệu trả về không hợp lệ:', data);
        }
      },
      (error) => {
        console.error('Lỗi khi lấy danh sách người dùng:', error);
      }
    );
  }
  getHistoryOrder(): void {
    this.loginToken = localStorage.getItem('loginToken');
    this.isLoadding = true;
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getHistoryOrderCustomer().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.history_order = data.result;
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


  historyOrderDetail(orderId: string): void {
    this.isLoadding = true;
    this.authenListService.getOrderDetailById(orderId).subscribe(
      (data) => {
        this.orderData = data.result;
        console.log("Order data:", this.orderData.price)
        this.isLoadding = false;


      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    )
  }
}
