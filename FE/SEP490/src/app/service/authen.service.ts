import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthenListService {
  private apiUrl_ViewProfile = 'http://localhost:8080/api/auth/user/ViewProfile';
  private apiUrl_UpdateProfile = 'http://localhost:8080/api/auth/user/UpdateProfile';

  constructor(private http: HttpClient) { }
  isLoggedIn(): boolean {
    const token = localStorage.getItem('loginToken');
    return !!token; // Trả về true nếu tồn tại token trong localStorage, ngược lại false
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

  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error.message);
    return throwError(error);
  }
}
