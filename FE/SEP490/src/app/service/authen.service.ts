import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment'; // Đường dẫn đúng tới file môi trường
@Injectable({
  providedIn: 'root'
})
export class AuthenListService {
  private apiUrl_ViewProfile = `${environment.apiUrl}api/auth/user/ViewProfile`;
  private apiUrl_UpdateProfile = `${environment.apiUrl}api/auth/user/UpdateProfile`;
  private apiUrl_ChangePass = `${environment.apiUrl}api/auth/user/ChangePass`;
  private apiUrl_GetById = `${environment.apiUrl}api/auth/admin/GetById`;
  private apiUrl_EditUser = `${environment.apiUrl}api/auth/admin/EditUser`;
  private apiUrl_GetByIdWishList = `${environment.apiUrl}api/auth/order/GetWhiteListByUser`;
  private apiUrl_GetHistoryOrderCustomer = `${environment.apiUrl}api/auth/order/historyOrder`;
  constructor(private http: HttpClient) { }
  isLoggedIn(): boolean {
    const token = localStorage.getItem('loginToken');
    return token !== null; // Trả về true nếu tồn tại token trong localStorage, ngược lại false
  }
  

  getUserById(user_id: string): Observable<any> {
    const url = `${this.apiUrl_GetById}?user_id=${user_id}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  getHistoryOrderCustomer(): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.get<any>(this.apiUrl_GetHistoryOrderCustomer, { headers }).pipe(
      catchError(this.handleError)
    );
  }


  GetByIdWishList(): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.get<any>(this.apiUrl_GetByIdWishList, { headers }).pipe(
      catchError(this.handleError)
    );
  }
    
  editUserById(user_id: string, userData: any): Observable<any> {
    const token = localStorage.getItem('loginToken');
    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const url = `${this.apiUrl_EditUser}?user_id=${user_id}`;
    return this.http.put<any>(url, userData, { headers }).pipe(
      catchError(this.handleError)
    );
  }



  getUserProfile(): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.get<any>(this.apiUrl_ViewProfile, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  updateUserProfile(userProfile: any): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.http.put<any>(this.apiUrl_UpdateProfile, userProfile, { headers }).pipe(
      catchError(this.handleError)
    );
  }


  getChangePass(oldPassword: string, newPassword: string): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const body = {
      old_pass: oldPassword,
      new_pass: newPassword,
      check_pass: newPassword // Assuming check_pass is for password confirmation
    };

    return this.http.put<any>(this.apiUrl_ChangePass, body, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error.message);
    return throwError(error);
  }
}
