import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse } from '@angular/common/http'; 

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(private router: Router, private http: HttpClient) { } 

  onLogout() {
    this.http.post('http://localhost:8080/api/auth/logout', {}).subscribe(
      () => {
        console.log('Đăng xuất thành công');
        localStorage.removeItem('loginToken');
        this.router.navigateByUrl('/register');
      },
      (error: HttpErrorResponse) => {
        if (error.status === 200 && error.statusText === 'OK' && error.error === 'Logout successfully') {
          console.log('Đăng xuất thành công');
          localStorage.removeItem('loginToken');
          this.router.navigateByUrl('/register');
        } else {
          console.error('Đăng xuất thất bại', error);
        }
      }
    );
  }
  
}
