import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
@Injectable({
  providedIn: 'root'
})
export class SupplierService {
  private supplierApi = 'http://localhost:8080/api/auth/supplier/GetAllSupplier'; // Path to your JSON file
  private apiUrl_findProduct = `http://localhost:8080/api/auth/supplier`
  private apiUrl_addSupplier = `http://localhost:8080/api/auth/supplier/AddNewSupplier`
  constructor(private http: HttpClient) { }
  getAllSuppliers(): Observable<any> {
    return this.http.get<any>(this.supplierApi).pipe(
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

  findSupplierName(key: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl_findProduct}/SearchByName?key=${key}`);
  }
  addNewSupplier(supplierData: any): Observable<any> {
    
    return this.http.post<any>(this.apiUrl_addSupplier, supplierData).pipe(
      catchError(this.handleError)
    );
  }
}
