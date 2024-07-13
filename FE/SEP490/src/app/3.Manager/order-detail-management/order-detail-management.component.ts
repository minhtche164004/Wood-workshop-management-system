import { HttpClient } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { OrderService } from 'src/app/service/order.service';
import { ToastrService } from 'ngx-toastr';
import { FormControl } from '@angular/forms';
@Component({
  selector: 'app-order-detail-management',
  templateUrl: './order-detail-management.component.html',
  styleUrls: ['./order-detail-management.component.scss']
})
export class OrderDetailManagementComponent implements OnInit{
  constructor(private orderDetailService: OrderService, private toastr: ToastrService) { }

  orderDetails: any[] = [];
  currentPage: number = 1 ;
  searchKey: string = '';
  ngOnInit(): void {
   this.loadAllOrderDetails();
  }
 
  loadAllOrderDetails(): void{
    this.orderDetailService.getAllOrderDetails().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.orderDetails = data.result;
          console.log('Danh sách order details: :', this.orderDetails);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách nhà cung cấp!', 'Lỗi'); // Display error toast
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Display generic error toast
      }
    );
  }
  viewProductDetails(orderId: number): void {
    console.log('View product details with orderId:', orderId);
    // Add your logic here to handle viewing product details
    // For example, you might navigate to a details page or open a modal
  }
}
