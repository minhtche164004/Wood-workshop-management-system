import { Component, HostListener } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { OrderRequestService } from 'src/app/service/order-request.service';
import { ToastrService } from 'ngx-toastr';
import { FormControl } from '@angular/forms';
interface OrderRequest {
  user_id: number;
  status_id: number;
  requestDate: string;
  response: string;
  description: string;
  phoneNumber: string;
  fullname: string;
  address: string;
  city_province: string;
  district: string;
  wards: string;
  imagesList: Image[];
  code: string | null;
}
interface Image {
  productImageId: number;
  image_name: string;
  fileOriginalName: string;
  extension_name: string;
  fullPath: string;
}
interface Product {
  id: number;
  requestProductName: string | null;
  description: string;
  price: number;
  quantity: number;
  completionTime: string;
  request_id: number;
  imagesList: Image[];
}
@Component({
  selector: 'app-order-request-component',
  templateUrl: './order-request-component.component.html',
  styleUrls: ['./order-request-component.component.scss']
})
export class OrderRequestComponentComponent {
  constructor(private orderRequestService: OrderRequestService, private toastr: ToastrService) { }
  orderRq: any[] = [];
  orderRqDetails: OrderRequest | null = null;
  productsList: any[] = []; // Variable to hold the list of products
  
  currentPage: number = 1 ;
  searchKey: string = '';
  orderId: number = 0;
  orderStatus: number = 0;
  ngOnInit(): void {
    this.loadAllOrderRq();
    
  }
  loadAllOrderRq(): void{
    this.orderRequestService.getAllRequestOrder().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.orderRq = data.result;
          console.log('Danh sách order details: :', this.orderRq);
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
    this.orderRequestService.getProductById(orderId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.productsList = data.result; // Assuming getProductById returns an array of products
          console.log('Product List:', this.productsList);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
    this.orderRequestService.getRequestById(orderId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.orderRqDetails = data.result;
          console.log('OrderDetail:', this.orderRqDetails);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy thông tin order detail!', 'Lỗi'); // Display error toast
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Display generic error toast
      }
    );

    
  }
  
} 
