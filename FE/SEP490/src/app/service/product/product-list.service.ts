import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProductListService {

  private apiUrl = 'http://localhost:8080/api/auth/product/GetAllProduct';
  private apiUrl_Cate = 'http://localhost:8080/api/auth/product/GetAllCategory';
  private apiUrl_GetAllUser = 'http://localhost:8080/api/auth/admin/GetAllUser';
  private apiUrl_Position = 'http://localhost:8080/api/auth/admin/GetAllPosition';
  private apiUrl_findProduct = 'http://localhost:8080/api/auth/product';
  private apiUrl_getProductByID = 'http://localhost:8080/api/auth/product/GetProductById'; // Assuming the correct endpoint
  private apiAddProduct = `http://localhost:8080/api/auth/product/AddNewProduct`;
  private apiDeleteProduct = `http://localhost:8080/api/auth/product/DeleteProduct`; // Assuming the delete endpoint
  private api_findProductByCategory = `http://localhost:8080/api/auth/product`
  constructor(private http: HttpClient) { }

  getProducts(): Observable<any> {
    return this.http.get<any>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }
  // , headers: HttpHeaders //, { headers }
  addNewProduct(requestData: any): Observable<any> {
    return this.http.post<any>(this.apiAddProduct, requestData).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: any) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `An error occurred: ${error.error.message}`;
    } else {
      errorMessage = `Backend returned code ${error.status}: ${error.error}`;
    }
    console.error(errorMessage);
    return throwError(errorMessage);
  }

  getAllUser(): Observable<any> {
    return this.http.get<any>(this.apiUrl_GetAllUser);
  }

  findProductByNameOrCode(key: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl_findProduct}/findProductByNameorCode?key=${key}`);
  }

  findProductByCategory(key: string): Observable<any> {
    return this.http.get<any>(`${this.api_findProductByCategory}/GetProductByCategory?id=${key}`);
  }

  getAllCategories(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_Cate);
  }

  getAllPosition(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_Position);
  }

  getProductById(productId: number): Observable<any> {
    const url = `${this.apiUrl_getProductByID}?product_id=${productId}`;
    return this.http.get<any>(url);
  }
  deleteProduct(productId: number): Observable<any> {
    const url = `${this.apiDeleteProduct}?product_id=${productId}`;
    return this.http.delete<any>(url).pipe(
      catchError(this.handleError)
    );
  }
}
