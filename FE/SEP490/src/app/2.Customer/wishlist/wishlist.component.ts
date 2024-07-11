import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthenListService } from 'src/app/service/authen.service';
import { timer } from 'rxjs';
@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss']
})
export class WishlistComponent {
  isLoadding: boolean = false; 
  wishlistItems: any[] = []; // Khai báo mảng để lưu các mục trong wishlist
  deleteId: any; // variable to store supplierId to be deleted
  router: any;
  currentPage: number = 1;
  modalRef: any;
  constructor(private authenListService: AuthenListService, private toastr: ToastrService) { }
  ngOnInit(): void {
    this.loadWishlist(); // Gọi hàm để tải wishlist khi component được khởi tạo
  }

  loadWishlist() {
    // Thay thế bằng user_id thích hợp của bạn
    this.authenListService.GetByIdWishList().subscribe(
      (data) => {
        this.wishlistItems = data.result; // Lưu trữ dữ liệu nhận được từ API vào biến wishlistItems
        console.log("Data WishList: ", data)
      },
      (error) => {
        console.error('Failed to fetch wishlist:', error);
        // Xử lý lỗi nếu cần thiết
      }
    );
  }
  setDeleteId(wishlistId: number) {
    console.log('Set deleteId:', wishlistId);
    this.deleteId = wishlistId;
  }

  deleteSupplier() {
    this.isLoadding = true;
    this.authenListService.deleteWishList(this.deleteId).subscribe(
      response => {
        this.isLoadding = false;
        this.toastr.success('Xóa sản phẩm yêu thích thành công!', 'Thành công');
        timer(1000).subscribe(() => {
          window.location.reload();
        });
      },
      error => {
        this.isLoadding = false;
        console.error('Error deleting supplier:', error);
        // Xử lý lỗi (Implement error handling logic)
      }
    );
  }
} 
