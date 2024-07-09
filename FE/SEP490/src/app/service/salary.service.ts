import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class SalaryService {

  private apiGetSalary = `${environment.apiUrl}api/auth/salary/getAllSalary`;

  private apiMultiSearchSalary = `${environment.apiUrl}api/auth/salary/getMultiFillterSalary`;

  private apiUrl = 'https://api.vietqr.io/v2/banks';
  constructor(private http: HttpClient) { }

  getSalary(): Observable<any> {
    return this.http.get<any>(this.apiGetSalary);
  }
 
  getBanks(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  multSearchSalary(employeeName: string, fromDate: string, toDate: string, sortDirection: string): Observable<any> {
    const params = {
      employeeName: employeeName,
      fromDate: fromDate,
      toDate: toDate,
      sortDirection: sortDirection
    };
    
    const queryString = Object.entries(params)
      .filter(([key, value]) => value != null && value !== '') // Loại bỏ các tham số có giá trị null, undefined hoặc rỗng
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');
  
    const url = `${this.apiMultiSearchSalary}?${queryString}`;
    console.log("total url search: ",url);
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
