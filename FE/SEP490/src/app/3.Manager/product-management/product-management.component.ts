import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr';
import { FormArray, FormBuilder, FormGroup, Validators, FormsModule, FormControl } from '@angular/forms';
import { concatMap } from 'rxjs/operators';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { map, switchMap, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import 'jquery';

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
declare var $: any; // khai bao jquery


interface OrderForm {
  orderId: number;
  orderDate: string;
  status: number;
  totalAmount: number | null;
  specialOrder: boolean;
  paymentMethod: any; // Assuming dynamic type due to null in example
  deposite: any; // Assuming dynamic type due to null in example
  code: string;
  phoneNumber: string;
  fullname: string;
  address: string;
  city_province: string;
  district: string;
  wards: string;
  orderFinish: any; // Assuming dynamic type due to null in example
  response: string;
  description: string;
}

@Component({
  selector: 'app-product-management',
  templateUrl: './product-management.component.html',
  styleUrls: ['./product-management.component.scss']
})
export class ProductManagementComponent implements OnInit {
  uploadForm: FormGroup;
  selectedThumbnail: File | any = null;
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
  selectedSortByPrice: string = '0';
  selectedSortById: string = '';
  // thumbnail image and list image
  productImages: File[] = [];
  thumbnailImage: File | null = null;
  thumbnailPreview = '';
  imagesPreview: string[] = [];
  //
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

  totalUnitPrice: number = 0; // tinh tong unitPrice
  quantityPerSubMaterial: { [key: number]: number | string } = {};; // so luong cua tung subMaterial cua 1 san pham

  isProduct: boolean = true; // check product or product request
  isLoadding: boolean = false;   //loading when click button

  //autocomplete request
  keyword = 'code';
  requests: any[] = [];
  //

  //tao formGroup cho order
  orderForm: FormGroup;
  imagesPreviewRequest: string[] = [];


  //form danh` cho up list request product theo order
  listRequestProductForm: FormGroup;
  constructor(
    private fb: FormBuilder,
    private productListService: ProductListService,
    private toastr: ToastrService,
    private http: HttpClient,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {

    this.listRequestProductForm = this.fb.group({
      itemsRProduct: this.fb.array([])
    });


    this.uploadForm = this.fb.group({

      request_id: [],// id cua product request
      requestProductName: [''], // ten cua product request
      product_name: [''],
      description: [''],
      price: [0],
      category_id: [0],
      type: [0],
      completionTime: [''], // thoi gian hoan thanh cua product request
      quantity: [1], // quantity cho product request
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
      type: [0],
      imageList: [''],
      //danh cho product request
      re_productId: [''],
      requestProductName: [''],
      code: [''],
      request_id: ['']
      //end
    });

    this.materialForm = this.fb.group({
      items: this.fb.array([]),
    });

    this.formSubMaterialPerProduct = this.fb.group({
      itemsEdit: this.fb.array([]),
    });

    this.orderForm = this.fb.group({
      orderId: [null],
      orderDate: [''],
      status: this.fb.group({
        status_id: [null],
        status_name: ['']
      }),
      totalAmount: [null],
      specialOrder: [false],
      paymentMethod: [null],
      deposite: [null],
      // userInfor: this.fb.group({
      //   inforId: [null],
      //   phoneNumber: [''],
      //   fullname: [''],
      //   address: [''],
      //   bank_name: [''],
      //   bank_number: [''],
      //   city_province: [''],
      //   district: [''],
      //   wards: [''],
      //   has_Account: [null]
      // }),
      code: [''],
      phoneNumber: [''],
      fullname: [''],
      address: [''],
      city_province: [''],
      district: [''],
      wards: [''],
      orderFinish: [null],
      response: [''],
      description: [''],
      requestImages: this.fb.array([])
    });

  }
  //reset lai khi add lai material cho product va product request
  resetFormAdd() {
    this.totalUnitPrice = 0;
    this.materialForm.reset();
    this.items.clear();
    this.selectedMaterialId = [];
    this.subMaterials = [];
    this.unitPriceSubMaterial = {};
    this.quantityPerSubMaterial = {};
    this.imagesPreview = [];
    this.thumbnailImage = null;
    this.productImages = [];
    this.uploadForm.reset();
  }

  //phan formGroup cua add product
  //phan` danh` cho material
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
    this.onRemoveMaterial(index);
  }
  //
  //phan` danh cho request product theo order
  get itemsRProduct(): FormArray {
    return this.listRequestProductForm.get('itemsRProduct') as FormArray;
  }

  addItemRProduct(): void {
    this.itemsRProduct.push(this.fb.group({
      request_id: [this.idOrder],
      requestProductName: ['', Validators.required],
      quantity: [0, Validators.required],
      completionTime: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, Validators.required],

    }));
  }

  removeItemRProduct(index: number) {
    const items = this.listRequestProductForm.get('itemsRProduct') as FormArray;
    items.removeAt(index);
    this.onRemoveMaterial(index);
  }
  //
  //phan formGroup cua edit productt

  populateFormWithData(productId: number) {
    this.totalUnitPrice = 0;
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
            this.totalUnitPrice += materialItem.unitPrice * materialItem.quantity;
            this.quantityPerSubMaterial[index] = materialItem.quantity;
            this.unitPriceSubMaterial[index] = materialItem.unitPrice;
          });

          // console.log(this.itemsEditArray.value);

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
    this.onRemoveMaterial(index);
  }
  //
  buttonClickModalFlag: boolean = true;

  ngAfterViewChecked() {
    if (!this.isProduct && this.buttonClickModalFlag && this.idOrder) {
      const addRequestProductButton = document.getElementById('addRequestProductButton');
      if (addRequestProductButton) {
        addRequestProductButton.click();
        this.buttonClickModalFlag = false;
      }
    }
  }

  ngOnDestroy(): void {
    this.buttonClickModalFlag = true;
  }

  idOrder: number | null = null; // khai bao idOrder
  ngOnInit(): void {
    this.loginToken = localStorage.getItem('loginToken');
    this.loadCategories();
    this.loadStatus();
    this.loadMaterials();

    //khi truyen param co id order 
    this.activatedRoute.params.subscribe(params => {
      this.idOrder = params['id'];
      if (this.idOrder) {
        this.isProduct = false;

        this.materialForm.reset();
        this.unitPriceSubMaterial = {};
        this.quantityPerSubMaterial = {};
        this.orderForm.patchValue({
          product_id: null,
          product_name: null,
          description: null,
          price: null,
          category_id: null,
          image: null,
          quantity: null,
          imageList: null,
          re_productId: null,
          re_productName: null,
          code: null,
          request_id: null
        });
        this.imagesPreviewRequest = [];

        this.productListService.getOrderById(this.idOrder).subscribe(
          (response) => {
            this.orderForm.patchValue({
              orderId: this.idOrder,
              orderDate: response.result.orderDate,
              code: response.result.code,
              description: response.result.description,
              price: response.result?.price,
              completionTime: response.result?.completionTime,
              status_id: response.result.status?.status_id,
              orderFinish: response.result.orderFinish,

            });

            this.imagesPreviewRequest = response.result.requestImages.map((image: any) => {
              return image.fullPath;
            });
          },
          (error) => {
            // Xử lý lỗi ở đây
            console.error(error);
          }
        );
      }
    });


    if (this.loginToken) {
      // this.isLoadding = true;
      // console.log('Retrieved loginToken:', this.loginToken);
      if (this.isProduct == true) {
        this.productListService.getProducts().subscribe(
          (data) => {
            this.isLoadding = false;
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
      } else {
        // this.productListService.getAllProductRequest().subscribe(
        //   (data) => {
        //     this.isLoadding = false;
        //     if (data.code === 1000) {
        //       this.products = data.result;
        //       console.log('Danh sách sản phẩm theo yeu cau:', this.products);
        //     } else {
        //       console.error('Failed to fetch products:', data);
        //       this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        //     }
        //   },
        //   (error) => {
        //     console.error('Error fetching products:', error);
        //     this.toastr.error('Có lỗi xảy ra! Không thể lấy danh sách sản phẩm theo yêu cầu', 'Lỗi');
        //   }
        // );
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

  onIsProductChange(newValue: boolean) {
    this.isLoadding = true;
    this.isProduct = newValue;

    this.products.length = 0;
    if (this.isProduct == true) {
      this.productListService.getProducts().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.products = data?.result;
            console.log('Danh sách sản phẩm:', this.products);
          } else {
            console.error('Failed to fetch products:', data);
            this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
          }
          this.isLoadding = false;
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
          this.isLoadding = false;
        }
      );
    } else {
      this.productListService.getAllProductRequest().subscribe(
        (data) => {
          this.isLoadding = false;
          if (data.code === 1000) {
            this.products = data?.result;
            console.log('Danh sách sản phẩm theo yeu cau:', this.products);
          } else {
            console.error('Failed to fetch products:', data);
            this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
          }
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
          this.isLoadding = false;
        }
      );
      // lay danh sach request de autocomplete
      // this.productListService.getAllRequest().subscribe((data: any) => {
      //   if (data.code === 1000) {
      //     this.requests = data?.result;
      //   } else {
      //     this.toastr.error('Không thể lấy danh sách request!', 'Lỗi');
      //   }
      // },
      //   (error) => {
      //     console.error('Error fetching requests:', error);
      //     this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      //   });
    }
    // console.log('Giá trị mới của isProduct:', this.isProduct);
  }

  reloadProduct(): void {
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



  filterProducts(): void {
    this.isLoadding = true;
    console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory, "và giá:", this.selectedSortByPrice);

    this.productListService.getMultiFillterProductForAdmin(this.searchKey, this.selectedCategory, this.selectedStatus, this.selectedSortByPrice)
      .subscribe(
        (data) => {
          this.isLoadding = false;
          if (data.code === 1000) {
            this.products = data.result;
            console.log('Lọc sản phẩm thành công:', this.products);
            //      this.toastr.success('Lọc sản phẩm thành công!', 'Thành công');
          } else if (data.code === 1015) {
            this.products = [];
            console.error('Lọc sản phẩm không thành công:', data);
            this.toastr.error('Không tìm thấy sản phẩm phù hợp!', 'Lọc thất bại');
          }
        }
      );
  }

  filterProductsRequest(): void {
    // console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory, "và giá:", this.selectedSortByPrice);
    this.isLoadding = true;
    this.productListService.getMultiFillterRequestProductForAdmin(this.searchKey, this.selectedStatus, this.selectedSortByPrice)
      .subscribe(
        (data) => {
          this.isLoadding = false;
          if (data.code === 1000) {
            this.products = data.result;
            console.log('Lọc sản phẩm thành công:', this.products);
            this.toastr.success('Lọc sản phẩm thành công!', 'Thành công');
          } else if (data.code === 1015) {
            this.products = [];
            //    console.error('Lọc sản phẩm không thành công:', data);
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
        console.log('Sub Materials:', this.subMaterials);
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
  }

  onQuantityChange(event: Event, index: number) {
    this.quantityPerSubMaterial[index] = (event.target as HTMLInputElement).value;
    this.totalUnitPrice = 0;

    if (this.unitPriceSubMaterial && this.quantityPerSubMaterial) {
      for (const key of Object.keys(this.unitPriceSubMaterial)) {
        const index = Number(key);
        const numericUnitPrice = Number(this.unitPriceSubMaterial[index]) || 0;
        const quantity = Number(this.quantityPerSubMaterial[index]) || 0;
        const totalForThisItem = numericUnitPrice * quantity;
        this.totalUnitPrice += totalForThisItem;
      }
    }
    console.log('totalprice:', this.totalUnitPrice);

  }

  onQuantityChangeEditForm(event: Event, index: number) {
    const newQuantity = Number((event.target as HTMLInputElement).value) || 0;

    const quantityDifference = newQuantity - (Number(this.quantityPerSubMaterial[index]) || 0);

    const priceDifference = quantityDifference * (Number(this.unitPriceSubMaterial[index]) || 0);

    this.totalUnitPrice += priceDifference;

    this.quantityPerSubMaterial[index] = newQuantity;
    console.log('quantityPerSubMaterial:', this.quantityPerSubMaterial[index]);
    console.log('priceDifference:', priceDifference);

  }

  onRemoveMaterial(index: number) {
    if (this.unitPriceSubMaterial[index] && this.quantityPerSubMaterial[index]) {
      this.totalUnitPrice = this.totalUnitPrice - (Number(this.unitPriceSubMaterial[index]) * Number(this.quantityPerSubMaterial[index]));
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

  //phan xu li upload anh
  onThumbnailSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedThumbnail = file;

      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.thumbnailPreview = e.target.result;
        // console.log(this.thumbnailPreview)
      };
      reader.readAsDataURL(file);
    } else {
      this.selectedThumbnail = null;
      this.thumbnailPreview = '';
    }
  }

  onImagesSelected(event: any): void {
    this.selectedImages = Array.from(event.target.files);

    const files: File[] = Array.from(event.target.files as FileList);
    if (event.target.files && event.target.files.length) {
      // xoa list preview cu    
      this.imagesPreview = [];

      // Create and store URLs for preview
      files.forEach((file: File) => {
        const url = URL.createObjectURL(file);
        this.imagesPreview.push(url);
      });

    }
  }
  onResetThumbnail() {
    this.selectedThumbnail = null;
    this.thumbnailPreview = '';
  }

  onResetImage() {
    this.selectedImages = [];
    this.imagesPreview = [];
  }
  //

  onSubmit() {
    if (this.uploadForm.valid && this.selectedThumbnail && this.selectedImages.length) {
      this.isLoadding = true;
      const productData = this.uploadForm.value;
      // console.log(productData);
      if (productData.price) {
        productData.price = Number(productData.price.replace(/,/g, ''));
      }
      // console.log('data goc:', this.materialForm.value);
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
          this.reloadProduct();
          this.isLoadding = false;
          this.toastr.success('Tạo sản phẩm thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click();
        },
        error => {
          this.isLoadding = false;
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
      quantity: null,
      imageList: null
    });
    this.imagesPreview = [];
    this.thumbnailPreview = '';
    // console.log('Thumbnailpre:', this.thumbnailPreview);

    this.productListService.getProductById(productId)
      .subscribe(async product => {
        this.editForm.patchValue({
          product_name: product.result.productName,
          description: product.result.description,
          price: this.formatNumberWithCommas(product.result.price),
          category_id: product.result.categories.categoryId,
          completionTime: product.result.completionTime,
          enddateWarranty: product.result.enddateWarranty,
          status_id: product.result.status.status_id,
          image: product.result.image,
          quantity: product.result.quantity,
          imageList: product.result.imageList
        });
        this.thumbnailPreview = product.result.image;
        this.imagesPreview = product.result.imageList.map((image: any) => {
          return image.fullPath;
        });
        // console.log('Thumbnailpre:', product.result);
      });
    this.populateFormWithData(productId);

  }

  onEditSubmit(): void {
    if (this.editForm.valid) {
      const productData = this.editForm.value;

      if (productData.price) {
        productData.price = Number(productData.price.replace(/,/g, ''));
      }
      // console.log('Form Data for Edit:', productData.product_id);
      this.isLoadding = true;

      const updatedProduct = {
        ...productData,
        // status: this.selectedStatus,
        thumbnail: this.selectedThumbnail,
        images: this.selectedImages
      };
      console.log('Form Data for updatedProduct:', updatedProduct);

      //phan` lay thong tin submate cho san pham
      var temp = this.formSubMaterialPerProduct.value;

      // tach lay quantity va subMaterialId
      var processedData = temp.itemsEdit.map((item: MaterialItem) => (
        [(item.subMaterialId as string), item.quantity]
      ));
      // console.log("temp:", temp); 
      // console.log('processedData:', processedData);
      // convert thanh dang map
      const transformedObject: { [key: string]: number } = {};

      for (const [subMaterialId, quantity] of processedData) {
        transformedObject[subMaterialId] = quantity;
      }

      this.productListService.editProduct(updatedProduct, this.selectedThumbnail, this.selectedImages, productData.product_id)
        .pipe(
          concatMap(response => {
            console.log('Update successful', response);

            const transformedData = {
              productId: response.result.productId,
              subMaterialQuantities: transformedObject
            };
            console.log("data cua submaterial: ", transformedData);
            return this.productListService.EditSubMaterialProduct(transformedData);
          })
        )
        .subscribe(
          finalResponse => {
            this.reloadProduct();
            this.isLoadding = false;
            console.log('Sub material update successful', finalResponse);
            this.toastr.success('Cập nhật sản phẩm và vật liệu phụ thành công!', 'Thành công');
            $('[data-dismiss="modal"]').click(); // Đóng modal
          },
          error => {
            if (error.status === 400 && error.error.code === 1038) {
              this.toastr.warning(error.error.message, 'Lỗi');
            } else {
              this.toastr.error('Cập nhật sản phẩm bị lỗi!', 'Lỗi');
            }
            console.error('Update error', error);
            this.isLoadding = false;
            $('[data-dismiss="modal"]').click(); // Đóng modal
          }
        );
    }
  }


  async convertImageTobase64(imageUrl: string): Promise<string> {
    const response = await fetch(imageUrl);
    const blob = await response.blob();
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result as string);
      reader.onerror = reject;
      reader.readAsDataURL(blob);
    });
  }

  openConfirmDeleteModal(productId: number, productName: string): void {
    this.selectedProductIdCurrentDelele = productId;
    this.selectedProductNameCurrentDelele = productName;
  }

  deleteProduct() {
    this.isLoadding = true;
    this.productListService.deleteProduct(this.selectedProductIdCurrentDelele)
      .subscribe(
        response => {
          this.reloadProduct();
          console.log('Xóa thành công', response);
          if (response.code === 1000) {
            this.toastr.success('Xóa sản phẩm thành công!', 'Thành công');
          }
          $('[data-dismiss="modal"]').click();      // tat modal  
          this.isLoadding = false;

        },
        (error) => {
          if (error.status === 400 && error.error.code === 1030) {
            this.toastr.warning(error.error.message, 'Lỗi');
          } else {
            this.toastr.error("Không thể xóa sản phẩm", 'Lỗi');
          }
          $('[data-dismiss="modal"]').click();      // tat modal  
          this.isLoadding = false; // Stop the loading spinner on error
        }
      );
    // console.log('productId', this.selectedProductIdCurrentDelele);
  }


  //phan xu li product request
  onRequestIdSelected(item: any) {
    // console.log('item:', item.requestId)
    this.uploadForm.patchValue({
      request_id: item.requestId
    });
  }

  populateFormWithDataRequestProduct(productId: number) {
    this.totalUnitPrice = 0;
    this.productListService.exportMaterialProductRequestByProductId(productId).subscribe(
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
            this.totalUnitPrice += materialItem.unitPrice * materialItem.quantity;
            this.quantityPerSubMaterial[index] = materialItem.quantity;
            this.unitPriceSubMaterial[index] = materialItem.unitPrice;
          });

          // console.log(this.itemsEditArray.value);

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

  reloadProductRequest(): void {
    this.productListService.getAllProductRequest().subscribe(
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

  onSubmitProductRequest() {
    console.log('Form Data for Product Request:', this.listRequestProductForm.value);
    // if (this.uploadForm.valid && this.selectedImages.length) {
    //   this.isLoadding = true;
    //   const productRequestData = this.uploadForm.value;

    //   if (productRequestData.price) {
    //     productRequestData.price = Number(productRequestData.price.replace(/,/g, ''));
    //   }

    //   productRequestData.request_id = this.idOrder;
    //   console.log('data goc:', this.materialForm.value);
    //   var temp = this.materialForm.value;

    //   // tach lay quantity va subMaterialId
    //   var processedData = temp.items.map((item: MaterialItem) => (
    //     [(item.subMaterialId as string), item.quantity]
    //   ));
    //   // convert thanh dang map
    //   const transformedObject: { [key: string]: number } = {};

    //   for (const [subMaterialId, quantity] of processedData) {
    //     transformedObject[subMaterialId] = quantity;
    //   }

    //   this.productListService.addNewProductRequest(productRequestData, this.selectedImages)
    //     .pipe(concatMap(response => {

    //       console.log('response:', response);
    //       const transformedData = {
    //         productId: response.result.requestProductId,
    //         subMaterialQuantities: transformedObject
    //       };
    //       console.log("transformedData:", transformedData);
    //       return this.productListService.createExportMaterialProductRequest(transformedData);
    //     }))
    //     .subscribe(
    //       response => {
    //         this.location.replaceState('/product_management');
    //         this.isProduct = false;
    //         this.reloadProductRequest();
    //         this.isLoadding = false;
    //         this.toastr.success('Tạo sản phẩm theo yêu cầu thành công!', 'Thành công');
    //         $('[data-dismiss="modal"]').click();      // tat modal  
    //       },
    //       error => {
    //         this.isLoadding = false;
    //         this.toastr.error('Tạo sản phẩm bị lỗi!', 'Lỗi');

    //       }
    //     );


    // }
  }

  editProductRequest(productId: number) {
    //binding data cua orderF
    this.orderForm.patchValue({
      product_id: null,
      product_name: null,
      description: null,
      price: null,
      category_id: null,
      image: null,
      quantity: null,
      imageList: null,
      re_productId: null,
      re_productName: null,
      code: null,
      request_id: null
    });
    this.imagesPreviewRequest = [];



    //reset phan form edit product
    this.materialForm.reset();
    this.unitPriceSubMaterial = {};
    this.quantityPerSubMaterial = {};
    this.editForm.patchValue({
      product_id: productId,
      product_name: null,
      description: null,
      price: null,
      category_id: null,
      image: null,
      quantity: null,
      imageList: null,
      re_productId: null,
      re_productName: null,
      code: null,
      request_id: null
    });
    this.imagesPreview = [];
    this.thumbnailPreview = '';
    // console.log('Thumbnailpre:', this.thumbnailPreview);



    this.productListService.getRequestProductById(productId).pipe(
      tap(product => {
        // Cập nhật form sản phẩm ở đây
        this.editForm.patchValue({
          re_productId: product.result.re_productId,
          requestProductName: product.result.re_productName,
          code: product.result.code,
          request_id: product.result.request_id,
          description: product.result.description,
          price: this.formatNumberWithCommas(product.result.price),
          completionTime: product.result.completionTime,
          enddateWarranty: product.result?.enddateWarranty,
          status_id: product.result.status?.status_id,
          quantity: product.result.quantity,
          imageList: product.result.imageList,

        });
        // Cập nhật imagesPreview ở đây
        this.imagesPreview = product.result.imageList.map((image: any) => image.fullPath);
      }),
      concatMap(product => this.productListService.getOrderById(product.result.request_id))
    ).subscribe(
      response => {
        // Cập nhật form đơn hàng ở đây
        this.orderForm.patchValue({
          orderId: this.idOrder,
          orderDate: response.result.orderDate,
          code: response.result.code,
          description: response.result.description,
          price: response.result?.price,
          completionTime: response.result?.completionTime,
          status_id: response.result.status?.status_id,
          orderFinish: response.result.orderFinish,

        });
        // Cập nhật imagesPreviewRequest ở đây
        this.imagesPreviewRequest = response.result.requestImages.map((image: any) => image.fullPath);
      },
      error => {
        // Xử lý lỗi ở đây
        console.error(error);
      }
    );

    this.populateFormWithDataRequestProduct(productId);

  }

  onEditRequestProductSubmit(): void {
    // if (this.editForm.valid) {
    const productData = this.editForm.value;

    if (productData.price) {
      productData.price = Number(productData.price.replace(/,/g, ''));
    }
    this.isLoadding = true;

    const updatedProductRequest = {
      ...productData,
      // status: this.selectedStatus,
      // images: this.selectedImages
    };

    //phan` lay thong tin submate cho san pham
    var temp = this.formSubMaterialPerProduct.value;

    // tach lay quantity va subMaterialId
    var processedData = temp.itemsEdit.map((item: MaterialItem) => (
      [(item.subMaterialId as string), item.quantity]
    ));
    // console.log("temp:", temp); 
    // console.log('processedData:', processedData);
    // convert thanh dang map
    const transformedObject: { [key: string]: number } = {};

    for (const [subMaterialId, quantity] of processedData) {
      transformedObject[subMaterialId] = quantity;
    }

    this.productListService.editProductRequest(updatedProductRequest, this.selectedImages, productData.product_id)
      .pipe(
        concatMap(response => {
          console.log('Update successful', response);

          const transformedData = {
            productId: response.result.requestProductId,
            subMaterialQuantities: transformedObject
          };
          console.log("data cua submaterial: 0", transformedData);
          return this.productListService.EditSubMaterialRequestProduct(transformedData);
        })
      )
      .subscribe(
        finalResponse => {
          this.reloadProductRequest();
          this.isLoadding = false;
          console.log('Sub material update successful', finalResponse);
          this.toastr.success('Cập nhật sản phẩm và vật liệu phụ thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click(); // Đóng modal
        },
        error => {
          console.error('Update error', error);
          this.toastr.error('Cập nhật sản phẩm bị lỗi!', 'Lỗi');
          this.isLoadding = false;
        }
      );

  }

  deleteProductRequest() {
    this.isLoadding = true;
    console.log('productId123', this.selectedProductIdCurrentDelele);
    this.productListService.deleteProductRequest(this.selectedProductIdCurrentDelele)
      .subscribe(
        response => {
          this.reloadProductRequest();
          // console.log('Xóa thành công', response);
          if (response.code === 1000) {
            this.toastr.success('Xóa sản phẩm thành công!', 'Thành công');
          }
          $('[data-dismiss="modal"]').click();      // tat modal  

          this.isLoadding = false;

        },
        (error) => {
          if (error.status === 400 && error.error.code === 1030) {
            this.toastr.warning(error.error.message, 'Lỗi');
          } else {
            this.toastr.error("Không thể xóa sản phẩm", 'Lỗi');
          }
          $('[data-dismiss="modal"]').click();      // tat modal  

          this.isLoadding = false; // Stop the loading spinner on error
        }
      );
    // console.log('productId', this.selectedProductIdCurrentDelele);
  }


  //danh cho format input gia tien
  formatInputValue(event: any) {
    let value = event.target.value.replace(/,/g, ''); // Remove existing commas
    const formattedValue = value.replace(/\B(?=(\d{3})+(?!\d))/g, ','); // Add commas
    event.target.value = formattedValue;
  }
  //format gia tien
  formatNumberWithCommas(x: number): string {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }
}