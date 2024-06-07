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

  onLogout(): void {
    // Lấy giá trị của token từ local storage
    const token = localStorage.getItem('loginToken');
    console.log('Token trước khi logout:', token);

    console.log('remove loginToken');
    // Xóa token đăng nhập khỏi local storage
    localStorage.removeItem('loginToken');
    console.log('Token sau khi logout:', localStorage.getItem('loginToken'));
    this.router.navigateByUrl('/register');
    
  }
  
}
