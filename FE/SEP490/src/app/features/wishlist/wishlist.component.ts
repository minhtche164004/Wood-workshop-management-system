import { Component } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { AuthenListService } from 'src/app/service/authen.service';
@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss']
})
export class WishlistComponent {
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
    console.log('Delete supplier:', this.deleteId);
  
    this.authenListService.deleteWishList(this.deleteId).subscribe(
      response => {
        console.log('Supplier deleted successfully!', response);
        this.toastr.success('Nhà cung cấp đã được xóa thành công!', 'Thành công');
        this.router.navigate(['/wishlist']); // Navigate to the wishlist page
        this.modalRef.close(); // Close the modal
        this.ngOnInit(); // Refresh the list of suppliers
      },
      error => {
        console.error('Error deleting supplier:', error);
        // Xử lý lỗi (Implement error handling logic)
      }
    );
  }
} 
