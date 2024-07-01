import { Component } from '@angular/core';
import { AuthenListService } from 'src/app/service/authen.service';
@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss']
})
export class WishlistComponent {
  wishlistItems: any[] = []; // Khai báo mảng để lưu các mục trong wishlist
  constructor( private authenListService: AuthenListService,) {}
  ngOnInit(): void {
    this.loadWishlist(); // Gọi hàm để tải wishlist khi component được khởi tạo
  }

  loadWishlist() {
    const user_id = '50'; // Thay thế bằng user_id thích hợp của bạn
    this.authenListService.GetByIdWishList(user_id).subscribe(
      (data) => {
        this.wishlistItems = data.result; // Lưu trữ dữ liệu nhận được từ API vào biến wishlistItems
      },
      (error) => {
        console.error('Failed to fetch wishlist:', error);
        // Xử lý lỗi nếu cần thiết
      }
    );
  }
} 
