import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service'; // Đảm bảo đường dẫn đúng

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
  products: any[] = [];

  constructor(private productListService: ProductListService) { }

  ngOnInit(): void {
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
  }
}
