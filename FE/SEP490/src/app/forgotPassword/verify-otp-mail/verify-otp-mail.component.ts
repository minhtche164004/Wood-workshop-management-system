import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-verify-otp-mail',
  templateUrl: './verify-otp-mail.component.html',
  styleUrls: ['./verify-otp-mail.component.scss']
})
export class VerifyOtpMailComponent implements OnInit {
  otp: number;
  email: string;
  errorMessage: string;

  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) {
    this.otp = 0;
    this.email = '';
    this.errorMessage = '';
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

    this.http.post(`http://localhost:8080/api/auth/forgotPassword/verifyOtp/${this.otp}/${this.email}`, {}, { responseType: 'text' })
      .subscribe(
        (response) => {
          console.log('Response:', response);
          if (response.includes('OTP verified')) {
            console.log('OTP verified successfully');
            this.router.navigate(['/change_pass', { email: this.email }]);
          } else {
            console.error('Unexpected response from server');
            this.errorMessage = 'Unexpected response from server.';
          }
        },
        (error: HttpErrorResponse) => {
          console.error('OTP verification failed', error);
          if (error.error instanceof ErrorEvent) {
            this.errorMessage = `An error occurred: ${error.error.message}`;
          } else {
            this.errorMessage = `Server returned error ${error.status}: ${error.error}`;
          }
        }
      );
  }
}
