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
  private apiUrlGetProduct = `${environment.apiUrl}api/auth/product/getAllProductForCustomer`;
  private apiUrl = `${environment.apiUrl}api/auth/product/getAllProductForAdmin`;
  private apiUrl_Cate = `${environment.apiUrl}api/auth/product/GetAllCategory`;
  private apiUrl_GetAllUser = `${environment.apiUrl}api/auth/admin/GetAllUser`;
  private apiUrl_Position = `${environment.apiUrl}api/auth/admin/GetAllPosition`;

  private apiUrl_findProduct = `${environment.apiUrl}api/auth/product`;
  private apiUrl_getProductByID = `${environment.apiUrl}api/auth/product/GetProductById`; // Assuming the correct endpoint

  private apiUrl_GetAllOrder = `${environment.apiUrl}api/auth/order/GetAllOrder`; // Assuming the correct endpoint


  private apiUrl_AllRole = `${environment.apiUrl}api/auth/admin/GetAllRole`; // Assuming the correct endpoint



  private apiUrl_AddProduct = `${environment.apiUrl}api/auth/product/AddNewProduct`;
<<<<<<< HEAD
  private apiUrl_UpdateProduct = `${environment.apiUrl}api/auth/product/AddNewProduct`;
  
=======

  private apiUrl_UpdateProduct = `${environment.apiUrl}api/auth/product/AddNewProduct`;

>>>>>>> 4026ae25d6e2a635c409e5db4ef671237b69b3c2
  constructor(private http: HttpClient) { }
  uploadProduct(productData: any, thumbnail: File, images: File[]): Observable<any> {
    const formData = new FormData();
    formData.append('productDTO', new Blob([JSON.stringify(productData)], { type: 'application/json' }));
    formData.append('file_thumbnail', thumbnail, thumbnail.name);
    images.forEach(image => {
      formData.append('files', image, image.name);
    });

    return this.http.post(this.apiUrl_AddProduct, formData, {
      headers: {
        'Accept': 'application/json'
      }
    });
  }



  
  getProducts(): Observable<any> {
    console.log(this.apiUrl)
    return this.http.get<any>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  getAllOrder(): Observable<any> {
    console.log(this.apiUrl_GetAllOrder)
    return this.http.get<any>(this.apiUrl_GetAllOrder).pipe(
      catchError(this.handleError)
    );
  }

  getAllProductCustomer(): Observable<any> {
    console.log(this.apiUrlGetProduct)
    return this.http.get<any>(this.apiUrlGetProduct).pipe(
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

  findProductByCategory(key: number): Observable<any> {
    return this.http.get<any>(`${this.api_findProductByCategory}/GetProductByCategory?id=${key}`);
  }

  getAllCategories(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_Cate);
  }

  getAllPosition(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_Position);
  }
  getAllRole(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_AllRole);
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
