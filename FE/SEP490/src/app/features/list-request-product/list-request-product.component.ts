import { Component } from '@angular/core';
import { AuthenListService } from 'src/app/service/authen.service';
interface ApiResponse {
  code: number;
  result: any[];
}
@Component({
  selector: 'app-list-request-product',
  templateUrl: './list-request-product.component.html',
  styleUrls: ['./list-request-product.component.scss']
})
export class ListRequestProductComponent {
  loginToken: string | null = null;
  list_request_product: any[] = [];
  currentPage: number = 1;
  constructor(private authenListService: AuthenListService) { }
  ngOnInit(): void {
   
    this.getHistoryOrder();
    
  }
  getHistoryOrder(): void {
    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getListRequestProductCusomer().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.list_request_product = data.result;
            console.log('Danh sách sản phẩm đã đặt:', data);
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

}
