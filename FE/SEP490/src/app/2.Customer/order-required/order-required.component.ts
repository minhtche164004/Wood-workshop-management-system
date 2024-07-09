// Component Code
import { Component, OnInit } from '@angular/core';
import { ProvincesService } from 'src/app/service/provinces.service';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ProductListService } from '../../service/product/product-list.service';
import { AuthenListService } from '../../service/authen.service';

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
  selector: 'app-order-required',
  templateUrl: './order-required.component.html',
  styleUrls: ['./order-required.component.scss']
})
export class OrderRequiredComponent implements OnInit {
  uploadForm: FormGroup;
  userProfile: any = {};
  productImages: File[] = [];
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  

  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;
  selectedImages: File[] = [];

  constructor(
    private toastr: ToastrService,
    private productListService: ProductListService,
    private fb: FormBuilder,
    private provincesService: ProvincesService,
    private authenListService: AuthenListService,
  ) {
    this.uploadForm = this.fb.group({
      status_id: [0],
      response: [''],
      description: [''],
      phoneNumber: [''],
      fullname: [''],
      address: [''],
      city_province: [''],
      district_province: [''],
      wards_province: [''],
      files: this.fb.array([]),
      email: ['']
    });
  }
  ngOnInit() {
    this.userProfile = {
      username: '',
      email: '',
      phoneNumber: '',
      address: '',
      fullname: '',
      bank_name: '',
      bank_number: '',
    };
    this.authenListService.getUserProfile().subscribe((data) => {
      this.userProfile = data.result;
      
    });
    this.provincesService.getProvinces().subscribe((data: Province[]) => {
      this.provinces = data;
      this.provinces.forEach(province => {
        this.districts.push(...province.districts);
        province.districts.forEach(district => {
          this.wards.push(...district.wards);
        });
      });
    });
    this.provinceControl.valueChanges.subscribe(provinceName => {
      this.selectedProvince = this.provinces.find(city_province => city_province.name === provinceName);
      this.districts = this.selectedProvince ? this.selectedProvince.districts : [];
      this.uploadForm.controls['city_province'].setValue(this.selectedProvince ? this.selectedProvince.name : '');
      this.uploadForm.controls['district'].reset();
      this.uploadForm.controls['wards'].reset();
    });

    this.districtControl.valueChanges.subscribe(districtName => {
      this.selectedDistrict = this.districts.find(district => district.name === districtName);
      this.wards = this.selectedDistrict ? this.selectedDistrict.wards : [];
      this.uploadForm.controls['district_province'].setValue(this.selectedDistrict ? this.selectedDistrict.name : '');
      this.uploadForm.controls['wards'].reset();
    });

    this.wardControl.valueChanges.subscribe(wardName => {
      this.uploadForm.controls['wards_province'].setValue(wardName);
    });
  }

  onFileSelected(event: any) {
    const selectedFile = event.target.files[0];
    console.log('Selected File:', selectedFile);
  }
  onImagesSelected(event: any): void {
    this.selectedImages = Array.from(event.target.files);
  }

  onFilesSelected(event: any): void {
    if (event.target.files.length > 0) {
      this.productImages = [];
      for (let i = 0; i < event.target.files.length; i++) {
        const file = event.target.files[i];
        this.productImages.push(file);
      }
      console.log('Selected Product Images:', this.productImages);
    }
  }

  onSubmit(): void {
    if (this.uploadForm.valid && this.selectedImages.length) {
      const productData = this.uploadForm.value;
      console.log('Form Data:', productData);

      console.log('Selected Images:', this.selectedImages);

      this.authenListService.uploadProductRequired(productData, this.selectedImages)
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

  onCancel() {
    this.uploadForm.patchValue({
      description: '',
      response: '',
      files: ''
    });
    const filesArray = this.uploadForm.get('files') as FormArray;
    filesArray.clear();
  }
}


