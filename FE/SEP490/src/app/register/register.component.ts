import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { ProvincesService } from 'src/app/service/provinces.service'; // Ensure correct path to your ProvincesService

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
  wards: Town[];
}

interface Town {
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
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  provinces: Province[] = [];
  districts: District[] = [];
  wards: Town[] = [];

  selectedProvince = '-1';
  selectedDistrict = '-1';
  selectedWard = '-1';
 


  loginObj: any = {
    "username": "",
    "password": ""
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
  private apiUrl_registration = 'http://localhost:8080/api/auth/registration'; // URL của backend



  constructor(private elementRef: ElementRef, private http: HttpClient, private router: Router, private dataService: ProvincesService) {}

  ngOnInit(): void {
    this.getProvinces();
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

  getProvinces() {
    this.dataService.getProvinces().subscribe(
      (data: Province[]) => {
        this.provinces = data;
      },
      (error: any) => {
        console.error('Error fetching provinces:', error);
        // Handle error as needed
      }
    );
  
  }

  onProvinceChange() {
    const selectedProvinceObject = this.provinces.find(p => p.code.toString() === this.selectedProvince);
    if (selectedProvinceObject) {
      this.districts = selectedProvinceObject.districts;
      this.selectedDistrict = '-1';
      this.selectedWard = '-1';
    }
  }

  onDistrictChange() {
    const selectedDistrictObject = this.districts.find(d => d.code.toString() === this.selectedDistrict);
    if (selectedDistrictObject) {
      this.wards = selectedDistrictObject.wards;
      this.selectedWard = '-1';
    }
  }


  onLogin(): void {
    console.log("Bắt đầu chạy login");
    console.log("username: " + this.loginObj.username);
    console.log("password: " + this.loginObj.password);

    this.http.post('http://localhost:8080/api/auth/login', this.loginObj).subscribe(
      (response: any) => {
        console.log('Truy cập API login thành công');
        if (response.code === 1000) {
          console.log("Đăng nhập thành công: " + response.code);

          // Extract and store access token
          const token = response.result.token;
          console.log("Access Token:", token);
          localStorage.setItem('loginToken', token);

          // Extract user data
          const userData = response.result.user;
          console.log("User Data:", userData);

          // Print authorities to console
          console.log("Authorities:", userData.authorities);

          // Determine and redirect based on authorities
          const authorities = userData.authorities.map((authority: { authority: any; }) => authority.authority);
          if (authorities.includes('CUSTOMER')) {
            this.router.navigateByUrl('/homepage');
          } else if (authorities.includes('ADMIN')) {
            this.router.navigateByUrl('/admin');
          } else if (authorities.includes('EMPLOYEE')) {
            this.router.navigateByUrl('/employee-work');
          } else {
            console.error('Invalid user role:', authorities);
            // Handle invalid role (e.g., display error message)
          }
        } 
      },
      (error: HttpErrorResponse) => {
        console.error('Lỗi khi đăng nhập', error);
        if (error.status === 400 && error.error.code === 1006 && error.error.message === "Sai Tên đăng nhập hoặc mật khẩu") {
          console.log("Sai tên đăng nhập hoặc mật khẩu");
          // Xử lý thông báo lỗi hoặc hành động phù hợp khi sai tên đăng nhập hoặc mật khẩu
        }
        if (error.error.code === 1014) {
          console.log("Account bị ban");
          // Xử lý thông báo lỗi hoặc hành động phù hợp khi account bị ban
        } if (error.error.code === 1004) {
          console.log("Phiên đăng nhập hết hạn. Vui lòng đăng nhập lại");
          // Xử lý thông báo lỗi hoặc hành động phù hợp khi account bị ban
        }

      }
    );
  }

  registerUser(): void {
    const registrationRequest: RegistrationRequest = {
      username: this.username,
      password: this.password,
      checkPass: this.checkPass,
      email: this.email,
      phoneNumber: this.phoneNumber,
      address: this.address,
      fullname: this.fullname,
      status: 0,
    };

    console.log('Username:', this.username);
    console.log('Password:', this.password);
    console.log('Confirm Password:', this.checkPass);
    console.log('Email:', this.email);
    console.log('Phone Number:', this.phoneNumber);
    console.log('Address:', this.address);
    console.log('Fullname:', this.fullname);
    console.log('Status:', registrationRequest.status); // Log status

    this.http.post<any>(this.apiUrl_registration, registrationRequest, { withCredentials: true })
      .subscribe(() => {
        console.log('Check Mail để kiểm tra OTP'); // Display success message
        this.router.navigate(['/otp']);
      }, (error: any) => {
        console.error('Đăng ký thất bại', error);
        this.errorMessage = 'Registration failed. Please try again.'; // Display error message
      });
  }

  
 
 
}
