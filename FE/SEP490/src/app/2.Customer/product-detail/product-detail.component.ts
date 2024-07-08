import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/app/environments/environment';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {
  isDescriptionActive: boolean = true;
  productId: number = 0;
  categoryId: number = 0;
  productDetails: any = {};
  categoryProduct: any= {};
  largeImageUrl: string = '';

  constructor(private route: ActivatedRoute, private http: HttpClient) {
    this.onTabClick('description');
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.productId = +params['id'];
      this.getProductDetails(this.productId);
    });
  }

  getProductDetails(id: number) {
    this.http.get(`${environment.apiUrl}api/auth/product/ViewDetailProductById?id=${id}`)
      .subscribe(
        (response: any) => {
          this.productDetails = response.result;
          this.categoryId = this.productDetails.categories.categoryId;
          this.largeImageUrl = this.productDetails.image;
          console.log('Product details:', this.productDetails);
          console.log('CategoryID:', this.categoryId);
          this.getCategoryProducts(this.categoryId);
        },
        (error) => {
          console.error('Error fetching product details:', error);
        }
      );
  }
  getCategoryProducts(categoryId: number) {
    this.http.get(`${environment.apiUrl}api/auth/product/GetProductByCategory?id=${categoryId}`)
      .subscribe(
        (response: any) => {
          this.categoryProduct = response.result;
        
  

        },
        (error) => {
          console.error('Error fetching product details:', error);
        }
      );
  }
  

  updateLargeImage(imageUrl: string) {
    this.largeImageUrl = imageUrl;
  }

  onTabClick(tabName: string): void {
    if (tabName === 'description') {
      console.log('Mô Tả tab activated');
      // Handle the event when the "Mô Tả" tab is clicked or activated.
      // Add your logic here.
    } else if (tabName === 'warranty') {
      console.log('Thông tin bảo hành tab activated');
      // Handle the event when the "Thông tin bảo hành" tab is clicked or activated.
      // Add your logic here.
    }
  }
}