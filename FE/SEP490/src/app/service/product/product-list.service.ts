import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductListService {

  private apiUrl = 'http://localhost:8080/api/auth/product/GetAllProduct';
  private apiUrl_Cate = 'http://localhost:8080/api/auth/product/getAllCategoryName';
  private apiUrl_GetAllUser = 'http://localhost:8080/api/auth/admin/GetAllUser';
  private apiUrl_Position = '  http://localhost:8080/api/auth/admin/GetAllPosition';
  private apiUrl_findProduct = 'http://localhost:8080/api/auth/product'
  private apiUrl_getProductByID = 'http://localhost:8080/api/auth/product/GetProductById'; // Assuming the correct endpoint

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
  getAllUser(): Observable<any> {
    return this.http.get<any>(this.apiUrl_GetAllUser);
  }

  findProductByNameOrCode(key: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl_findProduct}/findProductByNameorCode?key=${key}`);
  }
 
  getAllCategories(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_Cate);
  }
  getAllPosition(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_Position);
  }
  getProductById(productId: number): Observable<any> {
    const url = `${this.apiUrl_getProductByID}?product_id=${productId}`; // Construct URL with query parameter
    return this.http.get<any>(url); // Make the GET request with the constructed URL
  }
  }
