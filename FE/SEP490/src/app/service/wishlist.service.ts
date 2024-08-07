import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class WishlistService {
  private apiGetWhiteListByUserID = `${environment.apiUrl}api/auth/order/GetWhiteListByUserID`
  private apiAddWishlist = `${environment.apiUrl}api/auth/order/AddWhiteList`
  constructor(private http: HttpClient) { }

  GetWhiteListByUserID(): Observable<any>{
    return this.http.get<any>(this.apiGetWhiteListByUserID).pipe(
      catchError(this.handleError) 
    );
  }
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error.message);
    return throwError(error);
  }

  addWishlist(productId: number): Observable<any> {
    const token = localStorage.getItem('loginToken');
    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  
    return this.http.post<any>(`${this.apiAddWishlist}?product_id=${productId}`, {}, { headers: headers, withCredentials: true }).pipe(
      catchError(this.handleError)
    );
  }
}
