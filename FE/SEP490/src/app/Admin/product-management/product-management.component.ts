import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr';
import { FormBuilder, FormGroup } from '@angular/forms';

interface Category {
  categoryId: number;
  categoryName: string;
}

@Component({
  selector: 'app-product-management',
  templateUrl: './product-management.component.html',
  styleUrls: ['./product-management.component.scss']
})
export class ProductManagementComponent implements OnInit {
  uploadForm: FormGroup;
  selectedThumbnail: File | null = null;
  selectedImages: File[] = [];
  categories: Category[] = [];
  loginToken: string | null = null;
  products: any[] = [];
  currentPage: number = 1;
  searchKey: string = '';
  selectedCategory: number = 0;
  selectedStatus: any = null;
  selectedType: number = 0;
  productImages: File[] = [];
  thumbnailImage: File | null = null;

  constructor(
    private fb: FormBuilder,
    private productListService: ProductListService,
    private toastr: ToastrService
  ) {
    this.uploadForm = this.fb.group({
      product_name: [''],
      description: [''],
      price: [0],
      category_id: [0],
      type: [0]
    });
  }

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
          this.categories = data.result as Category[];

          console.log('Categories:', this.categories);
          this.categories.forEach(category => {
            console.log(`Category ID: ${category.categoryId}, Category Name: ${category.categoryName}`);
          });
        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching categories:', error); 
      }
    );
  }

  onCategoryChange(selectedValue: string) {
    const categoryId = parseInt(selectedValue, 10);
    console.log("Selected category ID:", categoryId);
  }

  searchProduct(): void {
    console.log("Thực hiện tìm kiếm sản phẩm: ", this.searchKey);

    if (this.searchKey && this.selectedCategory == null) {
      this.productListService.findProductByNameOrCode(this.searchKey)
        .subscribe(
          (data) => {
            if (data.code === 1000) {
              this.products = data.result;
              console.log('Tìm kiếm thành công:', this.products);
              this.toastr.success('Tìm kiếm sản phẩm thành công!', 'Thành công');
            } else if (data.code === 1015) {
              this.products = [];
              console.error('Tìm kiếm không thành công:', data);
              this.toastr.error('Không tìm thấy sản phẩm!', 'Tìm kiếm thất bại');
            } 
          }
        );
    } else if (this.selectedCategory != null) {
      console.log("Tìm theo loại: ", this.selectedCategory);
      this.productListService.findProductByCategory(this.selectedCategory)
        .subscribe(
          (data) => {
            console.log("Category: ", this.selectedCategory)
            if (data.code === 1000) {
              this.products = data.result;
              console.log('Tìm kiếm thành công:', this.products);
              this.toastr.success('Tìm kiếm sản phẩm theo kiểu thành công!', 'Thành công');
            } else if (data.code === 1015) {
              this.products = [];
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
  filterProducts(): void {
    // console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory, "và giá:", this.selectedSortByPrice);
  }
  onFilesSelected(event: any) {
    if (event.target.files.length > 0) {
      this.productImages = [];
      for (let i = 0; i < event.target.files.length; i++) {
        const file = event.target.files[i];
        this.productImages.push(file);
      }
    }
  }

  onThumbnailSelected(event: any): void {
    this.selectedThumbnail = event.target.files[0];
  }

  onImagesSelected(event: any): void {
    this.selectedImages = Array.from(event.target.files);
  } 

  onSubmit(): void {
    if (this.uploadForm.valid && this.selectedThumbnail && this.selectedImages.length) {
      const productData = this.uploadForm.value;
      console.log('Form Data:', productData);
      console.log('Selected Thumbnail:', this.selectedThumbnail);
      console.log('Selected Images:', this.selectedImages);

      this.productListService.uploadProduct(productData, this.selectedThumbnail, this.selectedImages)
        .subscribe(
          response => {
            
            this.toastr.success('Tạo sản phẩm thành công!', 'Thành công');
            this.ngOnInit();
          },
          error => {
            
            this.toastr.error('Tạo sản phẩm bị lỗi!', 'Lỗi');
          }
        );
    }
  }

  reloadProducts(): void {
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
  }

  editProduct(productId: number) {
    this.productListService.getProductById(productId)
      .subscribe(product => {
        // Populate edit modal form with retrieved product data
        this.uploadForm.patchValue({
          product_name: product.product_name,
          description: product.description,
          price: product.price,
          category_id: product.category_id,
          type: product.type
        });
        this.selectedStatus = product.status;
        this.selectedType = product.type;
      });
  }
 
  onEditSubmit(): void {
    if (this.uploadForm.valid) {
      const productData = this.uploadForm.value;
      console.log('Form Data for Edit:', productData);

      const updatedProduct = {
        ...productData,
        status: this.selectedStatus,
        type: this.selectedType,
        thumbnail: this.selectedThumbnail,
        images: this.selectedImages
      };

    //   this.productListService.updateProduct(updatedProduct)
    //     .subscribe(
    //       response => {
    //         console.log('Update successful', response);
    //         this.toastr.success('Cập nhật sản phẩm thành công!', 'Thành công');
    //         this.reloadProducts();
    //       },
    //       error => {
    //         console.error('Update error', error);
    //         this.toastr.error('Cập nhật sản phẩm bị lỗi!', 'Lỗi');
    //       }
    //     );
     }
  }

  deleteProduct(productId: number) {
    // Implement delete logic
  }

  showProductDetails(productId: number) {
    this.productListService.getProductById(productId)
      .subscribe(product => {
        // Update modal content with retrieved product data

      });
  }
}