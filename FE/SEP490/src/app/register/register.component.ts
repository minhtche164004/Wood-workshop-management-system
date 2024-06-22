import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ProvincesService } from 'src/app/service/provinces.service'; // Ensure correct path to your ProvincesService
import { ToastrService } from 'ngx-toastr';
import { FormControl } from '@angular/forms';
import { environment } from 'src/app/environments/environment'; // Đường dẫn đúng tới file môi trường
interface JwtAuthenticationResponse {
  token: string;
  refreshToken: string;
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

interface RegistrationRequest {
  username: string;
  password: string;
  checkPass: string;
  email: string;
  phoneNumber: string;
  address: string;
  fullname: string;
  status: number;
  city: string;
  district: string;
  wards: string;
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;

  loginObj: any = {
    username: '',
    password: ''
  };

  username: string = '';
  password: string = '';
  checkPass: string = '';
  email: string = '';
  phoneNumber: string = '';
  address: string = '';
  fullname: string = '';
  status: number = 0; // Default value for status

  errorMessage: string = '';
  successMessage: string = '';
  private apiUrl_registration = `${environment.apiUrl}api/auth/registration`; // URL của backend

  constructor(
    private elementRef: ElementRef,
    private toastr: ToastrService, // Inject ToastrService
    private http: HttpClient,
    private router: Router,
    private provincesService: ProvincesService
  ) {}

  ngOnInit(): void {
    this.provincesService.getProvinces().subscribe((data: Province[]) => {
      this.provinces = data;
      console.log(this.provinces);
    });

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

    const signUpButton = this.elementRef.nativeElement.querySelector('#signUp');
    const signInButton = this.elementRef.nativeElement.querySelector('#signIn');
    const container = this.elementRef.nativeElement.querySelector('#container');

    signUpButton.addEventListener('click', () => {
      container.classList.add('right-panel-active');
    });

    signInButton.addEventListener('click', () => {
      container.classList.remove('right-panel-active');
    });
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
    if (!this.username || this.username.trim() === "") {
      this.toastr.error('Không được bỏ trống trường thông tin');
        return false;
    }

    if (!this.password || this.password.trim() === "") {
      this.toastr.error('Không được bỏ trống trường thông tin');
        return false;
    }

    if (!this.checkPass || this.checkPass.trim() === "") {
      this.toastr.error('Không được bỏ trống trường thông tin');
        return false;
    }

    if (!this.email || this.email.trim() === "") {
      this.toastr.error('Không được bỏ trống trường thông tin');
        return false;
    }

    if (!this.phoneNumber || this.phoneNumber.trim() === "") {
      this.toastr.error('Không được bỏ trống trường thông tin');
        return false;
    }

    if (!this.address || this.address.trim() === "") {
      this.toastr.error('Không được bỏ trống trường thông tin');
        return false;
    }

    if (!this.provinceControl.value || this.provinceControl.value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường thông tin');
        return false;
    }

    if (!this.districtControl.value || this.districtControl.value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường thông tin');
        return false;
    }

    if (!this.wardControl.value || this.wardControl.value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường thông tin');
        return false;
    }

    if (!this.validateUsername(this.username)) {
        this.toastr.error('Tên đăng nhập không hợp lệ.', 'Lỗi');
        return false;
    }

    if (!this.validatePassword(this.password)) {
        this.toastr.error('Mật khẩu phải chứa ít nhất một chữ hoa, một chữ thường và một số.', 'Lỗi');
        return false;
    }

    if (this.password !== this.checkPass) {
        this.toastr.error('Mật khẩu xác nhận không khớp.', 'Lỗi');
        return false;
    }

    if (!this.validateEmail(this.email)) {
        this.toastr.error('Email không hợp lệ.', 'Lỗi');
        return false;
    }

    if (!this.validatePhoneNumber(this.phoneNumber)) {
        this.toastr.error('Số điện thoại không hợp lệ.', 'Lỗi');
        return false;
    }

    return true;
}


  registerUser(): void {
    if (!this.validateRegistration()) {
      return;
    }

    const registrationRequest: RegistrationRequest = {
      username: this.username,
      password: this.password,
      checkPass: this.checkPass,
      email: this.email,
      phoneNumber: this.phoneNumber,
      address: this.address,
      fullname: this.fullname,
      status: this.status,
      city: this.provinceControl.value,
      district: this.districtControl.value,
      wards: this.wardControl.value
    };
    this.http.post<any>(this.apiUrl_registration, registrationRequest, { withCredentials: true })
      .subscribe(
        () => {
          console.log('Registration successful. Check your email for OTP.');
          this.toastr.success('Đăng ký thành công! Kiểm tra email để nhận OTP.', 'Thành công');
          this.router.navigate(['/otp']);
        },
        (error: any) => {
          console.error('Registration failed', error);
          this.errorMessage = 'Registration failed. Please try again.';
          this.toastr.error('Đăng ký thất bại. Vui lòng thử lại.', 'Lỗi');
        }
      );
  }

  onLogin(): void {
    console.log('Bắt đầu quá trình đăng nhập');
    console.log('Tên đăng nhập:', this.loginObj.username);
    console.log('Mật khẩu:', this.loginObj.password);

    this.http.post(`${environment.apiUrl}api/auth/login`, this.loginObj).subscribe(
      (response: any) => {
        console.log('Truy cập API đăng nhập thành công');
        if (response.code === 1000) {
          console.log('Đăng nhập thành công:', response.code);
          this.toastr.success('Đăng nhập thành công!', 'Thành công');
          const token = response.result.token;
          console.log('Access Token:', token);
          localStorage.setItem('loginToken', token);

          const userData = response.result.user;
          console.log('Thông tin người dùng:', userData);

          const authorities = userData.authorities.map((authority: { authority: any }) => authority.authority);
          if (authorities.includes('CUSTOMER')) {
            this.router.navigateByUrl('/homepage');
          } else if (authorities.includes('ADMIN')) {
            this.router.navigateByUrl('/admin');
          } else if (authorities.includes('EMPLOYEE')) {
            this.router.navigateByUrl('/employee-work');
          } else {
            console.error('Vai trò người dùng không hợp lệ:', authorities);
            this.toastr.error('Vai trò người dùng không hợp lệ', 'Lỗi cố khi thực hiện đăng nhập'); // Ví dụ về việc sử dụng toastr trong logic if-else
          }
        }
      },
      (error: HttpErrorResponse) => {
        console.error('Lỗi khi đăng nhập', error);
          if (error.status === 400 && error.error.code === 1028) {
          this.toastr.error('Sai Tên đăng nhập ! Vui lòng kiểm tra lại', 'Lỗi cố khi thực hiện đăng nhập'); // Hiển thị thông báo lỗi cho tài khoản bị khóa
        } 
        else if (error.status === 400 && error.error.code === 1006) {
          this.toastr.error('Sai Mật khẩu ! Vui lòng kiểm tra lại', 'Lỗi cố khi thực hiện đăng nhập'); // Hiển thị thông báo lỗi cho tài khoản bị khóa
        } 
      }
    );
  }
}
