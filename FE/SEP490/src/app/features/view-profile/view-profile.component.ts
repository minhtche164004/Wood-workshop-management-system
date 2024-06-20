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
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;

  userProfile: any = {};

  constructor(
    private provincesService: ProvincesService,
    private authenListService: AuthenListService,
    private toastr: ToastrService
  ) { }
  ngOnInit() {
    this.userProfile = {
      username: '',
      email: '',
      phoneNumber: '',
      address: '',
      fullname: '',
      bank_name: '',
      bank_number: '',
      city: '',
      district: '',
      wards: '',

      // Add more properties as needed
    };

    // Assuming you receive data from an API call
    this.authenListService.getUserProfile().subscribe((data) => {
      this.userProfile = data.result; // Assuming 'result' contains the profile data
      console.log(data);
    });

    this.provincesService.getProvinces().subscribe((data: Province[]) => {
      this.provinces = data;
      // Ví dụ lặp qua các tỉnh/thành phố để lấy ra các quận/huyện và phường/xã
      this.provinces.forEach(province => {
        this.districts.push(...province.districts);
        province.districts.forEach(district => {
          this.wards.push(...district.wards);
        });
      });

    });
    this.provinceControl.valueChanges.subscribe(provinceName => {
      this.onProvinceChange();
    });

  }
  onProvinceChange() {
    const selectedProvinceName = this.userProfile.city; // assuming 'city' is bound to ngModel of the province dropdown
    this.selectedProvince = this.provinces.find(province => province.name === selectedProvinceName);

    // Update districts based on the selected province
    this.districts = this.selectedProvince ? this.selectedProvince.districts : [];

    // Reset selected district and ward
    this.userProfile.district = ''; // reset selected district in the model
    this.userProfile.wards = ''; // reset selected ward in the model
  }

  onDistrictChange() {
    const selectedDistrictName = this.userProfile.district; // assuming 'city' is bound to ngModel of the province dropdown
    this.selectedDistrict = this.districts.find(district => district.name === selectedDistrictName);

    // Update districts based on the selected province
    this.wards = this.selectedDistrict ? this.selectedDistrict.wards : [];

    this.userProfile.wards = ''; // reset selected ward in the model
  }
  validateUsername(username: string): boolean {
    const usernameRegex = /^[a-zA-Z0-9]{3,20}$/;
    return usernameRegex.test(username);
}

validatePassword(password: string): boolean {
    const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
    return passwordRegex.test(password);
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
    if (!this.validateRegistration()) {
      return;
    }
    console.log('Saving profile changes:', this.userProfile); // Log userProfile object for debugging

    this.authenListService.updateUserProfile(this.userProfile).subscribe(
      (response: any) => {
        console.log('Profile updated successfully:', response);
        this.toastr.success('Thông tin đã được cập nhật thành công!', 'Thành công');
      },
      (error: any) => {
        console.error('Error updating profile:', error);
        if (error.status === 400 && error.error.code === 1016) {
          this.toastr.error('Sai Format của Đặt Tên! Vui lòng kiểm tra lại', 'Lỗi cố khi thay đổi thông tin'); // Hiển thị thông báo lỗi cho tài khoản bị khóa
        } 
       
      }
    );
  }


}
