import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private urlGetAllOrderDetail = `${environment.apiUrl}api/auth/order/GetAllOrder`
  private urlFilterStatus =`${environment.apiUrl}auth/order/filter-by-status`
  private urlGetOrderStatus= `${environment.apiUrl}api/auth/order/getStatusOrder`
  constructor(private http: HttpClient) { }

  getAllOrderDetails(): Observable<any>{
    return this.http.get<any>(this.urlGetAllOrderDetail).pipe(
      catchError(this.handleError) 
    );
  }
  filterByStatus(order: number): Observable<any>{
    return this.http.get<any>(`this.urlFilterStatus?status_id=${order}`).pipe(
      catchError(this.handleError) 
    );
  }
  getOrderStatus(): Observable<any>{
    return this.http.get<any>(this.urlGetOrderStatus).pipe(
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
}
