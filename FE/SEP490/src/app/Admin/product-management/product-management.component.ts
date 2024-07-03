import { Component, OnInit } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr';
import { FormArray, FormBuilder, FormGroup, Validators, FormsModule, FormControl } from '@angular/forms';
import { concatMap } from 'rxjs/operators';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef } from '@angular/core';

interface Category {
  categoryId: number;
  categoryName: string;
}

interface Status {
  status_id: number;
  status_name: string;
}

interface Material {
  materialId: number;
  materialName: string;
  type: string;
}

interface SubMaterial {
  subMaterialId: number;
  subMaterialName: string;
  unitPrice: number;
}
interface MaterialItem {
  subMaterialId: string;
  quantity: number;
}
interface SubMaterialItemOfProduct {
  materialId: number;
  subMaterialId: number;
  subMaterialName: string;
  quantity: number;
  unitPrice: number;
  materialType: string;
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
  statuses: Status[] = [];
  loginToken: string | null = null;
  products: any[] = [];
  currentPage: number = 1;
  searchKey: string = '';
  selectedCategory: number = 0;
  selectedStatus: number = 0;
  // selectedType: number = 0;
  selectedSortByPrice: string = 'asc';
  productImages: File[] = [];
  thumbnailImage: File | null = null;
  materialForm: FormGroup;  // tao list material de luu vao bang
  materials: Material[] = [];
  subMaterials: SubMaterial[][] = [];
  selectedMaterialId: { [key: number]: string } = {};
  materialType: { [key: number]: string } = {};
  unitPriceSubMaterial: { [key: number]: number | string } = {};
  selectedSubMaterialId: { [key: string]: any } = {};
  editForm: FormGroup;
  formSubMaterialPerProduct: FormGroup;
  subMaterialItemOfProduct: SubMaterialItemOfProduct[] = [];
  subMaterialData: any;
  selectedProductIdCurrentDelele: number = 0;
  selectedProductNameCurrentDelele: string | null = null;

  isProduct: boolean = false;
  constructor(
    private fb: FormBuilder,
    private productListService: ProductListService,
    private toastr: ToastrService,
    private cdr: ChangeDetectorRef
  ) {
    this.uploadForm = this.fb.group({
      product_name: [''],
      description: [''],
      price: [0],
      category_id: [0],
      type: [0]
    });

    this.editForm = this.fb.group({
      product_id: [0],
      product_name: [''],
      description: [''],
      price: [0],
      category_id: [0],
      completionTime: [''],
      enddateWarranty: [''],
      status_id: [''],
      image: [''],
      quantity: [0],
      type: [0]
    });

    this.materialForm = this.fb.group({
      items: this.fb.array([]),
    });

    this.formSubMaterialPerProduct = this.fb.group({
      itemsEdit: this.fb.array([]),
    });
  }

  //phan formGroup cua add productt
  get items(): FormArray {
    return this.materialForm.get('items') as FormArray;
  }

  addItem(): void {
    const item = this.fb.group({
      materialId: [''],
      subMaterialId: [''],
      price: [''],
      quantity: ['']
    });
    this.items.push(item);
  }

  removeItem(index: number) {
    const items = this.materialForm.get('items') as FormArray;
    items.removeAt(index);
  }
  //phan formGroup cua edit productt

  populateFormWithData(productId: number) {

    this.productListService.exportMaterialProductByProductId(productId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.subMaterialData = data.result;
          // console.log('Danh sách material sản phẩm:', this.subMaterialData);
          // console.log('productId', productId);

          // Reset the form and remove all items
          this.formSubMaterialPerProduct.reset();
          while (this.itemsEditArray.length !== 0) {
            this.itemsEditArray.removeAt(0);
          }
          // Populate the form with data
          this.selectedMaterialId = [] as any;
          this.subMaterialData.forEach((materialItem: any, index: any) => {
            this.selectedMaterialId[index] = materialItem.materialId;
            this.selectedSubMaterialId[index] = materialItem.subMaterialId;
            this.onMaterialChangeFirstEdit(Number(this.selectedMaterialId[index]), index);

            this.fillMaterialItemEdit(materialItem);

          });

          console.log(this.itemsEditArray.value);

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

  onMaterialChangeFirstEdit(materialId: number, index: number) {
    this.loadSubMaterials(materialId, index);
  }

  get itemsEditArray(): FormArray {
    return this.formSubMaterialPerProduct.get('itemsEdit') as FormArray;
  }

  fillMaterialItemEdit(material: SubMaterialItemOfProduct) {
    const itemFormGroup = this.fb.group({
      materialId: [material.materialId],
      subMaterialId: [material.subMaterialId],
      subMaterialName: [material.subMaterialName],
      materialType: [material.materialType],
      quantity: [material.quantity],
      unitPrice: [material.unitPrice]
    });
    this.itemsEditArray.push(itemFormGroup);
  }

  addItemEdit(): void {
    const item = this.fb.group({
      materialId: [''],
      subMaterialId: [''],
      price: [''],
      quantity: ['']
    });
    this.itemsEditArray.push(item);
  }

  removeItemEdit(index: number) {
    if (this.itemsEditArray && this.itemsEditArray.length > index) {
      this.itemsEditArray.removeAt(index);
    }
  }
  //

  ngOnInit(): void {
    this.loginToken = localStorage.getItem('loginToken');
    this.loadCategories();
    this.loadStatus();
    this.loadMaterials();

    if (this.loginToken) {
      // console.log('Retrieved loginToken:', this.loginToken);
      if(this.isProduct == true){
        this.productListService.getProducts().subscribe(
          (data) => {
            if (data.code === 1000) {
              this.products = data.result;
              // console.log('Danh sách sản phẩm:', this.products);
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
      else{
        this.productListService.getAllProductRequest().subscribe(
          (data) => {
            if (data.code === 1000) {
              this.products = data?.result;
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

    } else {
      console.error('No loginToken found in localStorage.');
    }

  }

  loadCategories(): void {
    this.productListService.getAllCategories().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.categories = data.result as Category[];

          // console.log('Categories:', this.categories);
          this.categories.forEach(category => {
            // console.log(`Category ID: ${category.categoryId}, Category Name: ${category.categoryName}`);
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

  // onCategoryChange(selectedValue: string) {
  //   const categoryId = parseInt(selectedValue, 10);
  //   // console.log("Selected category ID:", categoryId);
  // }

  loadStatus(): void {
    this.productListService.getAllProductStatus().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.statuses = data.result as Status[];

          // console.log('Status:', this.statuses);
          this.statuses.forEach(status => {
            // console.log(`Status ID: ${status.status_id}, Status Name: ${status.status_name}`);
          });
        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching status:', error);
      }
    );
  }

  onChangeProductOrRequestProduct(isProduct: number) {
    if (isProduct == 0) {
      this.isProduct = true;
    } else {
      this.isProduct = false;
    }
  }

  filterProducts(): void {
    // console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory, "và giá:", this.selectedSortByPrice);

    this.productListService.getMultiFillterProductForAdmin(this.searchKey, this.selectedCategory, this.selectedStatus, this.selectedSortByPrice)
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.products = data.result;
            console.log('Lọc sản phẩm thành công:', this.products);
            this.toastr.success('Lọc sản phẩm thành công!', 'Thành công');
          } else if (data.code === 1015) {
            this.products = [];
            console.error('Lọc sản phẩm không thành công:', data);
            this.toastr.error('Không tìm thấy sản phẩm phù hợp!', 'Lọc thất bại');
          }
        }
      );
  }

  loadMaterials(): void {
    this.productListService.getAllMaterialProduct().subscribe(
      (data: any) => {
        this.materials = data?.result;
        // console.log('Materials:', this.materials);
      },
      (error) => {
        console.error('Error fetching materials:', error);
      }
    );
  }

  onMaterialChange(event: Event, index: number) {
    this.selectedSubMaterialId[index] = Number((event.target as HTMLSelectElement).value);    // Gọi hàm để tải sub-materials dựa trên giá trị được chọn
    const selectedMaterial = this.materials.find(material => material.materialId === this.selectedSubMaterialId[index]);
    this.materialType[index] = selectedMaterial ? selectedMaterial.type : '';

    this.loadSubMaterials(this.selectedSubMaterialId[index], index);
    // console.log('Selected MaterialId:', this.selectedSubMaterialId[index], " selected index:" + index);
  }

  loadSubMaterials(materialId: number, index: number): void {
    this.productListService.getAllSubMaterialByMaterialIdProduct(materialId).subscribe(
      (data: any) => {
        this.subMaterials[index] = data?.result;
        // console.log('Sub Materials:', this.subMaterials);
      },
      (error) => {
        console.error('Error fetching sub materials:', error);
      }
    );
  }

  onSubMaterialChange(event: Event, index: number) {
    this.selectedSubMaterialId[index] = Number((event.target as HTMLSelectElement).value);
    const selectedSubMaterial = this.subMaterials[index].find(subMaterial => subMaterial.subMaterialId === this.selectedSubMaterialId[index]);
    this.unitPriceSubMaterial[index] = selectedSubMaterial ? selectedSubMaterial.unitPrice : '';
    // console.log('Selected unitPriceSubMaterial: test', this.unitPriceSubMaterial);
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


  onSubmit() {
    if (this.uploadForm.valid && this.selectedThumbnail && this.selectedImages.length) {
      const productData = this.uploadForm.value;
      console.log(productData);

      console.log('data goc:', this.materialForm.value);
      var temp = this.materialForm.value;

      // tach lay quantity va subMaterialId
      var processedData = temp.items.map((item: MaterialItem) => (
        [(item.subMaterialId as string), item.quantity]
      ));
      // convert thanh dang map
      const transformedObject: { [key: string]: number } = {};

      for (const [subMaterialId, quantity] of processedData) {
        transformedObject[subMaterialId] = quantity;
      }

      // var productId;
      this.productListService.uploadProduct(productData, this.selectedThumbnail, this.selectedImages).pipe(
        concatMap(response => {

          const transformedData = {
            productId: response.result.productId,
            subMaterialQuantities: transformedObject
          };
          return this.productListService.createExportMaterialProduct(transformedData);
        })
      ).subscribe(
        response => {
          this.toastr.success('Tạo sản phẩm thành công!', 'Thành công');
          const closeButton = document.querySelector('.btn-mau-do[data-dismiss="modal"]') as HTMLElement;
          if (closeButton) { // Check if the button exists
            closeButton.click(); // If it exists, click it to close the modal
          }
        },
        error => {
          this.toastr.error('Tạo sản phẩm bị lỗi!', 'Lỗi');
        }
      );
    }
  }

  editProduct(productId: number) {
    this.editForm.patchValue({
      product_id: productId,
      product_name: null,
      description: null,
      price: null,
      category_id: null,
      image: null,
      quantity: null
    });
    this.productListService.getProductById(productId)
      .subscribe(product => {
        this.editForm.patchValue({
          product_name: product.result.productName,
          description: product.result.description,
          price: product.result.price,
          category_id: product.result.categories.categoryId,
          completionTime: product.result.completionTime,
          enddateWarranty: product.result.enddateWarranty,
          status_id: product.result.status.status_id,
          image: product.result.image,
          quantity: product.result.quantity
        });
        // console.log("productId la", this.editForm.value);
      });

    this.populateFormWithData(productId);

  }




  onEditSubmit(): void {
    if (this.editForm.valid) {
      const productData = this.editForm.value;
      // console.log('Form Data for Edit:', productData.product_id);

      const updatedProduct = {
        ...productData,
        // status: this.selectedStatus,
        thumbnail: this.selectedThumbnail,
        images: this.selectedImages
      };
      console.log('Form Data for updatedProduct:', updatedProduct);


      this.productListService.editProduct(updatedProduct, this.selectedThumbnail, this.selectedImages, productData.product_id)
        .subscribe(
          response => {
            console.log('Update successful', response);
            this.toastr.success('Cập nhật sản phẩm thành công!', 'Thành công');
            const closeButton = document.querySelector('.btn-mau-do[data-dismiss="modal"]') as HTMLElement;
            if (closeButton) { // Check if the button exists
              closeButton.click(); // If it exists, click it to close the modal
            }
          },
          error => {
            console.error('Update error', error);
            this.toastr.error('Cập nhật sản phẩm bị lỗi!', 'Lỗi');
          }
        );
    }
  }
  openConfirmDeleteModal(productId: number, productName: string): void {
    this.selectedProductIdCurrentDelele = productId;
    this.selectedProductNameCurrentDelele = productName;
  }
  deleteProduct() {
    this.productListService.deleteProduct(this.selectedProductIdCurrentDelele)
      .subscribe(
        response => {
          console.log('Xóa thành công', response);
          if (response.code === 1000) {
            this.toastr.success('Xóa sản phẩm thành công!', 'Thành công');
          }
          const cancelButton = document.querySelector('.btn.btn-secondary[data-dismiss="modal"]') as HTMLElement;
          if (cancelButton) { // Check if the button exists
            cancelButton.click(); // If it exists, click it to close the modal
          }

        },
        (error: HttpErrorResponse) => {
          if (error.status === 400 && error.error.code === 1030) {
            this.toastr.error(error.error.message, 'Lỗi');
          } else {
            this.toastr.error("Không thể xoá sản phẩm do sản phẩm đang được sử dụng ở các chức năng khác", 'Lỗi');
          }
          const cancelButton = document.querySelector('.btn.btn-secondary[data-dismiss="modal"]') as HTMLElement;
          if (cancelButton) { // Check if the button exists
            cancelButton.click(); // If it exists, click it to close the modal
          }
          // this.isLoading = false; // Stop the loading spinner on error
        }
      );
    console.log('productId', this.selectedProductIdCurrentDelele);
  }

  showProductDetails(productId: number) {
    this.productListService.getProductById(productId)
      .subscribe(product => {
        // Update modal content with retrieved product data

      });
  }
}