import { Component, OnInit, ViewChild, ElementRef, ChangeDetectorRef } from '@angular/core';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { ToastrService } from 'ngx-toastr';
import { FormArray, FormBuilder, FormGroup, Validators, FormsModule, FormControl } from '@angular/forms';
import { catchError, concatMap } from 'rxjs/operators';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { map, switchMap, tap } from 'rxjs/operators';
import { EMPTY, Observable, of, throwError } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import 'jquery';
import { error, get } from 'jquery';
import * as e from 'cors';

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
  code: string;
  input_id: number;
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
  input_id: number;
  code: string;
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
    private location: Location,
    // private cdRef: ChangeDetectorRef
  ) {

    this.listRequestProductForm = this.fb.group({
      itemsRProduct: this.fb.array([]),
      // materialFormRequests: this.fb.array([])
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
      code: [''],
      orderFinish: [null],
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

  async removeItem(index: number) {
    // const items = this.materialForm.get('items') as FormArray;
    // items.removeAt(index);

    this.subMaterialDataArray = []; // reset lai mang subMaterialDataArray
    if (this.items.length - 1 === 0) {
      this.toastr.error('Không thể xóa hết nguyên liệu!', 'Lỗi');
      return;
    }

    if (this.items && this.items.length > index) {

      this.onRemoveMaterial(index); // cap nhat gia tien` uoc tinh

      // Shift the elements to the left 
      if (index < this.items.length - 1) {
        for (let i = index, j = 0; i < this.items.length - 1; i++, j++) {
          this.items.at(i).setValue(this.items.at(i + 1).value);
          this.selectedMaterialId[i] = this.selectedMaterialId[i + 1];
          await this.onMaterialChangeFirstEdit(Number(this.items.at(i).value.materialId), i);
          await this.delay(1000);
          this.selectedSubMaterialId[i] = this.selectedMaterialId[i + 1];
          this.unitPriceSubMaterial[i] = this.unitPriceSubMaterial[i + 1];
          this.quantityPerSubMaterial[i] = this.quantityPerSubMaterial[i + 1];
          this.subMaterialDataArray[j] = this.items.at(i).value;

          // do chua binding data price nen phai set value cho price
          this.subMaterialDataArray[j].price = this.unitPriceSubMaterial[i];
        }
      }

      // Remove the last element
      while (this.items.length > index && this.items.length > 0) {
        this.items.removeAt(this.items.length - 1);
        this.selectedMaterialId[this.items.length] = '';
      }

      for (let i = 0; i < this.subMaterialDataArray.length; i++) {
        const subMaterialData = this.subMaterialDataArray[i];
        if (subMaterialData) {
          const itemFormGroup = this.fb.group({
            materialId: [subMaterialData.materialId],
            subMaterialId: [subMaterialData.subMaterialId],
            // subMaterialName: [subMaterialData.subMaterialName],
            // materialType: [subMaterialData.materialType],
            quantity: [subMaterialData.quantity],
            price: [subMaterialData.price]
          });
          this.items.push(itemFormGroup);
          console.log('subMaterialDataArray:', this.subMaterialData);
        } else {
          console.error(`subMaterialDataArray[${i}] is undefined`);
        }
      }
      console.log('subMaterialDataArray:', this.subMaterialDataArray);


    }
  }
  //
  //phan material cho request product theo order
  get ItemsMaterialRequest(): FormArray {
    return (this.listRequestProductForm.get(['materialFormRequests']) as FormArray);

  }

  get MaterialFormRequests(): FormArray {
    return this.listRequestProductForm.get('materialFormRequests') as FormArray;
  }

  // Phương thức để thêm một item mới vào FormArray 'items'

  getMaterialFormRequests(index: number): FormArray {
    // Access the FormGroup at the given index within the itemsRProduct FormArray
    const itemFormGroup = this.itemsRProduct.at(index) as FormGroup; // Cast to FormGroup here
    // console.log(`itemFormGroup at index ${index}:`, itemFormGroup);

    // Access the materialFormRequests FormArray within the obtained FormGroup
    return itemFormGroup.get('materialFormRequests') as FormArray;
  }



  addMaterialFormRequest(index: number) {
    const materialFormRequests = this.getMaterialFormRequests(index);
    // console.log("materialFormRequestcuaAdd" + index, materialFormRequests);
    // console.log('materialFormRequests111111:', materialFormRequests);
    // console.log('materialFormRequests22222:', this.MaterialFormRequests);
    // Create form controls for materialId, quantity, and price

    // const materialIdControl = new FormControl(null);
    // const quantityControl = new FormControl(1);
    // const priceControl = new FormControl(0);

    const newRequest = this.fb.group({
      materialId: [null],
      submateId: [null],
      quantity: [null],
    });
    materialFormRequests.push(newRequest);
  }

  subMaterialDataArrayRProduct: any[][] = [];
  removeMaterialFormRequest(sectionIndex: number, formIndex: number) {

    this.subMaterialDataArrayRProduct[sectionIndex] = [];
    const materialFormRequests = this.getMaterialFormRequests(sectionIndex);
    // if (materialFormRequests && formIndex >= 0 && formIndex < materialFormRequests.length) {
    //   materialFormRequests.removeAt(formIndex);
    // }
    const numericUnitPrice = Number(this.unitPriceSubMaterial[sectionIndex * 10 + formIndex]) || 0;
    const quantity = Number(this.quantityPerSubMaterial[sectionIndex * 10 + formIndex]) || 0;
    const totalForThisItem = numericUnitPrice * quantity;
    // console.log("submate", this.selectedSubMaterialId);
    //tru tong gia uoc tinh cua san pham neu xoa 1 material
    this.totalPriceSubmatePerProducRequest[sectionIndex] = this.totalPriceSubmatePerProducRequest[sectionIndex] - totalForThisItem;

    // Shift the elements to the left 
    if (formIndex < materialFormRequests.length - 1) {
      for (let i = formIndex, j = 0; i < materialFormRequests.length - 1; i++, j++) {
        this.subMaterialDataArrayRProduct[sectionIndex][j] = {}; // reset value cua subMaterialDataArrayRProduct
        materialFormRequests.at(i).setValue(materialFormRequests.at(i + 1).value);
        this.selectedMaterialId[sectionIndex * 10 + i] = this.selectedMaterialId[sectionIndex * 10 + i + 1];
        delete this.selectedMaterialId[sectionIndex * 10 + i + 1];

        this.onMaterialChangeFirstEdit(Number(this.selectedMaterialId[sectionIndex * 10 + i]), sectionIndex * 10 + i);

        this.selectedSubMaterialId[sectionIndex * 10 + i] = Number(this.selectedSubMaterialId[sectionIndex * 10 + i + 1]);
        delete this.selectedSubMaterialId[sectionIndex * 10 + i + 1];
        this.unitPriceSubMaterial[sectionIndex * 10 + i] = Number(this.unitPriceSubMaterial[sectionIndex * 10 + i + 1]);
        delete this.unitPriceSubMaterial[sectionIndex * 10 + i + 1];
        this.quantityPerSubMaterial[sectionIndex * 10 + i] = Number(this.quantityPerSubMaterial[sectionIndex * 10 + i + 1]);
        delete this.quantityPerSubMaterial[sectionIndex * 10 + i + 1];

        this.subMaterialDataArrayRProduct[sectionIndex][j].materialId = Number(this.selectedMaterialId[sectionIndex * 10 + i]);
        this.subMaterialDataArrayRProduct[sectionIndex][j].submateId = Number(this.selectedSubMaterialId[sectionIndex * 10 + i]); //gan gia tri cho subMaterialDataArrayRProduct
        this.subMaterialDataArrayRProduct[sectionIndex][j].unitPriceSubMaterial = Number(this.unitPriceSubMaterial[sectionIndex * 10 + i]);
        this.subMaterialDataArrayRProduct[sectionIndex][j].quantity = Number(this.quantityPerSubMaterial[sectionIndex * 10 + i]);
      }
      // console.log('this.selectedSubMaterialId:', this.selectedSubMaterialId);
    }

    // console.log('subMaterialDataArrayRProduct:', this.subMaterialDataArrayRProduct);
    // console.log('test: ', this.subMaterialDataArrayRProduct[0]);
    // Remove the last element
    while (materialFormRequests.length > formIndex && materialFormRequests.length > 0) {
      materialFormRequests.removeAt(materialFormRequests.length - 1);
      this.selectedMaterialId[materialFormRequests.length] = '';
    }

    for (let i = 0; i < this.subMaterialDataArrayRProduct[sectionIndex].length; i++) {
      const subMaterialData = this.subMaterialDataArrayRProduct[sectionIndex][i];
      if (subMaterialData) {

        const itemFormGroup = this.fb.group({
          materialId: [subMaterialData.materialId],
          submateId: [subMaterialData.submateId],
          // subMaterialName: [subMaterialData.subMaterialName],
          // materialType: [subMaterialData.materialType],
          quantity: [subMaterialData.quantity],
        });
        materialFormRequests.push(itemFormGroup);
        console.log('subMaterialData:', subMaterialData);
      } else {
        console.error(`subMaterialDataArrayRProduct[${sectionIndex}] is undefined`);
      }
    }


  }
  //phan` danh cho request product theo order
  get itemsRProduct(): FormArray {
    return this.listRequestProductForm.get('itemsRProduct') as FormArray;
  }

  addItemRProduct(): void {

    const itemRProductForm = this.fb.group({
      request_id: [this.idOrder],
      requestProductName: ['', Validators.required],
      quantity: [0, Validators.required],
      completionTime: ['', Validators.required],
      description: ['', Validators.required],
      price: [0, Validators.required],
      materialFormRequests: this.fb.array([]) // Ensure this is initialized
    });

    this.itemsRProduct.push(itemRProductForm);

  }

  removeItemRProduct(index: number) {
    const items = this.listRequestProductForm.get('itemsRProduct') as FormArray;
    items.removeAt(index);
    this.onRemoveMaterial(index);
    if (index > -1) {

      const keys = Object.keys(this.pricePerProduct).map(Number);
      for (let i = index; i < keys.length; i++) {
        this.pricePerProduct[i] = this.pricePerProduct[i + 1];
        this.quantityRProductValue[i] = this.quantityRProductValue[i + 1];
        this.pricePerProductAndQuantity[i] = this.pricePerProductAndQuantity[i + 1];

        delete this.quantityRProductValue[keys.length - 1];
        delete this.pricePerProduct[keys.length - 1];
        delete this.pricePerProductAndQuantity[keys.length - 1];
      }


      console.log('pricePerProductAndQuantity:', this.pricePerProductAndQuantity);
    }
    this.calculateTotalPriceOfOrder(index);
    // this.cdRef.detectChanges();
  }
  //
  //phan formGroup cua edit productt

  populateFormWithData(productId: number) {
    this.totalUnitPrice = 0;
    this.subMaterialData = [];
    this.selectedSubMaterialId = [];
    this.subMaterials = [];
    // this.materials = [];
    // this.loadMaterials();
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
          this.processSubMaterials();



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

  async onMaterialChangeFirstEdit(materialId: number, index: number) {
    this.loadSubMaterials(materialId, index);
  }

  get itemsEditArray(): FormArray {
    return this.formSubMaterialPerProduct.get('itemsEdit') as FormArray;
  }

  fillMaterialItemEdit(material: SubMaterialItemOfProduct) {
    const itemFormGroup = this.fb.group({
      materialId: [material.materialId],
      subMaterialId: [material.input_id],
      subMaterialName: [material.subMaterialName],
      materialType: [material.materialType],
      quantity: [material.quantity],
      unitPrice: [material.unitPrice],
      code: [material.code]
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

  delay(ms: number) {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
  subMaterialDataArray: any[] = [];
  async removeItemEdit(index: number) {
    this.subMaterialDataArray = []; // reset lai mang subMaterialDataArray
    if (this.itemsEditArray.length === 1) {
      this.toastr.error('Không thể xóa hết nguyên liệu!', 'Lỗi');
      return;
    }

    if (this.itemsEditArray && this.itemsEditArray.length > index) {
      await this.onRemoveMaterial(index); // cap nhat gia tien` uoc tinh

      // Shift the elements to the left 
      if (index < this.itemsEditArray.length - 1) {
        for (let i = index, j = 0; i < this.itemsEditArray.length - 1; i++, j++) {
          this.itemsEditArray.at(i).setValue(this.itemsEditArray.at(i + 1).value);
          this.selectedMaterialId[i] = this.selectedMaterialId[i + 1];
          await this.onMaterialChangeFirstEdit(Number(this.itemsEditArray.at(i).value.materialId), i);
          await this.delay(1000);
          this.selectedSubMaterialId[i] = this.selectedMaterialId[i + 1];
          this.unitPriceSubMaterial[i] = this.unitPriceSubMaterial[i + 1];
          this.quantityPerSubMaterial[i] = this.quantityPerSubMaterial[i + 1];
          this.subMaterialDataArray[j] = this.itemsEditArray.at(i).value;
        }
      }

      // Remove the last element
      while (this.itemsEditArray.length > index && this.itemsEditArray.length > 0) {
        this.itemsEditArray.removeAt(this.itemsEditArray.length - 1);
        this.selectedMaterialId[this.itemsEditArray.length] = '';
      }

      console.log('subMaterialDataArray:', this.subMaterialDataArray);

      for (let i = 0; i < this.subMaterialDataArray.length; i++) {
        const subMaterialData = this.subMaterialDataArray[i];
        if (subMaterialData) {
          const itemFormGroup = this.fb.group({
            materialId: [subMaterialData.materialId],
            subMaterialId: [subMaterialData.subMaterialId],
            subMaterialName: [subMaterialData.subMaterialName],
            materialType: [subMaterialData.materialType],
            quantity: [subMaterialData.quantity],
            unitPrice: [subMaterialData.unitPrice],
            code: [subMaterialData?.code]
          });
          this.itemsEditArray.push(itemFormGroup);
        } else {
          console.error(`subMaterialDataArray[${i}] is undefined`);
        }
      }
    }
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
    this.getSpecialOrder();

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

            const orderDate = new Date(response.result.orderDate).toISOString().split('T')[0]; // Lấy ngày tháng năm
            const orderFinish = response.result.contractDate ? new Date(response.result.contractDate).toISOString().split('T')[0] : null; // Lấy ngày tháng năm của contractDate hoặc null nếu không có
            this.orderForm.patchValue({
              orderId: this.idOrder,
              orderDate: orderDate,
              code: response.result.code,
              description: response.result.description,
              price: response.result?.price,
              completionTime: response.result?.completionTime,
              status_id: response.result.status?.status_id,
              orderFinish: orderFinish,

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
      this.isLoadding = true;
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
            this.isLoadding = false;
            console.error('Error fetching products:', error);
            this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
          }
        );
      } else {
        this.productListService.getAllProductRequest().subscribe(
          (data) => {
            this.isLoadding = false;
            if (data.code === 1000) {
              this.products = data.result;
              console.log('Danh sách sản phẩm theo yeu cau:', this.products);
            } else {
              console.error('Failed to fetch products:', data);
              this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
            }
          },
          (error) => {
            this.isLoadding = false;
            console.error('Error fetching products:', error);
            this.toastr.error('Có lỗi xảy ra! Không thể lấy danh sách sản phẩm theo yêu cầu', 'Lỗi');
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
    this.searchKey = this.searchKey.trim();
    console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory, "và giá:", this.selectedSortByPrice);

    this.productListService.getMultiFillterProductForAdmin(this.searchKey, this.selectedCategory, this.selectedStatus, this.selectedSortByPrice)
      .subscribe(
        (data) => {
          this.isLoadding = false;
          if (data.code === 1000) {
            this.currentPage = 1;
            this.products = data.result;
            console.log('Lọc sản phẩm thành công:', this.products);
            //      this.toastr.success('Lọc sản phẩm thành công!', 'Thành công');
          } else if (data.code === 1015) {
            this.products = [];
            console.error('Lọc sản phẩm không thành công:', data);
            this.toastr.warning('Không tìm thấy sản phẩm phù hợp!', 'Lọc thất bại');
          }
        }
      );
  }

  filterProductsRequest(): void {
    this.searchKey = this.searchKey.trim();
    // console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory, "và giá:", this.selectedSortByPrice);
    this.isLoadding = true;
    this.productListService.getMultiFillterRequestProductForAdmin(this.searchKey, this.selectedStatus, this.selectedSortByPrice)
      .subscribe(
        (data) => {
          this.isLoadding = false;
          if (data.code === 1000) {
            this.currentPage = 1;
            this.products = data.result;
            console.log('Lọc sản phẩm thành công:', this.products);
            this.toastr.success('Lọc sản phẩm thành công!', 'Thành công');
          } else if (data.code === 1015) {
            this.products = [];
            //    console.error('Lọc sản phẩm không thành công:', data);
            this.toastr.warning('Không tìm thấy sản phẩm phù hợp!', 'Lọc thất bại');
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
    const materialId = Number((event.target as HTMLSelectElement).value);    // Gọi hàm để tải sub-materials dựa trên giá trị được chọn
    const selectedMaterial = this.materials.find(material => material.materialId === materialId);
    this.materialType[index] = selectedMaterial ? selectedMaterial.type : '';

    this.loadSubMaterials(materialId, index);
    console.log('Selected MaterialId:', materialId, " selected index:" + index);
    this.selectedMaterialId[index] = (event.target as HTMLSelectElement).value; // bo sung cho add list productrequest
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
    const selectedValue = Number((event.target as HTMLSelectElement).value);
    // Check for duplicates
    // if (Object.values(this.selectedSubMaterialId).includes(selectedValue)) {
    //   this.toastr.warning('Nguyên vật liệu đã được chọn. Vui lòng chọn nguyên vật liệu khác', 'Lỗi');
    //   this.selectedSubMaterialId[index] = null;
    //   return;
    console.log('selectedValue:', selectedValue);
    this.totalUnitPrice = 0;
    this.selectedSubMaterialId[index] = Number((event.target as HTMLSelectElement).value);
    const selectedSubMaterial = this.subMaterials[index].find(subMaterial => subMaterial.input_id === selectedValue);
    this.unitPriceSubMaterial[index] = selectedSubMaterial ? selectedSubMaterial.unitPrice : '';

    //tinh lai totalUnitPrice(tong gia uoc tinh nguyen vat lieu)
    if (this.unitPriceSubMaterial && this.quantityPerSubMaterial) {
      for (const key of Object.keys(this.unitPriceSubMaterial)) {
        const index = Number(key);
        const numericUnitPrice = Number(this.unitPriceSubMaterial[index]) || 0;
        const quantity = Number(this.quantityPerSubMaterial[index]) || 0;
        const totalForThisItem = numericUnitPrice * quantity;
        this.totalUnitPrice += totalForThisItem;
      }
    }
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

  quantityRProductValue: { [key: number]: number } = {};
  updateQuantityRProduct(event: any, index: number) {
    this.quantityRProductValue[index] = event.target.value;
    this.calculateTotalPriceOfOrder(index);
    // console.log("goiham`calculateTotalPriceOfOrder", index);
  }

  onSubMaterialChangeRProduct(event: Event, index: number, indexOfItemRProduct: number, indexOfMaterial: number) {
    const selectedValue = Number((event.target as HTMLSelectElement).value);
    // Check for duplicates
    const startIndex1 = indexOfItemRProduct * 10;
    const endIndex1 = startIndex1 + 9;

    // Check for duplicates within the specified range
    // for (let i = startIndex1; i <= endIndex1; i++) {
    //   if (this.selectedSubMaterialId[i] && Object.values(this.selectedSubMaterialId[i]).includes(selectedValue)) {
    //     this.toastr.warning('Nguyên vật liệu đã được chọn. Vui lòng chọn nguyên vật liệu khác', 'Lỗi');
    //     this.selectedSubMaterialId[index] = null;
    //     return;
    //   }
    // }

    this.totalUnitPrice = 0;
    this.selectedSubMaterialId[index] = Number((event.target as HTMLSelectElement).value);
    const selectedSubMaterial = this.subMaterials[index].find(subMaterial => subMaterial.input_id === selectedValue);
    this.unitPriceSubMaterial[index] = selectedSubMaterial ? selectedSubMaterial.unitPrice : '';

    this.totalPriceSubmatePerProducRequest[indexOfItemRProduct] = 0;
    if (this.unitPriceSubMaterial && this.quantityPerSubMaterial) {
      // Calculate the start and end index based on indexOfItemRProduct and indexOfMaterial
      const startIndex = indexOfItemRProduct * 10;
      const endIndex = startIndex + indexOfMaterial;

      // Iterate from startIndex to endIndex
      for (let index = startIndex; index <= endIndex; index++) {

        const numericUnitPrice = Number(this.unitPriceSubMaterial[index]) || 0;
        const quantity = Number(this.quantityPerSubMaterial[index]) || 0;
        const totalForThisItem = numericUnitPrice * quantity;
        console.log('totalForThisItem:' + index, totalForThisItem);

        this.totalPriceSubmatePerProducRequest[indexOfItemRProduct] += totalForThisItem;
        // console.log('totalPriceSubmatePerProducRequest123:'+index, this.totalPriceSubmatePerProducRequest[indexOfItemRProduct]);
      }
    }
  }

  totalPriceSubmatePerProducRequest: { [key: number]: number | number } = {}; // tinh tong gia cua tung san pham (chua nhan voi quantity)
  onQuantityChangeSubMaterialRProduct(event: Event, index: number, indexOfItemRProduct: number, indexOfMaterial: number) {
    this.quantityPerSubMaterial[index] = (event.target as HTMLInputElement).value;
    this.totalPriceSubmatePerProducRequest[indexOfItemRProduct] = 0;
    if (this.unitPriceSubMaterial && this.quantityPerSubMaterial) {
      // Calculate the start and end index based on indexOfItemRProduct and indexOfMaterial
      const startIndex = indexOfItemRProduct * 10;
      const endIndex = startIndex + indexOfMaterial;

      // Iterate from startIndex to endIndex
      for (let index = startIndex; index <= endIndex; index++) {

        const numericUnitPrice = Number(this.unitPriceSubMaterial[index]) || 0;
        const quantity = Number(this.quantityPerSubMaterial[index]) || 0;
        const totalForThisItem = numericUnitPrice * quantity;
        console.log('totalForThisItem:' + index, totalForThisItem);

        this.totalPriceSubmatePerProducRequest[indexOfItemRProduct] += totalForThisItem;
        // console.log('totalPriceSubmatePerProducRequest123:'+index, this.totalPriceSubmatePerProducRequest[indexOfItemRProduct]);
      }
    }

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

  async onRemoveMaterial(index: number) {
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
    const fileInput = document.getElementById('thumbnailImage') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  onResetImage() {
    this.selectedImages = [];
    this.imagesPreview = [];
    this.selectedImagesRProduct = [];
    this.imagesPreviewRProduct = [];
    const fileInput1 = document.getElementById('productImages') as HTMLInputElement;
    if (fileInput1) {
      fileInput1.value = '';
    }
  }
  //

  onSubmit() {



    if (parseFloat(this.uploadForm.get('price')?.value.replace(/,/g, '')) < this.uploadForm.get('quantity')?.value * this.totalUnitPrice) {
      this.toastr.error('Giá sản phẩm phải lớn hơn giá vật liệu !', 'Lỗi');
      return;
    }

    //validate san pham uoc tinh can du 3 loai nguyen vat lieu
    var allMaterialIds: string[] = [];
    for (let j = 0; j <= 9; j++) {
      if (this.selectedMaterialId[j]) {
        allMaterialIds.push(this.selectedMaterialId[j]);
      }
    }
    const requiredIds = [1, 2, 3];
    const hasAllRequiredIds = requiredIds.every(id => allMaterialIds.includes(id.toString()));
    if (!hasAllRequiredIds) {
      this.toastr.warning('Sản phẩm ước tính của sản phẩm cần đủ 3 nguyên vật liệu Gỗ, Giấy Nhám, Sơn', 'Lỗi');
      return;
    }

    if (this.uploadForm.valid && this.selectedThumbnail && this.selectedImages.length && this.uploadForm.get('category_id')?.value) {
      this.isLoadding = true;
      const productData = this.uploadForm.value;
      // console.log(productData);
      if (productData.price) {
        productData.price = Number(productData.price.replace(/,/g, ''));
      }
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
          return this.productListService.createExportMaterialProduct(transformedData,);
        })
      ).subscribe(
        response => {
          this.reloadProduct();
          this.isLoadding = false;
          this.toastr.success('Tạo sản phẩm thành công!', 'Thành công');
          $('[data-dismiss="modal"]').click();
          this.uploadForm.reset();
          this.onResetThumbnail();
          this.onResetImage();
        },
        error => {
          this.isLoadding = false;
          if (error.error.status === 400 && error.error.code !== 1004 && error.error.message) {
            this.toastr.error(error.error.message, 'Lỗi');
          } else {
            this.toastr.error('Tạo sản phẩm bị lỗi!', 'Lỗi');
          }
        }
      );
    } else {
      this.toastr.warning('Vui lòng điền đầy đủ thông tin!', 'Lỗi');
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

    if (parseFloat(this.editForm.get('price')?.value.replace(/,/g, '')) < this.totalUnitPrice) {
      this.toastr.error('Giá sản phẩm phải lớn hơn giá vật liệu !', 'Lỗi');
      return;
    }

    if (this.editForm.get('price')?.value && this.editForm.get('category_id')?.value) {
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
          }),
          // catchError(error => {
          //   this.isLoadding = false;
          //   if (error.status === 400 && error.error.code === 1018) {
          //     this.toastr.error(error.error.message, 'Lỗi');
          //   }
          //   return EMPTY;
          // })
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
            this.reloadProduct();
            console.error('Error during EditSubMaterialProduct:', error);
            if (error.status === 400 && error.error.code === 1038) {
              this.toastr.warning(error.error.message, 'Lỗi');
            } else if (error.status === 400 && error.error.code === 1048) {
              this.toastr.warning(error.error.message, 'Lỗi');
            }
            else if (error.status === 400 && error.error.code === 1018) {
              this.toastr.warning(error.error.message, 'Lỗi');
            }
            else if (error.status === 400 && error.error.code === 1050) {
              this.toastr.warning(error.error.message, 'Lỗi');
            }
            else if (error.status === 400 && error.error.message && error.error.code !== 1004) {
              this.toastr.error(error.error.message, 'Lỗi');
            } else {
              this.toastr.error('Cập nhật ước tính nguyên vật liệu sản phẩm bị lỗi!', 'Lỗi');
            }
            this.isLoadding = false;
            $('[data-dismiss="modal"]').click(); // Đóng modal
          });
    } else {
      this.toastr.warning('Có lỗi xảy ra vui lòng thử lại', 'Lỗi');
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
    this.subMaterialData = [];
    this.selectedSubMaterialId = [];
    this.subMaterials = [];
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
          this.processSubMaterials();

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

  async processSubMaterials() {
    console.log('subMaterialData:', this.subMaterialData);

    for (let index = 0; index < this.subMaterialData.length; index++) {
      const materialItem = this.subMaterialData[index];
      this.selectedMaterialId[index] = materialItem.materialId;
      this.selectedSubMaterialId[index] = materialItem.subMaterialId;

      console.log('selectedMaterialId:', this.selectedMaterialId[index]);

      try {
        const data: any = await this.productListService.getAllSubMaterialByMaterialIdProduct(Number(this.selectedMaterialId[index])).toPromise();
        this.subMaterials[index] = data?.result;
        console.log('Sub Materials:' + index, this.subMaterials[index]);

        for (let i = 0; i < this.subMaterials[index].length; i++) {
          if (this.subMaterials[index][i].code === materialItem.code) {
            materialItem.input_id = this.subMaterials[index][i].input_id;
            console.log('input_id:213', materialItem.input_id);
            break;
          }
        }

        this.fillMaterialItemEdit(materialItem);
      } catch (error) {
        console.error('Error fetching sub materials:', error);
      }

      this.totalUnitPrice += materialItem.unitPrice * materialItem.quantity;
      this.quantityPerSubMaterial[index] = materialItem.quantity;
      this.unitPriceSubMaterial[index] = materialItem.unitPrice;
    }

    console.log('All sub material data processed');
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

  selectedImagesRProduct: { [key: number]: File[] } = {};
  imagesPreviewRProduct: { [key: number]: string[] } = {}; // Specify as an array of strings

  onImagesRProductSelected(event: any, index: number): void {
    this.selectedImagesRProduct[index] = Array.from(event.target.files);

    const files: File[] = Array.from(event.target.files as FileList);
    if (event.target.files && event.target.files.length) {
      // Reset the preview for the current index
      this.imagesPreviewRProduct[index] = []; // Now correctly typed as an array of strings

      files.forEach((file: File) => {
        const url = URL.createObjectURL(file);
        this.imagesPreviewRProduct[index].push(url); // No error since url is a string
      });
    }
  }

  convertImageToBase64RProduct(imageFile: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.onload = (event) => {
        // Kiểm tra event.target không phải là null trước khi truy cập result
        if (event.target) {
          resolve(event.target.result as string);
        } else {
          reject(new Error('FileReader event target is null'));
        }
      };
      fileReader.onerror = (error) => {
        reject(error);
      };
      fileReader.readAsDataURL(imageFile);
    });
  }

  convertListImagesToBase64(files: File[]): Promise<string[]> {
    return Promise.all(files.map(file => this.convertImageToBase64RProduct(file)));
  }

  totalAmountOfOrder: number = 0;
  onSubmitProductRequest() {
    const currentDate = new Date();

    // console.log('materialIds:', materialIds);
    this.pricePerProductAndQuantity = []; //reset lai gia tri

    for (let i = 0; i < this.itemsRProduct.length; i++) {

      


      //validate ngay` uoc tinh hoan thanh`
      const completionTime = this.listRequestProductForm.value.itemsRProduct[i].completionTime;
      const completionTimeDate = new Date(completionTime);

      if (completionTimeDate < currentDate) {
        this.toastr.error('Ngày hoàn thành không được nhỏ hơn ngày hiện tại!', 'Lỗi');
        return;
      }

//validate san pham uoc tinh can du 3 loai nguyen vat lieu
var allMaterialIds: string[] = [];
for (let j = i * 10; j <= i * 10 + 9; j++) {
  if (this.selectedMaterialId[j]) {
    allMaterialIds.push(this.selectedMaterialId[j]);
  }
}
const requiredIds = [1, 2, 3];
const hasAllRequiredIds = requiredIds.every(id => allMaterialIds.includes(id.toString()));
if (!hasAllRequiredIds) {
  this.toastr.warning('Sản phẩm ước tính của sản phẩm ' + (i + 1) + ' cần đủ 3 nguyên vật liệu Gỗ, Giấy Nhám, Sơn', 'Lỗi');
  return;
}
console.log('allMaterialIds:', allMaterialIds);
      const materialFormRequests = this.getMaterialFormRequests(i);
      // console.log("do dai cua j:" ,materialFormRequests.length)
      // console.log("do dai cua i: ", i)
      // console.log("in materialFormRequests:"+i, materialFormRequests);

      // Duyệt qua mỗi request product hiện có và cập nhật giá trị
      for (let j = 0; j < materialFormRequests.length; j++) {
        // const materialIdValue = this.selectedMaterialId[i * 10 + j];
        // console.log("Giá trị materialId:"+ (i*10+j), materialIdValue);
        const request = materialFormRequests.at(j);
        // console.log("request:"+j, request);
        request.patchValue({
          materialId: this.selectedMaterialId[i * 10 + j], // Giá trị mới cho materialId
          submateId: this.selectedSubMaterialId[i * 10 + j], // Giá trị mới cho submateId
          quantity: this.quantityPerSubMaterial[i * 10 + j], // Giá trị mới cho quantity
        });
      }

      if (this.pricePerProduct[i] < this.totalPriceSubmatePerProducRequest[i]) {
        this.toastr.warning('Đơn giá sản phẩm ' + (i + 1) + ' không được nhỏ hơn giá vật liệu!', 'Lỗi');
        return;
      }
      // this.totalAmountOfOrder += this.totalPriceSubmatePerProducRequest[i];
    }
    // console.log('Form Data for Product Request:', this.listRequestProductForm.value);
    //bien convert submate sang dang thich hop de call api
    const transformedDataSubMate = this.listRequestProductForm.value.itemsRProduct.map((item: any) => ({
      productId: 0, // Assuming productId is not available in original data and set to 0
      subMaterialQuantities: item.materialFormRequests.reduce((acc: Record<string, number>, { submateId, quantity }: { submateId: number; quantity: string }) => {
        acc[submateId.toString()] = Number(quantity);
        return acc;
      }, {})
    }));

    this.isLoadding = true;

    // console.log(transformedData);
    if (this.listRequestProductForm.valid) {

      const productRequestData = this.listRequestProductForm.value.itemsRProduct;

      productRequestData.forEach((item: any) => {
        if (item.price) {
          item.price = Number(item.price.replace(/,/g, ''));
        }
      });

      const transformedArray = productRequestData.map((item: any) => ({
        requestProductDTO: {
          requestProductName: item.requestProductName,
          description: item.description,
          price: item.price,
          quantity: item.quantity,
          completionTime: item.completionTime,
          request_id: parseInt(item.request_id)
        },
        filesBase64: []
      }));

      // xu li upload anh
      const promises = Object.keys(this.selectedImagesRProduct).map(async (key) => {
        const index = parseInt(key, 10);
        if (this.selectedImagesRProduct[index]) {
          const files = this.selectedImagesRProduct[index];
          const base64Files = await this.convertListImagesToBase64(files);
          return { index, base64Files }; // Trả về đối tượng chứa index và base64Files
        } else {
          console.log("Không hoạt động");
          return null;
        }
      });


      Promise.all(promises).then(results => {
        results.forEach(result => {
          if (result) { // Kiểm tra nếu kết quả không phải là null
            transformedArray[result.index].filesBase64 = result.base64Files;
          }
        });

        console.log("transformedArray", transformedArray);

        const id_Order = this.idOrder;

        this.productListService.addNewProductRequest(transformedArray, id_Order)
          .pipe(concatMap(response => {

            console.log('response:', response);

            response.result.forEach((item: any, index: number) => {
              // Kiểm tra xem có đối tượng tương ứng trong transformedDataSubMate không
              if (transformedDataSubMate[index]) {
                // Thêm thuộc tính requestProductName từ response vào đối tượng tương ứng trong transformedDataSubMate
                transformedDataSubMate[index].productId = item.requestProductId;
              }
            });

            console.log("transformedDataSubMate: ", transformedDataSubMate);

            return this.productListService.createExportMaterialListProductRequest(transformedDataSubMate);
          }),
            catchError(error => {
              this.isLoadding = false;
              if (error.status === 400 && error.error.code === 1018) {
                this.toastr.error(error.error.message, 'Lỗi');
              }
              return EMPTY;
            }))
          .subscribe(
            response => {
              this.location.replaceState('/product_management');
              this.isProduct = false;
              this.reloadProductRequest();
              this.isLoadding = false;
              this.toastr.success('Tạo sản phẩm theo yêu cầu thành công!', 'Thành công');
              $('[data-dismiss="modal"]').click();      // tat modal  
            },
            error => {
              this.isLoadding = false;
              if (error.status === 400 && error.error.message && error.error.code !== 1004) {
                this.toastr.error(error.error.message, 'Lỗi');
              }
              else {
                this.toastr.error('Tạo sản phẩm bị lỗi!', 'Lỗi');
              }
            }
          );
      });


    } else {
      this.isLoadding = false;
      this.toastr.error('Vui lòng điền đầy đủ thông tin!', 'Lỗi');
      console.log(this.listRequestProductForm)
    }
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

        const orderDate = new Date(response.result.orderDate).toISOString().split('T')[0]; // Lấy ngày tháng năm
        const orderFinish = new Date(response.result.contractDate).toISOString().split('T')[0]; // Lấy ngày tháng năm  của contractDate
        // Cập nhật form đơn hàng ở đây
        this.orderForm.patchValue({
          orderId: this.idOrder,
          orderDate: orderDate,
          code: response.result.code,
          description: response.result.description,
          price: response.result?.price,
          completionTime: response.result?.completionTime,
          status_id: response.result.status?.status_id,
          orderFinish: orderFinish,

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
    if (this.editForm.get('quantity')?.value < 1) {
      this.toastr.warning('Số lượng sản phẩm phải lớn hơn 0!', 'Lỗi');
      return;
    }

    const currentDate = new Date();
    const completionTime = new Date(this.editForm.get('completionTime')?.value);
    if (currentDate > completionTime) {
      this.toastr.error('Ngày hoàn thành phải lớn hơn ngày hiện tại!', 'Lỗi');
      return;
    }

    if (parseFloat(this.editForm.get('price')?.value.replace(/,/g, '')) < this.totalUnitPrice) {
      this.toastr.error('Giá sản phẩm phải lớn hơn giá vật liệu !', 'Lỗi');
      return;
    }
    console.log("price", this.editForm.get('price')?.value);
    console.log(this.totalUnitPrice);
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
          console.log('Error block executed', error); // Debugging log
          this.isLoadding = false;
          if (error.status === 400 && error.error.code === 1038) {
            this.toastr.warning(error.error.message, 'Lỗi');
          }
          else if (error.status === 400 && error.error.code === 1048) {
            this.toastr.warning(error.error.message, 'Lỗi');
          }
          else if (error.status === 400 && error.error.code === 1018) {
            this.toastr.warning(error.error.message, 'Lỗi');
          }
          else if (error.status === 400 && error.error.code === 1050) {
            this.toastr.warning(error.error.message, 'Lỗi');
          }
          else if (error.status === 400 && error.error.message && error.error.code !== 1004) {
            this.toastr.error(error.error.message, 'Lỗi');
          }
          else {
            this.toastr.error('Cập nhật sản phẩm bị lỗi!', 'Lỗi');
          }
          console.error('Update error', error);
          $('[data-dismiss="modal"]').click(); // Đóng modal
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
          }
          else if (error.status === 400 && error.error.message && error.error.code !== 1004) {
            this.toastr.error(error.error.message, 'Lỗi');
          } else {
            this.toastr.error("Không thể xóa sản phẩm", 'Lỗi');
          }
          $('[data-dismiss="modal"]').click();      // tat modal  

          this.isLoadding = false; // Stop the loading spinner on error
        }
      );
    // console.log('productId', this.selectedProductIdCurrentDelele);
  }

  //tinh tong tien` cho 1 order
  pricePerProduct: { [key: number]: number } = {};
  pricePerProductAndQuantity: { [key: number]: [number, number] } = {};
  totalPriceOfOrder: number = 0;

  //ham` tinh tong gia tien` va so luong cua tung san pham trong order
  calculateTotalPriceOfOrder(index: number) {
    this.totalPriceOfOrder = 0;
    const totalPrice = Object.keys(this.pricePerProduct).reduce((acc, key) => {
      const numericKey = Number(key);
      const numericPrice = Number(this.pricePerProduct[numericKey]) || 0;
      const quantity = Number(this.quantityRProductValue[numericKey]) || 0;
      return acc + (numericPrice * quantity);
    }, 0);
    this.totalPriceOfOrder = totalPrice;
    this.pricePerProductAndQuantity[index] = [this.pricePerProduct[index], this.quantityRProductValue[index]];
    // console.log('pricePerProductAndQuantity:', this.pricePerProductAndQuantity);

  }
  //danh cho format input gia tien cua RProduct 
  formatInputValueRProduct(event: any, index: number) {
    // this.totalPriceOfOrder = 0;
    let rawValue = event.target.value.replace(/,/g, '');
    this.pricePerProduct[index] = rawValue;

    // const totalPrice = Object.keys(this.pricePerProduct).reduce((acc, key) => {
    //   const numericKey = Number(key);
    //   const numericPrice = Number(this.pricePerProduct[numericKey]) || 0;
    //   const quantity = Number(this.quantityRProductValue[numericKey]) || 0;
    //   return acc + (numericPrice * quantity);
    // }, 0);
    // this.totalPriceOfOrder = totalPrice;
    this.calculateTotalPriceOfOrder(index);
    const formattedValue = rawValue.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    event.target.value = formattedValue;
  }

  //danh cho format input gia tien
  formatInputValue(event: any) {
    let rawValue = event.target.value.replace(/,/g, '');
    const formattedValue = rawValue.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    event.target.value = formattedValue;
  }

  //format gia tien
  formatNumberWithCommas(x: number): string {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  }

  //dieu` huong mui ten chi tiet
  isCollapsed: boolean[] = [];

  toggleCollapse(index: number): void {
    this.isCollapsed[index] = !this.isCollapsed[index];
  }

  //lay danh sach cac order dac biet
  specialOrders: any[] = [];
  //autocomplete for request order
  keywordRequest = 'code';
  getSpecialOrder(): void {
    this.productListService.getAllOrderSpecial()
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.specialOrders = data.result;
            // console.log('Danh sách order đặc biệt:', this.specialOrders);
          } else {
            console.error('Failed to fetch special orders:', data);
            this.toastr.error('Không thể lấy danh sách order đặc biệt!', 'Lỗi');
          }
        },
        (error) => {
          console.error('Error fetching special orders:', error);
          this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        }
      );
  }

  onSelectSpecialOrder(item: any): void {
    if (!this.idOrder) {
      this.orderForm.reset();
    }
    const orderId = item.orderId;
    this.productListService.getOrderById(orderId).subscribe(
      (response) => {

        const orderDate = new Date(response.result.orderDate).toISOString().split('T')[0]; // Lấy ngày tháng năm
        const orderFinish = response.result.contractDate ? new Date(response.result.contractDate).toISOString().split('T')[0] : null; // Lấy ngày tháng năm của contractDate hoặc null nếu không có
        this.orderForm.patchValue({
          orderId: this.idOrder,
          orderDate: orderDate,
          code: response.result.code,
          description: response.result.description,
          price: response.result?.price,
          completionTime: response.result?.completionTime,
          status_id: response.result.status?.status_id,
          orderFinish: orderFinish,

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
}