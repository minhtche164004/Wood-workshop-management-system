import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/app/environments/environment';

@Component({
  selector: 'app-verify-otp',
  templateUrl: './verify-otp.component.html',
  styleUrls: ['./verify-otp.component.scss']
})
export class VerifyOtpComponent {
  otpverify: string = ''; // Use string instead of number for OTP input
  isLoading = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private toastr: ToastrService
  ) {}

  validateOTP(otp: string): boolean {
    const otpRegex = /^[0-9]{6}$/; // Regex to match exactly 6 digits (numbers only)
    return otpRegex.test(otp);
  }

  verifyOtp(): void {
    this.isLoading = true;
    
    if (!this.validateOTP(this.otpverify)) {
      this.toastr.error('OTP phải là 6 số và chỉ chứa số.', 'Lỗi');
      this.isLoading = false;
      return;
    }

    const otp = this.otpverify; // Assign OTP value from component property

    // Send OTP verification request to backend
    this.http.post(`${environment.apiUrl}api/auth/verifyMail1/${otp}`, {}, { withCredentials: true }).subscribe(
      (response: any) => {
        console.log('OTP verified successfully', response);
        this.toastr.success('Xác minh OTP thành công! Bạn có thể đăng nhập ngay bây giờ', 'Thành công');
        this.router.navigate(['/login']); // Navigate to registration page on success
      
      },
      (error) => {
        console.error('OTP verification failed', error);
        this.toastr.error('Xác nhận OTP thất bại. Vui lòng thử lại.', 'Lỗi');
        
      }
    )
    .add(() => {
      this.isLoading = false; // Stop loading after API call completes
  });
  }
}
