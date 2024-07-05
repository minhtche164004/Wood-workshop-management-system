import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apigetProductSubMaterialByProductId = `${environment.apiUrl}api/auth/submaterial/getProductSubMaterialByProductId`;

  
  constructor(private http: HttpClient) { }

  getProductSubMaterialByProductId(productId: number, materialId: number): Observable<any> {
    const url = `${this.apigetProductSubMaterialByProductId}?id=${productId}&mate_id=${materialId}`;
    return this.http.get<any>(url);
  }
}
