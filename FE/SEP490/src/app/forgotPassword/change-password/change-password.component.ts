import { Component } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent {

  email: string = '';
  password: string = '';
  repeatPassword: string = '';
  baseUrl = 'http://localhost:8080/api';
  errorMessage: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  onSubmit(): void {
    console.log('New Password:', this.password); // Log new password
    console.log('Confirm Password:', this.repeatPassword); // Log confirm password


    const url = `${this.baseUrl}/forgotPassword/changePassword/${encodeURIComponent(this.email)}`;
    this.http.post<any>(url, { password: this.repeatPassword })
      .subscribe(
        (response) => {
          console.log('Password change successful', response);
          this.errorMessage = ''; // Clear any previous error messages
          alert('Password changed successfully!');
          this.router.navigate(['/register'], { relativeTo: this.route }); // Navigate to /login after successful password change
        },
        (error: HttpErrorResponse) => {
          console.error('Error changing password', error);
          if (error.status === 400 && error.error && error.error.message) {
            this.errorMessage = error.error.message; // Display server-side error message
          } else {
            this.errorMessage = 'An unexpected error occurred. Please try again.'; // Generic error message
          }
        }
      );
  }
}