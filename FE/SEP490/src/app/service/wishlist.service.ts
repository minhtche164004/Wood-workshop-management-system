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
  private handleError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Xảy ra lỗi ở phía client hoặc mạng. Xử lý tương ứng.
      errorMessage = `An error occurred: ${error.error.message}`;
    } else {
       errorMessage = `Backend returned code ${error.status}: ${error.error}`;
    }
    console.error(errorMessage); // Ghi log lỗi để debug
    return throwError(errorMessage); // Ném lỗi lại như một observable
  }
  addWishlist(productId: number): Observable<any> {
    const token = localStorage.getItem('loginToken');
  //   console.log("token: ", token)
    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

   // console.log("Authorization header:", headers.get('Authorization'));


    return this.http.post<any>(`${this.apiAddWishlist}?product_id=${productId}`, {}, { headers: headers, withCredentials: true }).pipe(
      catchError(this.handleError)
    );
  }
}
