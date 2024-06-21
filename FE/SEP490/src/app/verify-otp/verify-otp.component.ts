import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/app/environments/environment'; // Đường dẫn đúng tới file môi trường
@Component({
  selector: 'app-verify-otp',
  templateUrl: './verify-otp.component.html',
  styleUrls: ['./verify-otp.component.scss']
})
export class VerifyOtpComponent {
  otpverify: number = 0; // Use string instead of number for OTP input

  constructor(
    private http: HttpClient,
    private router: Router,
    private toastr: ToastrService
  ) {}

  validateOTP(otp: number): boolean {
    const otpString = otp.toString(); // Convert number to string for regex check
    const otpRegex = /^[0-9]{6}$/; // Regex to match exactly 6 digits (numbers only)
    return otpRegex.test(otpString);
  }

  verifyOtp(): void {
    if (!this.validateOTP(this.otpverify)) {
      this.toastr.error('OTP phải là 6 số và chỉ chứa số.', 'Lỗi');
      return;
    }


    const otp = this.otpverify; // Assign OTP value from component property

    // Send OTP verification request to backend
    this.http.post(`${environment.apiUrl}api/auth/verifyMail1/${otp}`, {}, { withCredentials: true }).subscribe(
      (response: any) => {
        console.log('OTP verified successfully', response);
        this.toastr.success('Đăng ký tài khoàn thành công! Bạn có thể đăng nhập ngay bây giờ', 'Thành công');
        this.router.navigate(['/register']); // Navigate to registration page on success
      },
      (error) => {
        console.error('OTP verification failed', error);
        this.toastr.error('Xác nhận OTP thất bại. Vui lòng thử lại.', 'Lỗi');
        
      }
    );
  }
}