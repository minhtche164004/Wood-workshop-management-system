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
  listWoodMaterial: string[] = [];
  listPaintMaterial: string[] = [];
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
  addToWishlist(productId: number): void {
    this.wishList.addWishlist(productId).subscribe( 
      data => {
        console.log('data:', data);
        if (data.code === 1000) {
          this.toastr.success('Sản phẩm đã được thêm vào yêu thích!', 'Thành công');
        } else if (data.code === 1034) {
          this.toastr.error('Sản phẩm đã được thêm vào yêu thích!', 'Thành công');
        } else {
          console.log("data.code: ", data.code);
          this.toastr.warning('Vui lòng đăng nhập để thêm sản phẩm yêu thích!', 'Lỗi');
        }
      },
      error => {
        // console.error('error:', error);
        this.toastr.success('Sản phẩm đã được thêm vào yêu thích!', 'Thành công');
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
          // console.log(this.productDetails.sub_material_name);
          // if(this.productDetails.sub_material_name.length)
          this.listWoodMaterial = [];
          this.listPaintMaterial = [];
          this.productDetails.sub_material_name.map((submaterial: any)=>{
            if(submaterial.materialId == 1){
              this.listWoodMaterial.push(submaterial.subMaterialName)
            }
            if(submaterial.materialId == 4){
              this.listPaintMaterial.push(submaterial.subMaterialName)
            }
          })

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