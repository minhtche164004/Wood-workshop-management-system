import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/app/environments/environment'; // Đường dẫn đúng tới file môi trường
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
    private toastr: ToastrService
  ) { }

  ngOnInit(): void {
    // Retrieve email from route parameters
    this.route.params.subscribe(params => {
      this.email = params['email'];
    });
  }

  validateEmail(email: string): boolean {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailRegex.test(email);
  }

  verifyMail(): void {
    if (!this.email || this.email.trim() === "") {
      this.toastr.error('Email không được để trống.', 'Lỗi');
      return;
    }

    if (!this.validateEmail(this.email)) {
      this.toastr.error('Email không hợp lệ.', 'Lỗi');
      return;
    }

    // Headers and options for the HTTP request
    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      responseType: 'text' as 'json'  // Specify 'text' response type
    };

    // HTTP POST request to verify email
    this.http.post<any>(
      `${environment.apiUrl}api/auth/forgotPassword/verifyMail/${this.email}`,
      {},
      options
    ).subscribe(
      (response) => {
        console.log('Response:', response);
        // Check if response contains the success message
        if (typeof response === 'string' && response.includes('Check OTP đã được gửi đến gmail!')) {
          console.log('Email verified successfully');
          this.toastr.success('Kiểm tra mã OTP đã được gửi đến email!', 'Thành công');
          this.router.navigate(['/verifyOtp', { email: this.email }]);
        } else {
          console.error('Email verification failed: Unexpected response', response);
          this.errorMessage = 'Unexpected response from server.';
        }
      },
      (error: HttpErrorResponse) => {
        console.error('Email verification failed', error);

        // Parse the error response to access its properties
        let errorResponse;
        try {
          errorResponse = JSON.parse(error.error);
        } catch (e) {
          errorResponse = { message: 'Unexpected error format' };
        }

        // Handle specific error codes from backend
        if (errorResponse.code === 1020) {
          this.toastr.error('Email không tồn tại! Vui lòng kiểm tra lại.', 'Lỗi khi thực hiện xác nhận');
        } else {
          this.toastr.error('Đã xảy ra lỗi trong quá trình xác nhận email.', 'Lỗi khi thực hiện xác nhận');
        }
      }
    );
  }
}
