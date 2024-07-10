// Component Code
import { Component, OnInit } from '@angular/core';
import { ProvincesService } from 'src/app/service/provinces.service';
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ProductListService } from '../../service/product/product-list.service';
import { AuthenListService } from '../../service/authen.service';
import { timer } from 'rxjs';

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
  isLoadding: boolean = false;   //loading when click button
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
  selectedWard: any;
  
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
    this.loadData();
  }

  loadData() {
    this.authenListService.getUserProfile().subscribe((data) => {
      this.userProfile = data.result;

      this.provincesService.getProvinces().subscribe((data: Province[]) => {
        this.provinces = data;
        this.updateControls();

      });
    });
  }

  updateControls() {
    // Set initial values based on userProfile
    this.provinceControl.setValue(this.userProfile.city);
    this.districtControl.setValue(this.userProfile.district);

    // Handle changes in province selection
    this.provinceControl.valueChanges.subscribe(provinceName => {
      this.selectedProvince = this.provinces.find(province => province.name === provinceName);
      this.districts = this.selectedProvince?.districts || [];
      // Set the district control to a default value that corresponds to the placeholder
      this.districtControl.setValue(null, { emitEvent: false });
      this.wardControl.setValue(null, { emitEvent: false });
    });
    
    this.districtControl.valueChanges.subscribe(districtName => {
      this.selectedDistrict = this.districts.find(district => district.name === districtName);
      this.wards = this.selectedDistrict?.wards || [];
      this.wardControl.setValue(null, { emitEvent: false });
    });
    this.selectedProvince = this.provinces.find(province => province.name === this.userProfile.city);
    if (this.selectedProvince) {
      this.districts = this.selectedProvince.districts;
      this.selectedDistrict = this.districts.find(district => district.name === this.userProfile.district);
      if (this.selectedDistrict) {
        this.wards = this.selectedDistrict.wards;
        // Update ward control based on userProfile initial data
      }
    }
  }




  // this.wardControl.valueChanges.subscribe(wardName => {
  //   this.uploadForm.controls['wards_province'].setValue(wardName);
  // });


  onFileSelected(event: any) {
    const selectedFile = event.target.files[0];
    // console.log('Selected File:', selectedFile);
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
      // console.log('Selected Product Images:', this.productImages);
    }
  }

  onSubmit(): void {
    this.isLoadding = true;
    if (this.uploadForm.valid && this.selectedImages.length) {
      const productData = this.uploadForm.value;
      // console.log('Form Data:', productData);
  
      // console.log('Selected Images:', this.selectedImages);
  
      this.authenListService.uploadProductRequired(productData, this.selectedImages)
        .subscribe(
          response => {
            this.isLoadding = false;
            this.toastr.success('Đặt hàng thành công!', 'Thành công');
            timer(1000).subscribe(() => {
              window.location.reload();
            });
          },
          error => {
            this.isLoadding = false;
            this.toastr.error('Đặt hàng bị lỗi!', 'Lỗi');
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

