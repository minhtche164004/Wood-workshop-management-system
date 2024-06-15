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
    if (!this.email || !this.email.includes('@')) {
      this.errorMessage = "Please enter a valid email address.";
      return;
    }
    
    this.http.post<any>(
      `http://localhost:8080/api/forgotPassword/verifyMail/${this.email}`,
      {}
    ).subscribe(
      (response) => {
        console.log('Response:', response);
        if (typeof response === 'string' && response.includes("Check OTP đã được gửi đến gmail!")) {
          console.log('Email verified successfully');
          this.router.navigate(['/verifyOtp']);
        } else {
          console.error('Email verification failed: Unexpected response', response);
          this.errorMessage = "Unexpected response from server.";
        }
      },
      (error: HttpErrorResponse) => {
        console.error('Email verification failed', error);
        if (error.error instanceof ErrorEvent) {
          this.errorMessage = `An error occurred: ${error.error.message}`;
        } else {
          this.errorMessage = `Server returned error ${error.status}: ${error.error}`;
        }
      }
    );
  }
}
