import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/app/environments/environment';
import { WishlistService } from 'src/app/service/wishlist.service';
import { ToastrService } from 'ngx-toastr';

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
  largeImageUrl_Cate: string = '';

  constructor(private route: ActivatedRoute, private http: HttpClient,private wishList: WishlistService,private toastr: ToastrService) {
    this.onTabClick('description');
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.productId = +params['id'];
      this.getProductDetails(this.productId);
    });
    this.shuffleArray(this.categoryProduct);
  }
  addToWishlist(productId: number) {  
   
    console.log('Product ID:', productId);
    this.wishList.addWishlist(productId)
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            console.log('Product added to wishlist:');
            this.toastr.success('Sản phẩm đã được thêm vào yêu thích!', 'Thành công'); // Success message

          }else if(data.code === 1005){
            this.toastr.warning('Vui lòng đăng nhập để thêm sản phẩm yêu thích!', 'Lỗi'); // Error message
          }
        },
        (error) => {
          console.error('Error adding product to wishlist:', error);
          this.toastr.warning('Vui lòng đăng nhập để thêm sản phẩm yêu thích!', 'Lỗi'); // Error message
        }
      );
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
          this.largeImageUrl_Cate = this.categoryProduct.image;
          this.categoryProduct = this.shuffleArray(this.categoryProduct);

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
  shuffleArray(array: any[]): any[] {
    for (let i = array.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
  }
}