import { Component } from '@angular/core';

import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent {

  email: string = '';
  password: string = '';
  repeatPassword: string = '';

  baseUrl = 'http://localhost:8080/api/auth/forgotPassword';

  errorMessage: string = '';
  constructor(private http: HttpClient, private router: Router, private route: ActivatedRoute) { }

  validatePassword(): boolean {
    // Add your password validation logic here
    return this.password === this.repeatPassword;
  }
  ngOnInit(): void {
    // Retrieve email from route parameters
    this.route.params.subscribe(params => {
      this.email = params['email'];
    });
  }
  onSubmit(): void {
    if (!this.validatePassword()) {
      this.errorMessage = "Passwords must match.";
      return;
    }

    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      responseType: 'text' as 'json' // Ensure response type is set to 'text'
    };

    const payload = {
      password: this.password,
      repeatPassword: this.repeatPassword
    };

    this.http.post<any>(
      `${this.baseUrl}/changePassword/${encodeURIComponent(this.email)}`,
      payload,
      options
    ).subscribe(
      (response) => {
        console.log('Password change successful', response);
        // Assuming the response is in text format, handle it accordingly
        if (response.includes('Password has been changed!')) {
          this.errorMessage = ''; // Clear any previous error messages
          alert('Password has been changed successfully!');
          this.router.navigate(['/register']); // Navigate upon successful verification
        } else {
          console.error('Unexpected response from server:', response);
          this.errorMessage = 'An unexpected error occurred. Please try again.';
        }
      },
      (error: HttpErrorResponse) => {
        console.error('Error changing password', error);
        if (error.status === 400 && error.error) {
          this.errorMessage = error.error; // Display server-side error message
        } else {
          console.error('Unexpected error details:', error);
          this.errorMessage = 'An unexpected error occurred. Please try again.'; // Generic error message
        }
      }
    );
  }
}
