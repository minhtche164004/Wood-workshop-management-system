import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { EmailService } from 'src/app/service/email.service'; // Import EmailService

@Component({
  selector: 'app-verify-mail',
  templateUrl: './verify-mail.component.html',
  styleUrls: ['./verify-mail.component.scss']
})
export class VerifyMailComponent implements OnInit {
  email: string = '';
  errorMessage: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private emailService: EmailService
  ) { }


  ngOnInit(): void {
    // Retrieve email from route parameters
    this.route.params.subscribe(params => {
      const email = params['email'];
      this.emailService.setEmail(email); // Save email to service
    });
  }

  verifyMail(): void {
    if (!this.email || !this.email.includes('@')) {
      this.errorMessage = 'Please enter a valid email address.';
      return;
    }

    // Specify response type as 'text' to prevent JSON parsing
    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      responseType: 'text' as 'json'  // Specify 'text' response type
    };

    this.http.post<any>(
      `http://localhost:8080/api/auth/forgotPassword/verifyMail/${this.email}`,
      {},
      options  // Pass options with responseType 'text'
    ).subscribe(
      (response) => {
        console.log('Response:', response);
        // Check if response contains the success message
        if (typeof response === 'string' && response.includes('Check OTP đã được gửi đến gmail!')) {
          console.log('Email verified successfully');
          // Navigate to OTP verification component and pass email as a route parameter
          this.router.navigate(['/verifyOtp', { email: this.email }]);

        } else {
          console.error('Email verification failed: Unexpected response', response);
          this.errorMessage = 'Unexpected response from server.';
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
