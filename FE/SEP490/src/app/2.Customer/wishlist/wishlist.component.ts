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
    this.isLoadding = true;

    // Thay thế bằng user_id thích hợp của bạn
    this.authenListService.GetByIdWishList().subscribe(
      (data) => {


        this.wishlistItems = data.result; // Lưu trữ dữ liệu nhận được từ API vào biến wishlistItems
       
        this.isLoadding = false;

      },
      (error) => {
        console.error('Failed to fetch wishlist:', error);
        this.isLoadding = false;

      }
    );
  }
  ReloadWishlist() {
    this.authenListService.GetByIdWishList().subscribe(
      (data) => {
        this.wishlistItems = data.result; // Lưu trữ dữ liệu nhận được từ API vào biến wishlistItems
      },
      (error) => {
        console.error('Failed to fetch wishlist:', error);
        this.isLoadding = false;
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
        this.ReloadWishlist();
        this.toastr.success('Xóa sản phẩm yêu thích thành công!', 'Thành công');
        $('[data-dismiss="modal"]').click();
      },
      error => {
        this.isLoadding = false;
        console.error('Error deleting supplier:', error);
        // Xử lý lỗi (Implement error handling logic)
      }
    );
  }
} 
