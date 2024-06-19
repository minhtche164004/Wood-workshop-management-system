import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service'; // Đảm bảo đường dẫn đúng

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: any[] = [];
  loginToken: string | null = null;

  constructor(private productListService: ProductListService) { }

  ngOnInit(): void {
    // Lấy loginToken từ localStorage
    this.loginToken = localStorage.getItem('loginToken');

    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      
      // Gọi dịch vụ để lấy danh sách sản phẩm
      this.productListService.getProducts().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.products = data.result;
            console.log('Danh sách sản phẩm:', this.products); // In ra danh sách sản phẩm vào console
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