// Component Code
import { Component, OnInit } from '@angular/core';
import { ProvincesService } from 'src/app/service/provinces.service';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
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
  imagesPreview: string[] = [];
  isLoadding: boolean = false;   //loading when click button
  uploadForm: FormGroup;
  userProfile: any = {};
  productImages: File[] = [];
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];

  selectedFiles: File[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;
  selectedWard: any;
  selectedStatus: any = null;
  selectedImages: File[] = [];

  isDisabled: boolean = false;
 
  selectedPaymenet: number = 0;
  constructor(
    private toastr: ToastrService,
    private productListService: ProductListService,
    private fb: FormBuilder,
    private provincesService: ProvincesService,
    private authenListService: AuthenListService,
  ) {
    this.provinceControl = new FormControl('', Validators.required);
    this.districtControl = new FormControl('', Validators.required);
    this.wardControl = new FormControl('', Validators.required);

    this.uploadForm = this.fb.group({
      status_id: [0],
      response: [''],
      description: [''],
      phoneNumber: [''],
      fullname: [''],
      address: [''],
      city_province:  [''],
      district_province:  [''],
      wards_province:  [''],
      files: this.fb.array([]),
      payment_method: [''],
      email: ['']
    });
    
  }
  ngOnInit() {
    this.loadData();
    this.updateControls1();
  
    
  }
  updateControls1() {
    // Disable/enable the form controls based on your logic
    this.provinceControl.disable();
    this.districtControl.disable();
    this.wardControl.disable();
  }
  onResetImage() {
    this.selectedImages = [];
    (document.getElementById('thumbnailImage') as HTMLInputElement).value = '';
    this.imagesPreview = [];
  }
  loadData() {
    this.isLoadding = true;
    this.authenListService.getUserProfile().subscribe((data) => {
      this.userProfile = data.result;

      this.provincesService.getProvinces().subscribe((data: Province[]) => {
        this.provinces = data;
        this.updateControls();

      });
      this.isLoadding = false;
    });
  }
  reloadData() {
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
    // Lấy danh sách các file đã chọn
    this.selectedImages = Array.from(event.target.files);
  
    // Tạo một mảng các file
    const files: File[] = Array.from(event.target.files as FileList);
  
    // Kiểm tra định dạng file
    const allowedExtensions = ['.jpg', '.jpeg', '.png'];
    const invalidFiles = files.filter(file => !allowedExtensions.some(ext => file.name.toLowerCase().endsWith(ext)));
  
    if (invalidFiles.length > 0) {
      // Hiển thị thông báo lỗi
      this.toastr.warning(`Chỉ được phép chọn file ảnh với định dạng ${allowedExtensions.join(', ')}.`);
  
      // Lọc bỏ các file không hợp lệ
      this.selectedImages = this.selectedImages.filter(file => !invalidFiles.includes(file));
    }
  
    // Xóa list preview cũ
    this.imagesPreview = [];
  
    // Tạo và lưu URL cho preview
    this.selectedImages.forEach((file: File) => {
      const url = URL.createObjectURL(file);
      this.imagesPreview.push(url);
    });
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
  validateCreateOrder(): boolean {
    const paymentMethod = this.uploadForm.controls['payment_method'].value;
    if (paymentMethod == null) {
      this.toastr.error('Không được bỏ trống trường Phương thức thanh toán');
      return false;
    }
    if (this.selectedImages.length === 0) {
      this.toastr.error('Không được bỏ trống trường Ảnh minh họa');
      return false;
    }
    const description = this.uploadForm.controls['description'].value.trim();;
    if (description == '') {
      this.toastr.error('Không được bỏ trống trường Mô tả sản phẩm');
      return false;
    }

    
   

    return true;
  }
  onSubmit1(): void {
    this.isLoadding = true;
    if (!this.validateCreateOrder()) {
      this.isLoadding = false;
      return;
    }
    if (this.uploadForm.valid && this.selectedImages.length) {
      const productData = this.uploadForm.value;
      console.log('Form Data order:', productData);
  
      console.log('Selected Images:', this.selectedImages);
  
      this.authenListService.uploadProductRequired(productData, this.selectedImages)
        .subscribe(
          response => {
            this.isLoadding = false;
            this.toastr.success('Đặt hàng thành công!', 'Thành công');
           this.onCancel();
           this.onResetImage();
          },
          error => {
            this.isLoadding = false;
            this.toastr.error('Đặt hàng bị lỗi!', 'Lỗi');

          }
        );
    }
  }
  

  onCancel() {
    // Clear the FormArray for 'files'
    const filesArray = this.uploadForm.get('files') as FormArray;
    filesArray.clear();
  
    // Reset the form values
    this.uploadForm.patchValue({
      payment_method: null,
      files: [],
      description: ''
    });
  
    // Reset the imagesPreview array
    this.imagesPreview = [];
  }
}

