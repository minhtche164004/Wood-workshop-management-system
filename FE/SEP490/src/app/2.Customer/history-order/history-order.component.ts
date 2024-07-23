import { Component } from '@angular/core';
import { AuthenListService } from 'src/app/service/authen.service';
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
  orderData: any = {};
  isLoadding: boolean = false;
  constructor(private authenListService: AuthenListService) { }
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
