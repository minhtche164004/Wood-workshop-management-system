import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderRequestService {
  private urlGetAllRequestOrder = `${environment.apiUrl}api/auth/order/GetAllRequest`;
  private urlGetRequestByID = `${environment.apiUrl}api/auth/order/GetRequestById`;
  private urlGetProductbyID = `${environment.apiUrl}api/auth/order/getRequestProductById`
  
  constructor(private http: HttpClient) { }

  getAllRequestOrder(): Observable<any>{
    return this.http.get<any>(this.urlGetAllRequestOrder).pipe(
      catchError(this.handleError) 
    );
  }
  private handleError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Xảy ra lỗi ở phía client hoặc mạng. Xử lý tương ứng.
      errorMessage = `An error occurred: ${error.error.message}`;
    } else {
      // Backend trả về mã lỗi không thành công.
      // Response body có thể cung cấp dấu hiệu về vấn đề gặp phải.
      errorMessage = `Backend returned code ${error.status}: ${error.error}`;
    }
    console.error(errorMessage); // Ghi log lỗi để debug
    return throwError(errorMessage); // Ném lỗi lại như một observable
  }
  getRequestById(orderId: number): Observable<any> {
    const url = `${this.urlGetRequestByID}?id=${orderId}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  getProductById(orderId: number): Observable<any> {
    const url = `${this.urlGetProductbyID}?id=${orderId}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
}
