import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class SupplierService {
  private supplierApi = `${environment.apiUrl}api/auth/supplier/GetAllSupplier` // Path to your JSON file
  private apiUrl_findProduct = `${environment.apiUrl}api/auth/supplier`
  private apiUrl_addSupplier = `${environment.apiUrl}api/auth/supplier/AddNewSupplier`
  private apiUrl_deleteSupplier = `${environment.apiUrl}api/auth/supplier/DeleteSupplier`;

  constructor(private http: HttpClient) { }
  getAllSuppliers(): Observable<any> {
    return this.http.get<any>(this.supplierApi).pipe(
      catchError(this.handleError) 
    );
  }
  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }

  findSupplierName(key: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl_findProduct}/SearchByName?key=${key}`);
  }
  addNewSupplier(supplierData: any): Observable<any> {
    
    return this.http.post<any>(this.apiUrl_addSupplier, supplierData).pipe(
      catchError(this.handleError)
    );
  }
  deleteSupplier(supplierId: number): Observable<any> {
    const url = `${this.apiUrl_deleteSupplier}?id=${supplierId}`;
    return this.http.delete<any>(url).pipe(
      catchError(this.handleError)
    );
  }
}
