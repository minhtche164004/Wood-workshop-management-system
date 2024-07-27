import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ProvincesService } from '../service/provinces.service';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-login-admin',
  templateUrl: './login-admin.component.html',
  styleUrls: ['./login-admin.component.scss']
})
export class LoginAdminComponent {
  isLoading = false;
  loginObj: any = {
    username: '',
    password: ''
  };

  constructor(
    private elementRef: ElementRef,
    private toastr: ToastrService, // Inject ToastrService
    private http: HttpClient,
    private router: Router,
    private provincesService: ProvincesService
  ) { }
  onLogin(): void {
    this.isLoading = true;
    this.http.post(`${environment.apiUrl}api/auth/login`, this.loginObj).subscribe(
      (response: any) => {
        console.log('Truy cập API đăng nhập thành công');
        if (response.code === 1000) {
          console.log('Đăng nhập thành công:', response.code); 
         
          const token = response.result.token;
          console.log('Access Token:', token);
          localStorage.setItem('loginToken', token);

          const userData = response.result.user;
          console.log('Thông tin người dùng:', userData);

          const authorities = userData.authorities.map((authority: { authority: any }) => authority.authority);
          if (authorities.includes('MANAGER')) {
            this.router.navigateByUrl('/static-report');
            this.toastr.success('Đăng nhập thành công!', 'Thành công');
          } else if (authorities.includes('ADMIN')) {
            this.router.navigateByUrl('/admin_manager_user');
            this.toastr.success('Đăng nhập thành công!', 'Thành công');
          } else if (authorities.includes('EMPLOYEE')) {
            this.router.navigateByUrl('/employee');
            this.toastr.success('Đăng nhập thành công!', 'Thành công');
          } else {
            console.error('Vai trò người dùng không hợp lệ:', authorities);
            this.toastr.error('Vai trò người dùng không hợp lệ', 'Lỗi cố khi thực hiện đăng nhập');
          }
        }
        this.isLoading = false; // Stop the loading spinner
      },
      (error: HttpErrorResponse) => {
        console.error('Lỗi khi đăng nhập', error);
        if (error.status === 400 && error.error.code === 1028) {
          this.toastr.error('Sai Tên đăng nhập ! Vui lòng kiểm tra lại', 'Lỗi cố khi thực hiện đăng nhập');
        } else if (error.status === 400 && error.error.code === 1006) {
          this.toastr.error('Sai Mật khẩu ! Vui lòng kiểm tra lại', 'Lỗi cố khi thực hiện đăng nhập');
        }
        this.isLoading = false; // Stop the loading spinner on error
      }
    );
  }
} 
