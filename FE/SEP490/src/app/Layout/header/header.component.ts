import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { AuthenListService } from 'src/app/service/authen.service';
import { FormControl } from '@angular/forms';
import { DataService } from 'src/app/service/data.service';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ToastrService } from 'ngx-toastr';
interface ApiResponse {
  code: number;
  result: any[];
}

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})

export class HeaderComponent implements OnInit {
  fullname: string | null = null;
  constructor(private dataService: DataService, private sanitizer: DomSanitizer,private toastr: ToastrService,  private router: Router, private http: HttpClient, private authService: AuthenListService, private productListService: ProductListService) { }
  ngOnInit(): void {
    this.wishlistcount()
    if (localStorage.getItem('fullname') === null) {
      this.authService.getUserProfile().subscribe((data) => {
        this.fullname = data.result.fullname; // Assuming 'result' contains the profile data
        localStorage.setItem('fullname', data.result?.fullname)
        console.log("fullname: ", data.result?.fullname)
      });
    }
    else {
      this.fullname = localStorage.getItem('fullname')
    }
    this.productListService.getAllProductCustomer().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.products = data.result;
      //    console.log('Danh sách sản phẩm:', this.products);
        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching categories:', error);
      }
    );
    
  }
  selectedSortByPrice: string = '';
  countwishlist: number = 0;
  user: any[] = [];
  parentData: any;
  searchKey?: string = '';
  categoryId?: number;
  minPrice?: number;
  maxPrice?: number;
  sortDirection?: number;
  selectedCategory: number = 0;
  products: any[] = [];
  searchControl = new FormControl();
  filteredProducts: any[] = [];
  selectedProduct: any = {};
  keyword = 'productName';
  selectedStatus: number = 0;
  sort: string = '';
  loadAllUsers(): void {
    this.productListService.getAllUser().subscribe(
      (response: ApiResponse) => {
        if (response.code === 1000) {
          this.user = response.result; // Lưu trữ dữ liệu người dùng vào biến users
        }
      },
      (error) => {
        console.error('Error fetching users:', error);
      }
    );
  }
  wishlistcount(): void {
    this.authService.GetByIdWishList().subscribe(
      (data) => {
        if (data != null || data != undefined)
          this.countwishlist = data.result.length; // Lưu trữ dữ liệu nhận được từ API vào biến wishlistItems
        else this.countwishlist = 0
      },
      (error) => {
        // console.error('Failed to fetch wishlist:', error);
        // Xử lý lỗi nếu cần thiết
      }
    );
  }
  onLogout(): void {
    // Lấy giá trị của token từ local storage
    const token = localStorage.getItem('loginToken');
 //   console.log('Token trước khi logout:', token);

 //   console.log('remove loginToken');
    // Xóa token đăng nhập khỏi local storage
    localStorage.removeItem('loginToken');
 //   console.log('Token sau khi logout:', localStorage.getItem('loginToken'));

    this.router.navigateByUrl('/login');


  }
  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }
  isLogout(): boolean {
    return !this.authService.isLoggedIn();
  }
  onSearch(): void {
    console.log('Search key header:', this.searchKey);
    this.dataService.changeSearchKey(this.searchKey);
    this.router.navigate(['/product']);
  }
 
  onChangeSearch(search: string) {
    this.searchKey = search;
  }

  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
  }

  selectProduct(product: any): void {
    this.selectedProduct = product; // Điều chỉnh theo cấu trúc đối tượng sản phẩm của bạn
    const productName = this.selectedProduct.productName;
    this.dataService.changeSearchKey(productName);
    this.router.navigate(['/product']);
  
  }


}