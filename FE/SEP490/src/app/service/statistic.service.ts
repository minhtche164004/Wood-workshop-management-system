import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class StatisticService {
  private apiTotalAmountOrderHaveDone = `${environment.apiUrl}api/auth/statistic/totalAmountOrderHaveDone`

  private apiTotalAmountSubMaterial = `${environment.apiUrl}api/auth/statistic/totalAmountSubMaterial`

  private apifindTotalSalaryByMonthAndYear = `${environment.apiUrl}api/auth/statistic/findTotalSalaryByMonthAndYear`

  private apiTotalOrder = `${environment.apiUrl}api/auth/statistic/countTotalOrder
`
  private apiCountSpecialOrder = `${environment.apiUrl}api/auth/statistic/countSpecialOrder`

  private apiCountProduct = `${environment.apiUrl}api/auth/statistic/countProduct`

  private apiCountEmployeeWithTypePosition = `${environment.apiUrl}api/auth/statistic/countEmployeeWithTypePosition`

  private apiCountCompletedJobsByMonthAndYear = `${environment.apiUrl}api/auth/statistic/countCompletedJobsByMonthAndYear`
  constructor(private http: HttpClient) { }

  getTotalAmountOrderHaveDone(): Observable<any> {
    return this.http.get<any>(this.apiTotalAmountOrderHaveDone);
  }

  getTotalAmountSubMaterial(): Observable<any> {
    return this.http.get<any>(this.apiTotalAmountSubMaterial);
  }

  findTotalSalaryByMonthAndYear(month: number, year: number): Observable<any> {
    const url = `${this.apifindTotalSalaryByMonthAndYear}/findTotalSalaryByMonthAndYear`;
    const params = new HttpParams()
      .set('month', month.toString())
      .set('year', year.toString());

    return this.http.get<any>(url, { params });
  }

  getTotalOrder(): Observable<any> {
    return this.http.get<any>(this.apiTotalOrder);
  }

  getCountSpecialOrder(): Observable<any> {
    return this.http.get<any>(this.apiCountSpecialOrder);
  }

  getCountProduct(): Observable<any> {
    return this.http.get<any>(this.apiCountProduct);
  }

  countEmployeeWithTypePosition(query: number): Observable<any> {
    const url = `${this.apiCountEmployeeWithTypePosition}`;
    const params = new HttpParams()
      .set('query', query.toString());

    return this.http.get<any>(url, { params });
  }

  getCompletedJobsByMonthAndYear(statusName: any, month: number, year: number): Observable<any> {
    const params = {
      status_name: statusName,
      month: month,
      year: year
    };

    return this.http.get(this.apiCountCompletedJobsByMonthAndYear, { params });
  }
}
