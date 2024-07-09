import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class ErrorProductService {
  private deleteProductErrorUrl = `${environment.apiUrl}api/auth/job/DeleteProductError`;

  private getError = `${environment.apiUrl}api/auth/job/getAllProductError`;
  private errorDetail = `${environment.apiUrl}api/auth/job/getAllProductErrorDetail`

  constructor(private http: HttpClient) { }
  deleteProductError(productId: number): Observable<any> {
    const url = `${this.deleteProductErrorUrl}?id=${productId}`;
    return this.http.delete<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  getAllProductError(): Observable<any> {
    return this.http.get<any>(this.getError).pipe(
      catchError(this.handleError)
    );
  }

  getRrrorDetail(productId: number): Observable<any> {
    const url = `${this.errorDetail}?id=${productId}`;
    return this.http.get<any>(url).pipe(
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
}
