import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http'; 
import { AuthenListService } from 'src/app/service/authen.service'; 
import { FormControl } from '@angular/forms';
import { DataService } from 'src/app/service/data.service';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit{

  constructor(private dataService: DataService, private router: Router, private http: HttpClient,private authService: AuthenListService, private productListService: ProductListService) { } 
  ngOnInit(): void {
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(keyword => this.productListService.getMultiFilterProductForCustomer(keyword))
    ).subscribe(products => {
      this.filteredProducts = products;
    });
  }
  parentData: any;
  searchKey?: string = '';
  categoryId?: number;
  minPrice?: number;
  maxPrice?: number;
  sortDirection?: number;
  products: any[] = [];
  searchControl = new FormControl();
  filteredProducts: any[] = [];
  onLogout(): void {
    // Lấy giá trị của token từ local storage
    const token = localStorage.getItem('loginToken');
    console.log('Token trước khi logout:', token);

    console.log('remove loginToken');
    // Xóa token đăng nhập khỏi local storage
    localStorage.removeItem('loginToken');
    console.log('Token sau khi logout:', localStorage.getItem('loginToken'));

    this.router.navigateByUrl('/login');
    

  }
  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }
  isLogout(): boolean {
    return !this.authService.isLoggedIn();
  }
  onSearch(): void {
    this.dataService.changeSearchKey(this.searchKey);
    
    this.router.navigate(['/product']);
  }
  onOptionSelected(event: any) {
    // Xử lý khi lựa chọn một sản phẩm từ autocomplete
    console.log('Selected product:', event.option.value);
    // Gọi hàm tìm kiếm sản phẩm ở đây nếu cần
    this.getProductsSearch();
  }

  getProductsSearch() {
    // Thực hiện tìm kiếm sản phẩm dựa trên từ khóa đã chọn
    const selectedProduct = this.filteredProducts.find(product => product.productName === this.searchControl.value);
    if (selectedProduct) {
      console.log('Performing search for:', selectedProduct);
      // Gọi service hoặc hàm tìm kiếm sản phẩm với selectedProduct.productId
    }
  }
}
