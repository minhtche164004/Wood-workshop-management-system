import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductListService {

  private apiUrl = 'http://localhost:8080/api/auth/product/GetAllProduct';

  constructor(private http: HttpClient) { }

  getProducts(): Observable<any> {
    return this.http.get<any>(this.apiUrl).pipe(
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