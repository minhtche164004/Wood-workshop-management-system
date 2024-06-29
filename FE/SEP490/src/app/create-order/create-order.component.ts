import { Component, OnInit } from '@angular/core';
import { ProvincesService } from 'src/app/service/provinces.service';
import { FormControl } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
interface CustomerInfo {
  userid: number;
  fullname: string;
  address: string;
  city_province: string;
  district: string;
  wards: string;
  phone: string;
}
interface ReceiveInfo {
  fullname: string;
  address: string;
  city_province: string;
  district: string;
  wards: string;
  phone: string;
}
interface ProductItem {
  id: number;
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
  productForm: FormGroup;


  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;

  constructor(private provincesService: ProvincesService, private fb: FormBuilder) {
   
    this.productForm = this.fb.group({
      items: this.fb.array([])  // Khởi tạo FormArray trống
    });

  }

  get items(): FormArray {
    return this.productForm.get('items') as FormArray;
  }

  addItem(): void {
    this.items.push(this.fb.group({
      quantity: ['', Validators.required],
      price: ['', Validators.required]
    }));
  }


  onFileSelected(event: any) {
    const selectedFile = event.target.files[0];
    // Xử lý logic khi đã chọn file, ví dụ như upload lên server
    console.log('Selected File:', selectedFile);
  }
  ngOnInit() {
    this.provincesService.getProvinces().subscribe((data: Province[]) => {
      this.provinces = data;
      console.log(this.provinces);
    });
    this.addItem();

    

    this.provinceControl.valueChanges.subscribe(provinceName => {
      console.log('provinceName:', provinceName);
      this.selectedProvince = this.provinces.find(province => province.name === provinceName);
      console.log('selectedProvince:', this.selectedProvince);
      this.districts = this.selectedProvince ? this.selectedProvince.districts : [];
    });

    this.districtControl.valueChanges.subscribe(districtName => {
      console.log('districtName:', districtName);
      const selectedDistrict = this.districts.find(district => district.name === districtName);
      console.log('selectedDistrict:', selectedDistrict);
      this.wards = selectedDistrict ? selectedDistrict.wards : [];
      this.wardControl.reset();
    });






  }
}
