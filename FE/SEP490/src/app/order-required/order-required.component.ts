import { Component, OnInit } from '@angular/core';
import { ProvincesService } from 'src/app/service/provinces.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { ProductListService } from '../service/product/product-list.service';
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
  selectedThumbnail: File | null = null;
  productImages: File[] = [];
  city_province: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;
  selectedImages: File[] = [];
  constructor(  private toastr: ToastrService,private productListService: ProductListService,private fb: FormBuilder,private provincesService: ProvincesService) {
    this.uploadForm = this.fb.group({
      response: [''],
      description: [''],
      phoneNumber: [''],
      fullname: [''],
      address: [''],
      city_province: [''],
      district: [''],
      wards: [''],
    });
  }
  onFileSelected(event: any) {
    const selectedFile = event.target.files[0];
    console.log('Selected File:', selectedFile);
  }
  ngOnInit() {
    this.provincesService.getProvinces().subscribe((data: Province[]) => {
      this.city_province = data;
      console.log(this.city_province);
    });

    this.provinceControl.valueChanges.subscribe(provinceName => {
      this.selectedProvince = this.city_province.find(city_province => city_province.name === provinceName);
      this.districts = this.selectedProvince ? this.selectedProvince.districts : [];
      this.uploadForm.controls['city_province'].setValue(this.selectedProvince ? this.selectedProvince.name : '');
      this.uploadForm.controls['district'].reset();
      this.uploadForm.controls['wards'].reset();
    });

    this.districtControl.valueChanges.subscribe(districtName => {
      this.selectedDistrict = this.districts.find(district => district.name === districtName);
      this.wards = this.selectedDistrict ? this.selectedDistrict.wards : [];
      this.uploadForm.controls['district'].setValue(this.selectedDistrict ? this.selectedDistrict.name : '');
      this.uploadForm.controls['wards'].reset();
    });

    this.wardControl.valueChanges.subscribe(wardName => {
      this.uploadForm.controls['wards'].setValue(wardName);
    });
  }
  onThumbnailSelected(event: any): void {
    this.selectedThumbnail = event.target.files[0];
    console.log('Selected Thumbnail:', this.selectedThumbnail);
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
    if (this.selectedThumbnail) {
      const productRequiredData = this.uploadForm.value;
      console.log('Product Data:', productRequiredData); // Log the entered data

      this.productListService.uploadProductRequired(productRequiredData, this.selectedThumbnail)
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
}