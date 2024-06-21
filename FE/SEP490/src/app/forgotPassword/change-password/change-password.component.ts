import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/app/environments/environment'; // Đường dẫn đúng tới file môi trường
@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  email: string = '';
  password: string = '';
  repeatPassword: string = '';
  errorMessage: string = '';
  baseUrl = `${environment.apiUrl}api/auth/forgotPassword`;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.email = params['email'];
    });
  }

  validatePassword(): boolean {
    if (this.password.length < 8) {
      this.toastr.error('Mật khẩu phải có ít nhất 8 ký tự.', 'Lỗi xác thực');
      return false;
    }
    if (this.password !== this.repeatPassword) {
      this.toastr.error('Mật khẩu không khớp.', 'Lỗi xác thực');
      return false;
    }
    return true;
  }

  onSubmit(): void {
    if (!this.validatePassword()) {
      return;
    }

    const options = {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
      responseType: 'text' as 'json'
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
        console.log('Đổi mật khẩu thành công', response);
        if (response.includes('Password has been changed!')) {
          this.errorMessage = '';
          this.toastr.success('Mật khẩu đã được thay đổi thành công!', 'Thành công');
          this.router.navigate(['/login']);
        } else {
          console.error('Phản hồi không mong muốn từ máy chủ:', response);
          this.errorMessage = 'Đã xảy ra lỗi không mong muốn. Vui lòng thử lại.';
        }
      },
      (error: HttpErrorResponse) => {
        console.error('Lỗi đổi mật khẩu', error);
        if (error.status === 400 && error.error) {
          this.errorMessage = error.error;
          this.toastr.error(this.errorMessage, 'Lỗi');
        } else {
          console.error('Chi tiết lỗi không mong muốn:', error);
          this.errorMessage = 'Đã xảy ra lỗi không mong muốn. Vui lòng thử lại.';
          this.toastr.error(this.errorMessage, 'Lỗi');
        }
      }
    );
  }
}
