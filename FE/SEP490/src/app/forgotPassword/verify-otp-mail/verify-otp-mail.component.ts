
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/app/environments/environment'; // Đường dẫn đúng tới file môi trường

@Component({
  selector: 'app-verify-otp-mail',
  templateUrl: './verify-otp-mail.component.html',
  styleUrls: ['./verify-otp-mail.component.scss']
})

export class VerifyOtpMailComponent implements OnInit {
  otp: number;
  email: string;
  errorMessage: string;

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute, private toastr: ToastrService) {
    this.otp = 0;
    this.email = '';
    this.errorMessage = '';
  }
  validateOTP(otp: number): boolean {
    const otpString = otp.toString(); // Convert number to string for regex check
    const otpRegex = /^[0-9]{6}$/; // Regex to match exactly 6 digits (numbers only)
    return otpRegex.test(otpString);
  }

  verifyOtp(): void {
    if (!this.validateOTP(this.otp)) {
      this.toastr.error('OTP phải là 6 số và chỉ chứa số.', 'Lỗi');
      return;
    }
  }
  ngOnInit(): void {
    // Retrieve email from route parameters
    this.route.params.subscribe(params => {
      this.email = params['email'];
    });
  }

  verifyOtpEmail() {
    if (!this.email || !this.otp) {
      this.errorMessage = 'Please provide both OTP and Email.';
      return;
    }

    this.http.post(`${environment.apiUrl}api/auth/forgotPassword/verifyOtp/${this.otp}/${this.email}`, {}, { responseType: 'text' })
      .subscribe(
        (response) => {
          console.log('Response:', response);
          if (response.includes('OTP verified')) {
            console.log('OTP verified successfully');
            this.toastr.success('Xác nhận mã OTP thành công! Bạn có thể thay đổi mật khẩu', 'Thành công');
            this.router.navigate(['/change_pass', { email: this.email }]);
          } else {
            console.error('Unexpected response from server');
            this.errorMessage = 'Unexpected response from server.';
          }
        },
        (error: HttpErrorResponse) => {
           // Parse the error response to access its properties
        let errorResponse;
        try {
          errorResponse = JSON.parse(error.error);
        } catch (e) {
          errorResponse = { message: 'Unexpected error format' };
        }

        // Handle specific error codes from backend
        if (errorResponse.code === 1010) {
          this.toastr.error('Sai OTP, vui lòng nhập lại');
        } else  if (errorResponse.code === 1013) {
          this.toastr.error('OTP đã hết hạn , OTP mới đã được gửi, vui lòng check mail');
        }
        }
      );

  }
}
