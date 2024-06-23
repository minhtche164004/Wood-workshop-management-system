import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductListService {

 

  private apiAddProduct = `${environment.apiUrl}api/auth/product/AddNewProduct`;
  private apiDeleteProduct = `${environment.apiUrl}api/auth/product/DeleteProduct`; // Assuming the delete endpoint
  private api_findProductByCategory = `${environment.apiUrl}api/auth/product`;

  private apiUrl = `${environment.apiUrl}api/auth/product/getAllProductForAdmin`;
  private apiUrl_Cate = `${environment.apiUrl}api/auth/product/getAllCategoryName`;
  private apiUrl_GetAllUser = `${environment.apiUrl}api/auth/admin/GetAllUser`;
  private apiUrl_Position = `${environment.apiUrl}api/auth/admin/GetAllPosition`;

  private apiUrl_findProduct = `${environment.apiUrl}api/auth/product`;
  private apiUrl_getProductByID = `${environment.apiUrl}api/auth/product/GetProductById`; // Assuming the correct endpoint

  constructor(private http: HttpClient) { }

  getProducts(): Observable<any> {
    console.log(this.apiUrl)
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
