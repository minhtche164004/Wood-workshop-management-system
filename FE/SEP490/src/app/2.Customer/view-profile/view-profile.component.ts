import { Component, OnInit } from '@angular/core';
import { AuthenListService } from 'src/app/service/authen.service';
import { ProvincesService } from 'src/app/service/provinces.service';
import { FormControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

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
  selector: 'app-view-profile',
  templateUrl: './view-profile.component.html',
  styleUrls: ['./view-profile.component.scss']
})
export class ViewProfileComponent implements OnInit {
  isLoadding: boolean = false; 
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;
  selectedWard: any;
  isEditing = false; // Flag to manage readonly state

  userProfile: any = {};

  constructor(
    private provincesService: ProvincesService,
    private authenListService: AuthenListService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {

    this.loadData();
  }

  loadData() {

    this.authenListService.getUserProfile().subscribe((data) => {
      this.userProfile = data.result;

      console.log("Data Profile: ", this.userProfile)
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


  editProfile() {
    this.isEditing = true;
  }

  cancelChanges() {
    this.isEditing = false;
    // Reload the user profile to discard changes
    this.authenListService.getUserProfile().subscribe((data) => {
      this.userProfile = data.result;
    });
  }

  validateUsername(username: string): boolean {
    const usernameRegex = /^[a-zA-Z0-9]{3,20}$/;
    return usernameRegex.test(username);
  }

  validateEmail(email: string): boolean {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailRegex.test(email);
  }

  validatePhoneNumber(phoneNumber: string): boolean {
    const phoneNumberRegex = /^[0-9]{10,12}$/;
    return phoneNumberRegex.test(phoneNumber);
  }

  validateRegistration(): boolean {
    if (!this.userProfile.username || this.userProfile.username.trim() === '') {
      this.toastr.error('Không được bỏ trống trường Tên đăng nhập');
      return false;
    }
    if (!this.userProfile.email || this.userProfile.email.trim() === '') {
      this.toastr.error('Không được bỏ trống trường Email');
      return false;
    }
    if (!this.userProfile.phoneNumber || this.userProfile.phoneNumber.trim() === '') {
      this.toastr.error('Không được bỏ trống trường Số điện thoại');
      return false;
    }
    if (!this.userProfile.fullname || this.userProfile.fullname.trim() === '') {
      this.toastr.error('Không được bỏ trống trường Họ tên');
      return false;
    }
    if (!this.userProfile.address || this.userProfile.address.trim() === '') {
      this.toastr.error('Không được bỏ trống trường Địa chỉ');
      return false;
    }
    if (!this.userProfile.city || this.userProfile.city.trim() === '') {
      this.toastr.error('Không được bỏ trống trường Thành phố');
      return false;
    }
    if (!this.userProfile.district || this.userProfile.district.trim() === '') {
      this.toastr.error('Không được bỏ trống trường Quận/Huyện');
      return false;
    }
    if (!this.userProfile.wards || this.userProfile.wards.trim() === '') {
      this.toastr.error('Không được bỏ trống trường Phường/Xã');
      return false;
    }
    if (!this.validateUsername(this.userProfile.username)) {
      this.toastr.error('Tên đăng nhập không hợp lệ.', 'Lỗi');
      return false;
    }
    if (!this.validateEmail(this.userProfile.email)) {
      this.toastr.error('Email không hợp lệ.', 'Lỗi');
      return false;
    }
    if (!this.validatePhoneNumber(this.userProfile.phoneNumber)) {
      this.toastr.error('Số điện thoại không hợp lệ.', 'Lỗi');
      return false;
    }
    return true;
  }

  saveChanges() {
    this.isLoadding = true;
    if (!this.validateRegistration()) {
      this.isLoadding = false;
      return;
    }
    console.log('Saving profile changes:', this.userProfile); // Log userProfile object for debugging

    this.authenListService.updateUserProfile(this.userProfile).subscribe(
      (response: any) => {
        this.isLoadding = false;
        console.log('Profile updated successfully:', response);
        this.toastr.success('Thông tin đã được cập nhật thành công!', 'Thành công');
       
      },
      (error: any) => {
        this.isLoadding = false;
        console.error('Error updating profile:', error);
        if (error.status === 400 && error.error.code === 1016) {
          this.toastr.error('Sai Format của Đặt Tên! Vui lòng kiểm tra lại', 'Lỗi cố khi thay đổi thông tin'); // Hiển thị thông báo lỗi cho tài khoản bị khóa
        }
      }
    );
  }
}
