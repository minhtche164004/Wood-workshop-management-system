import { Component } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-verify-mail',
  templateUrl: './verify-mail.component.html',
  styleUrls: ['./verify-mail.component.scss']
})
export class VerifyMailComponent {
  email: string = "";
  errorMessage: string = "";

  constructor(private http: HttpClient, private router: Router) { }

  verifyMail() {
 
    const apiUrl = `http://localhost:8080/api/forgotPassword/verifyMail/${this.email}`;

    this.http.post(apiUrl, {}, { responseType: 'text' }).subscribe(
      (response: any) => {
        console.log('Response:', response);
        if (typeof response === 'string' && response.includes("Check OTP đã được gửi đến gmail!")) {
          this.router.navigateByUrl('/verifyOtp');
          console.log('Email verified successfully');
        } else {
          console.error('Email verification failed: Unexpected response', response);
          this.errorMessage = "Unexpected response from server.";
        }
      },
      (error: HttpErrorResponse) => {
        console.error('Email verification failed', error);
        console.log('Error response:', error.error);

        if (typeof error.error === 'string') {
          this.errorMessage = error.error;
        } else if (error.error instanceof ErrorEvent) {
          this.errorMessage = `An error occurred: ${error.error.message}`;
        } else {
          this.errorMessage = `Server returned error ${error.status}: ${error.error}`;
        }
      }
    );
  }
}
