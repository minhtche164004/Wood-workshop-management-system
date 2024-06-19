import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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
    return this.http.get<any>(this.apiUrl);
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


 

  

  

