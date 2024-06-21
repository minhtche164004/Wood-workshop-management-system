import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpClient ,HttpErrorResponse} from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../environments/environment';



interface JwtAuthenticationResponse {
  token: string;
  refreshToken: string;
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
 
  role: number;
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
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
  role: number = 0; // Default value for role
  errorMessage: string = '';
  successMessage: string = '';
  private apiUrl_registration = `${environment.apiUrl}api/auth/registration`; // URL của backend


  constructor(private elementRef: ElementRef, private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    const signUpButton = this.elementRef.nativeElement.querySelector('#signUp');
    const signInButton = this.elementRef.nativeElement.querySelector('#signIn');
    const container = this.elementRef.nativeElement.querySelector('#container');

    signUpButton.addEventListener('click', () => {
      container.classList.add('right-panel-active');
    });

    signInButton.addEventListener('click', () => {
      container.classList.remove('right-panel-active');
    });

    localStorage.removeItem('loginToken');
  }


 
  onLogin() {
    console.log("Bắt đầu chạy login");
    console.log("username: " + this.loginObj.username);
    console.log("password: " + this.loginObj.password);

    this.http.post(`${environment.apiUrl}api/auth/login`, this.loginObj).subscribe(
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

  registerUser() {
    const registrationRequest: RegistrationRequest = {
      username: this.username,
      password: this.password,
      checkPass: this.checkPass,
      email: this.email,
      phoneNumber: this.phoneNumber,
      address: this.address,
      fullname: this.fullname,
      status: 0,
      role: 0
    };
    console.log('Username:', this.username);
    console.log('Password:', this.password);
    console.log('Confirm Password:', this.checkPass);
    console.log('Email:', this.email);
    console.log('Phone Number:', this.phoneNumber);
    console.log('Address:', this.address);
    console.log('Fullname:', this.fullname);
    console.log('Status:', registrationRequest.status); // Log status
    console.log('Role:', registrationRequest.role); // Log role
    
 
  
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
