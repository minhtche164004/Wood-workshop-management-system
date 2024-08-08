import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ProductListService } from '../service/product/product-list.service';
import { ErrorProductService } from '../service/error-product.service';
import { ToastrService } from 'ngx-toastr';
import { data } from 'jquery';

@Component({
  selector: 'app-product-cate-management',
  templateUrl: './product-cate-management.component.html',
  styleUrls: ['./product-cate-management.component.scss']
})
export class ProductCateManagementComponent implements OnInit{
  
  ngOnInit(): void {
    this.getAllCategory();
  }
  constructor(private fb: FormBuilder,
    private route: ActivatedRoute,
    private productListService: ProductListService,
    private errorProductService: ErrorProductService,
    private toastr: ToastrService) {
      this.editProductCate = this.fb.group({
        categoryName: [''],
       
      });
    }
    editProductCate: FormGroup;
  isLoadding: boolean = false;
  currentPage: number = 1;
  errorProducts: any[] = [];
  newProductCate: string = '';
  searchKey: string = ''; 
  checkNotFound: boolean = false;
  getAllCategory(): void{
    this.productListService.getAllCategories().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.errorProducts = data.result;
           console.log('Categories:', this.errorProducts);
           this.checkNotFound = true;
        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching categories:', error);
      }
    );
  }
  deleteCategory(categoryId: number): void {
    console.log('Delete category:', categoryId)
    this.isLoadding = true;
    this.productListService.deleteCategory(categoryId).subscribe(
      
      (response: any) => {
        if (response.code === 1000) {
          this.toastr.success('Xóa danh mục sản phẩm thành công');
          this.getAllCategory();
          this.closeModal();
          this.isLoadding = false;
        } else {
          console.error('Error deleting category:', response);
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error deleting category:', error);
        this.toastr.warning(error.error.message, 'Thông báo');
        this.isLoadding = false;
      }
    );
  }
  selectedCategory: any;
  openConfirmDeleteModal(category: any): void {
    
    this.selectedCategory = category;
    console.log('Delete category:', this.selectedCategory);
  }
  EditSupMaterial(): void {
    this.isLoadding = true;
    console.log('Edit category:', this.editProductCate.value);
    console.log('id: ', this.productCategoryId);
    this.productListService.editCategory(this.productCategoryId, this.editProductCate.value.categoryName).subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.toastr.success('Sửa danh mục sản phẩm thành công');
          this.getAllCategory();  // Load lại danh sách sau khi chỉnh sửa
          this.closeModal();
           this.isLoadding = false;
        } else {
          console.error('Invalid data returned:', data); 
          this.isLoadding = false;
        }
      },
      error => {
        console.error('Error editing category:', error);
        this.toastr.warning(error.error.message, 'Thông báo'); 
        this.isLoadding = false;
      }
    );
  }
  
  findCategories(): void {
    console.log('Search key:', this.searchKey);
    this.isLoadding = true;
    if (this.searchKey) {
      this.productListService.findCategoriesByName(this.searchKey).subscribe(
        (data: any) => {
          if (data.code === 1000) {
            this.errorProducts = data.result;
             console.log('Categories search:', this.errorProducts);
            this.isLoadding = false;
            this.checkNotFound = true;
          } else {
            console.error('Invalid data returned:', data);
            this.isLoadding = false;
            this.checkNotFound = false;
          }
        },
        (error) => {
          console.error('Error fetching categories:', error);
          this.checkNotFound = false;this.isLoadding = false;
        }
      );
    } else {
      console.error('Search key is required');
      this.isLoadding = false;
    }
  }
  addCategory(): void {
    console.log('Add category:', this.newProductCate);
    if(this.newProductCate == ''){
      this.toastr.info('Tên danh mục không được để trống', 'Thông báo');
      return;
    }
    if (this.newProductCate) {
      this.productListService.addNewCategory(this.newProductCate).subscribe(
        response => {
          console.log('Category added successfully:', response);
          // Đóng modal sau khi thêm danh mục thành công
          this.toastr.success('Thêm danh mục sản phẩm thành công');
          this.getAllCategory();
          this.closeModal();
        },
        error => {
          console.error('Error adding category:', error);
          this.toastr.warning(error.error.message, 'Thông báo');
        }
      );
    } else {
      console.error('Category name is required');
    
    }
  }
  closeModal(): void {
    // Đóng modal bằng cách đặt lại newProductCate và sử dụng jQuery để ẩn modal
    this.newProductCate = '';
    $('[data-dismiss="modal"]').click();
  }
  productCategoryId: number = 0;
  editProductCategory(category: any): void{
    console.log('Edit category:', category);
    this.productCategoryId = category.categoryId;
    this.editProductCate.patchValue({
      categoryName: category.categoryName,
    });
    console.log('Edit category:', this.editProductCate.value);
  }
}
