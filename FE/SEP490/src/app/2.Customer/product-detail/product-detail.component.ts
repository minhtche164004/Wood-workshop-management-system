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
  productId: number = 0;
  productDetails: any;
  largeImageUrl: string = ''; // Add this property
  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.productId = +params['id']; // Retrieve the product ID from the route parameters
      this.getProductDetails(this.productId);
    });
  }

  getProductDetails(id: number) {
    this.http.get(`${environment.apiUrl}api/auth/product/ViewDetailProductById?id=${id}`)
      .subscribe(
        (response: any) => {
          this.productDetails = response.result;
          this.largeImageUrl = this.productDetails.image; // Initialize largeImageUrl
          console.log('Product details:', this.productDetails);
        },
        (error) => {
          console.error('Error fetching product details:', error);
        }
      );
  }
  updateLargeImage(imageUrl: string) {
    this.largeImageUrl = imageUrl; // Update largeImageUrl when a smaller image is clicked
  }
}
