import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-otp-mail',
  templateUrl: './verify-otp-mail.component.html',
  styleUrls: ['./verify-otp-mail.component.scss']
})
export class VerifyOtpMailComponent {
  otp: number = 0;
  constructor(private http: HttpClient, private router: Router) {}
  verifyOtp_Email() {
    const otp = this.otp;
    this.http.post(`http://localhost:8080/api/forgotPassword/verifyOtp/${otp}`,  {  withCredentials: true }).subscribe(
      (response: any) => {
        console.log('OTP verified successfully', response);
        this.router.navigate(['/change_pass']);
      },
      (error) => {
        console.error('OTP verification failed', error);
        if (error.status === 401) {
          alert('Session expired or unauthorized. Please log in again.');
          this.router.navigate(['/login']);
        } else {
          alert('OTP verification failed: ' + error.error.message);
        }
      }
    );
  }
}
