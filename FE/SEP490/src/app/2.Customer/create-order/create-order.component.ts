import { Component, OnInit } from '@angular/core';
import { ProvincesService } from 'src/app/service/provinces.service';
import { FormControl } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

import { FormBuilder, FormGroup, FormArray, Validators, ReactiveFormsModule} from '@angular/forms';
import { CreateOrderService } from 'src/app/service/create-order.service';
interface CustomerInfo {
  inforId: number;
  fullname: string;
  address: string;
  city_province: string;
  district: string;
  wards: string;
  phoneNumber: string;
}
interface ReceiveInfo {
  fullnameCopy: string;
  addressCopy: string;
  city_provinceCopy: string;
  districtCopy: string;
  wardsCopy: string;
  phoneNumberCopy: string;
}
interface ProductItem {
  productId: number;
  quantity: number;
  price: number;
}
interface Province {
  code: string;
  name: string;
  districts: District[];
}

interface District {
  code: string;
  name: string;
  wards: Ward[];
}

interface Ward {
  code: string;
  name: string;
}

@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
  styleUrls: ['./create-order.component.scss']
})
export class CreateOrderComponent implements OnInit {
  inforId: number = 0;
  fullname: string = ''; // Initialize input1
  phonenumber: string = ''; // Initialize input1
  email: string = ''; // Initialize input1
  address: string = ''; // Initialize input1
  city_change: Province[] = []; // Initialize city as null
  district_change: any | null = null; // Initialize city as null
  wards_change: number | null = null; // Initialize city as null

  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;

  //copy value sang cho nguoi nhan
  fullnameCopy: string = ''; // Initialize input1
  phonenumberCopy: string = ''; // Initialize input1
  emailCopy: string = ''; // Initialize input1
  addressCopy: string = ''; // Initialize input1
  city_changeCopy: Province[] = []; // Initialize city as null
  district_changeCopy: any | null = null; // Initialize city as null
  wards_changeCopy: number | null = null; // Initialize city as null

  provincesCopy: Province[] = [];
  districtsCopy: District[] = [];
  wardsCopy: Ward[] = [];
  provinceControlCopy = new FormControl();
  districtControlCopy = new FormControl();
  wardControlCopy = new FormControl();
  selectedProvinceCopy: any;
  selectedDistrictCopy: any;
  //
  productForm: FormGroup;
  phoneList: any[] = [];
  //autocomplete for phonenumber
  keyword = '';
  //autocomplete for product
  keywordProduct = 'productName';
  productList: any[] = [];

  //tinh tien` san pham
  totalUnitPrice: number = 0; // tinh tong tien
  unitPriceProduct: number[] = [];
  quantityPerProduct: string[] = [];
  //

  orderForm : FormGroup;
  constructor(private provincesService: ProvincesService, private fb: FormBuilder, private createOrderService: CreateOrderService,) {

    this.productForm = this.fb.group({
      items: this.fb.array([])  // Khởi tạo FormArray trống
    });

    this.orderForm = this.fb.group({
      special_order: [0],
      cusInfo: this.fb.group({
        userid: [0],
        fullname: [''],
        address: [''],
        city_province: [''],
        district: [''],
        wards: [''],
        phone: [''],
        email: ['']
      }),
      receiveInfo: this.fb.group({
        fullname: [''],
        address: [''],
        city_province: [''],
        district: [''],
        wards: [''],
        phone: [''],
        email: ['']
      }),
      orderDetail: this.fb.group({
        productItems: this.fb.array([
          this.fb.group({
            id: [0],
            quantity: [0],
            price: [0]
          })
        ])
      }),
      payment_method: [0],

    });

  }

  get items(): FormArray {
    return this.productForm.get('items') as FormArray;
  }

  addItem(): void {
    this.items.push(this.fb.group({
      id: ['', Validators.required],
      quantity: ['', Validators.required],
      price: ['', Validators.required]
    }));
  }

  removeItem(index: number) {
    const items = this.productForm.get('items') as FormArray;
    items.removeAt(index);
    if (this.unitPriceProduct[index] && this.quantityPerProduct[index]) {
      this.totalUnitPrice = this.totalUnitPrice - (Number(this.unitPriceProduct[index]) * Number(this.quantityPerProduct[index]));
    }
  }

  ngOnInit() {
    this.provincesService.getProvinces().subscribe((data: Province[]) => {
      this.provinces = data;
      console.log(this.provinces);
    });
    this.addItem();

    //Danh cho nguoi dat hang
    this.provinceControl.valueChanges.subscribe(provinceName => {
      // console.log('provinceName:', provinceName);
      this.selectedProvince = this.provinces.find(province => province.name === provinceName);
      console.log('selectedProvince:', this.selectedProvince);
      this.districts = this.selectedProvince ? this.selectedProvince.districts : [];
    });

    this.districtControl.valueChanges.subscribe(districtName => {
      // console.log('districtName:', districtName);
      const selectedDistrict = this.districts.find(district => district.name === districtName);
      console.log('selectedDistrict:', selectedDistrict);
      this.wards = selectedDistrict ? selectedDistrict.wards : [];
      this.wardControl.reset();
    });

    //Danh cho nguoi nhan
    this.provinceControlCopy.valueChanges.subscribe(provinceName => {
      // console.log('provinceName:', provinceName);
      this.selectedProvinceCopy = this.provincesCopy.find(province => province.name === provinceName);
      console.log('selectedProvinceCopy:', this.selectedProvinceCopy);
      this.districtsCopy = this.selectedProvinceCopy ? this.selectedProvinceCopy.districts : [];
    });

    this.districtControlCopy.valueChanges.subscribe(districtName => {
      // console.log('districtName:', districtName);
      const selectedDistrictCopy = this.districtsCopy.find(district => district.name === districtName);
      console.log('selectedDistrictCopy:', selectedDistrictCopy);
      this.wardsCopy = selectedDistrictCopy ? selectedDistrictCopy.wards : [];
      this.wardControlCopy.reset();
    });

    this.loadAllPhoneNumber();
    this.loadAllProductForCustomer();
  }

  loadAllPhoneNumber(): void {
    // this.phoneList = phoneNumbers;
    this.createOrderService.getGetListPhoneNumber().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.phoneList = data.result;
          // console.log('phoneList:', this.phoneList);
        }
      },
      (error) => {
        console.error('Error fetching phoneList:', error);
      }
    );
  }

  onPhoneNumberChange(phoneNumber: string): void {
    this.createOrderService.getUserInfoByPhone(phoneNumber).subscribe(
      (data: any) => {
        console.log('data theo phone:', data.result);
        if (data.code === 1000) {
          const customerInfo: CustomerInfo = data.result;
          // this.inforId = customerInfo.inforId;
          // this.fullname = customerInfo.fullname;
          // this.phonenumber = customerInfo.phoneNumber;
          // this.address = customerInfo.address;
          this.selectedProvince = this.provinces.find(province => province.name === customerInfo.city_province);
          this.provinceControl.setValue(customerInfo.city_province);
          setTimeout(() => {
            this.districtControl.setValue(customerInfo.district);
          }, 0);
          setTimeout(() => {
            this.wardControl.setValue(customerInfo.wards);
          }, 0);
          //copy value sang cho nguoi nhan
          // const receiveInfo: ReceiveInfo = data.result; {
          //   this.fullnameCopy = customerInfo.fullname;
          //   this.phonenumberCopy = customerInfo.phoneNumber;
          //   this.addressCopy = customerInfo.address;
          //   this.selectedProvinceCopy = this.provinces.find(province => province.name === customerInfo.city_province);
          //   this.provinceControlCopy.setValue(customerInfo.city_province);
          //   setTimeout(() => {
          //     this.districtControlCopy.setValue(customerInfo.district);
          //   }, 0);
          //   setTimeout(() => {
          //     this.wardControlCopy.setValue(customerInfo.wards);
          //   }, 0);
          // }
          //
          this.orderForm.patchValue({
            cusInfo:{
              userid: customerInfo.inforId,
              fullname: customerInfo.fullname,
              address: customerInfo.address,
              city_province: customerInfo.city_province,
              district: customerInfo.district,
              wards: customerInfo.wards,
              phone: customerInfo.phoneNumber
            },        

            receiveInfo: {
              fullname: customerInfo.fullname,
              phonenumber: customerInfo.phoneNumber,
              address: customerInfo.address,
              city_province: customerInfo.city_province,
              district: customerInfo.district,
              wards: customerInfo.wards,
              phone: customerInfo.phoneNumber,
              // this.emailCopy: customerInfo.email
            }
          });
        }
        // console.log('customerInfo:', data.result);
        // console.log(this.city_change)
        // console.log("this.districinfo", this.districtControlCopy.value)
      },
      (error) => {
        console.error('Error fetching phoneList:', error);
      }
    );
  }


  //danh` cho phan san pham
  loadAllProductForCustomer(): void {
    this.createOrderService.getAllProductCustomer().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.productList = data.result;
        }
      },
      (error) => {
        console.error('Error fetching phoneList:', error);
      }
    );
  }

  onProductChange(item: any, index: number): void {
    const productId = item.productId;
    this.createOrderService.getProductById(productId).subscribe(
      (data: any) => {
        if (data.code === 1000) {
          const productItem: ProductItem = data.result;
          this.items.at(index).patchValue({
            id: productItem.productId,
            price: productItem.price
          });
          this.unitPriceProduct[index] = productItem.price;
        }

      },
      (error) => {
        console.error('Error fetching phoneList:', error);
      }
    );
  }

  onQuantityChange(event: Event, index: number) {
    this.quantityPerProduct[index] = (event.target as HTMLInputElement).value;
    this.totalUnitPrice = 0;

    if (this.unitPriceProduct && this.quantityPerProduct) {
      for (const key of Object.keys(this.unitPriceProduct)) {
        const index = Number(key);
        const numericUnitPrice = Number(this.unitPriceProduct[index]) || 0;
        const quantity = Number(this.quantityPerProduct[index]) || 0;
        const totalForThisItem = numericUnitPrice * quantity;
        this.totalUnitPrice += totalForThisItem;
      }
    }
    console.log('totalprice:', this.totalUnitPrice);

  }

  //
  onSubmit(): void {
    
      // this.isLoadding = true;
      const orderData = this.orderForm.value;
      console.log(orderData);

      // console.log('data goc:', this.materialForm.value);
      // var temp = this.materialForm.value;

      // tach lay quantity va subMaterialId
      // var processedData = temp.items.map((item: MaterialItem) => (
      //   [(item.subMaterialId as string), item.quantity]
      // ));
      // convert thanh dang map
      console.log("product Data: ", this.productForm.value);
      // const transformedObject: { [key: string]: number } = {};

      // for (const [subMaterialId, quantity] of processedData) {
      //   transformedObject[subMaterialId] = quantity;
      // }

      // // var productId;
      // this.productListService.uploadProduct(productData, this.selectedThumbnail, this.selectedImages).pipe(
      //   concatMap(response => {

      //     const transformedData = {
      //       productId: response.result.productId,
      //       subMaterialQuantities: transformedObject
      //     };
      //     return this.productListService.createExportMaterialProduct(transformedData);
      //   })
      // ).subscribe(
      //   response => {
      //     this.reloadProduct();
      //     this.isLoadding = false;
      //     this.toastr.success('Tạo sản phẩm thành công!', 'Thành công');
      //     $('[data-dismiss="modal"]').click();
      //   },
      //   error => {
      //     this.isLoadding = false;
      //     this.toastr.error('Tạo sản phẩm bị lỗi!', 'Lỗi');
      //   }
      // );
    
  }
}
