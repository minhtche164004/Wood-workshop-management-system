import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-otp-mail',
  templateUrl: './verify-otp-mail.component.html',
  styleUrls: ['./verify-otp-mail.component.scss']
})
export class VerifyOtpMailComponent {
  otp: number = 0;
  email: string = "";
  constructor(private http: HttpClient, private router: Router) {}

  verifyOtp_Email() {
    const otp = this.otp; // Gán giá trị cho biến otp từ otpverify
    const email = this.email; // Gán giá trị cho biến otp từ otpverify
    this.http.post(`http://localhost:8080/api/forgotPassword/verifyOtp/${otp}/${email}`, {}, { withCredentials: true }).subscribe(
      (response: any) => {
        console.log('OTP verified successfully', response);
        this.router.navigate(['/change_pass']);
      },
      (error) => {
        console.error('OTP verification failed', error);
        alert('OTP verification failed: ' + error.error.message); // Cung cấp phản hồi cho người dùng
      }
    );
  }
}
