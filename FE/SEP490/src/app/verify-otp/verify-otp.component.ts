import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-otp',
  templateUrl: './verify-otp.component.html',
  styleUrls: ['./verify-otp.component.scss']
})
export class VerifyOtpComponent {
  otpverify: number = 0;

  constructor(private http: HttpClient, private router: Router) {}

  verifyOtp() {
    const otp = this.otpverify; // Gán giá trị cho biến otp từ otpverify
    this.http.post(`http://localhost:8080/api/auth/verifyMail1/${otp}`, {}, { withCredentials: true }).subscribe(
      (response: any) => {
        console.log('OTP verified successfully', response);
        this.router.navigate(['/register']);
      },
      (error) => {
        console.error('OTP verification failed', error);
        alert('OTP verification failed: ' + error.error.message); // Cung cấp phản hồi cho người dùng
      }
    );
  }
}
