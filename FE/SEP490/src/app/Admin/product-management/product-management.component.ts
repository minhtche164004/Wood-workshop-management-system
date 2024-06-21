import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr';
import { HttpHeaders } from '@angular/common/http';
import { MessageService } from 'primeng/api';
@Component({
  selector: 'app-product-management',
  templateUrl: './product-management.component.html',
  styleUrls: ['./product-management.component.scss']
})
export class ProductManagementComponent implements OnInit {

  products: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;

  searchKey: string = '';
  categories: any[] = [];
  selectedCategory: any = null;
  selectedStatus: any = null;
  product_name: string = '';
  description: string = '';
  quantity: number = 0;
  price: number = 0;
  category_id: number | null = null;
  selectedType: number = 0;
  productImages: File[] = [];
  thumbnailImage: File | null = null;

  constructor(private productListService: ProductListService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.loginToken = localStorage.getItem('loginToken');
    this.loadCategories();

    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.productListService.getProducts().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.products = data.result;
            console.log('Danh sách sản phẩm:', this.products);
          } else {
            console.error('Failed to fetch products:', data);
            this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
          }
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        }
      );
    } else {
      console.error('No loginToken found in localStorage.');
    }
  }

  loadCategories(): void {
    this.productListService.getAllCategories().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.categories = data.result;
          console.log('Danh sách Loại:', this.categories);
        } else {
          console.error('Dữ liệu trả về không hợp lệ:', data);
        }
      },
      (error) => {
        console.error('Lỗi khi lấy danh sách Loại:', error);
      }
    );
  }

  searchProduct(): void {
    console.log("Thực hiện tìm kiếm sản phẩm: ", this.searchKey);
    console.log("Tìm theo loại: ", this.selectedCategory);
    if (this.searchKey && this.selectedCategory == null) {
      this.productListService.findProductByNameOrCode(this.searchKey)
        .subscribe(
          (data) => {
            if (data.code === 1000) {
              this.products = data.result;
              console.log('Tìm kiếm thành công:', this.products);
              this.toastr.success('Tìm kiếm sản phẩm thành công!', 'Thành công');
            } else if (data.code === 1015) {
              this.products = []; // Clear previous results
              console.error('Tìm kiếm không thành công:', data);
              this.toastr.error('Không tìm thấy sản phẩm!', 'Tìm kiếm thất bại');
            } 
          }
        );
    } else if (this.selectedCategory && this.searchKey == null){
      this.productListService.findProductByCategory(this.selectedCategory)
        .subscribe(
        
          (data) => {
            console.log("Category: ", this.selectedCategory)
            if (data.code === 1000) {
              this.products = data.result;
              console.log('Tìm kiếm thành công:', this.products);
              this.toastr.success('Tìm kiếm sản phẩm theo kiểu thành công!', 'Thành công');
            } else if (data.code === 1015) {
              this.products = []; // Clear previous results
              console.error('Tìm kiếm không thành công:', data);
              this.toastr.error('Không tìm thấy sản phẩm!', 'Tìm kiếm thất bại');
            } 
          }
        );
    }
  }

  getProductDetails(productId: number): void {
    this.productListService.getProductById(productId)
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            console.log('Product details:', data.result);
          } else {
            console.error('Failed to fetch product details:', data);
          }
        },
        (error) => {
          console.error('Error fetching product details:', error);
        }
      );
  }

  onFilesSelected(event: any) {
    if (event.target.files.length > 0) {
      // Lưu danh sách các file vào một mảng
      this.productImages = [];
      for (let i = 0; i < event.target.files.length; i++) {
        const file = event.target.files[i];
        this.productImages.push(file);
      }
    }
  }

  onThumbnailSelected(event: any) {
    if (event.target.files && event.target.files.length > 0) {
      this.thumbnailImage = event.target.files[0];
    }
  }

  addProduct(): void {
    if (!this.productImages || this.productImages.length === 0 || !this.thumbnailImage) {
      this.toastr.error('Vui lòng chọn cả ảnh sản phẩm và ảnh thumbnail!', 'Lỗi');
      return;
    }

    const productDTO = {
      product_name: this.product_name,
      description: this.description,
      quantity: this.quantity,
      price: this.price,
      status_id: this.selectedStatus,
      enddateWarranty: new Date().toISOString(),
      completionTime: new Date().toISOString(),
      category_id: this.selectedCategory,
      type: this.selectedType
    };
   
    const requestData = {
      productDTO: productDTO,
      productImages: this.productImages,
      thumbnailImage: this.thumbnailImage
    };
   
    const headers = {
      headers: {
        Authorization: `Bearer ${this.loginToken}`,
        'Content-Type': 'application/json'
      }
    };
    console.log("Add product request:", JSON.stringify(requestData)); // In ra giá trị của requestData

    this.productListService.addNewProduct(requestData)
      .subscribe(
        (response) => {
          this.toastr.success('Sản phẩm đã được thêm thành công!', 'Thành công');
        },
        (error) => {
          console.log("add product request: " + requestData)
          console.error('Lỗi khi thêm sản phẩm:', error);
          this.toastr.error('Đã xảy ra lỗi khi thêm sản phẩm!', 'Lỗi');
        }
      );
  }
  
  editProduct(productId: number) {
    // Fetch product details by ID for editing
    this.productListService.getProductById(productId)
      .subscribe(product => {
        // Populate edit modal form with retrieved product data
        // ...
      });
  }
  
  deleteProduct(productId: number) {
    
  }
  
  showProductDetails(productId: number) {
    // Fetch product details by ID for displaying in modal
    this.productListService.getProductById(productId)
      .subscribe(product => {
        // Update modal content with retrieved product data
        // ...
      });
  }
}
