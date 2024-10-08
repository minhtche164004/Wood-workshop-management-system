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

  private apiCountRQProduct = `${environment.apiUrl}api/auth/statistic/countRequestProduct`

  private apiCountEmployeeWithTypePosition = `${environment.apiUrl}api/auth/statistic/countEmployeeWithTypePosition`

  private apiCountCompletedJobsByMonthAndYear = `${environment.apiUrl}api/auth/statistic/countCompletedJobsByMonthAndYear`

  private apiCountProductByMonthYear = `${environment.apiUrl}api/auth/statistic`;

  private apiCountTotalOrderByMonthAndYear = `${environment.apiUrl}api/auth/statistic/countTotalOrderByMonthAndYear`

  private apiCountTotalSpecOrderByMonthAndYear = `${environment.apiUrl}api/auth/statistic/countTotalSpecialOrderByMonthAndYear`

  private apiCheckJobError = `${environment.apiUrl}api/auth/job/checkErrorOfJobHaveFixDoneOrNot`

  private apiFindTotalInputSubMaterialByMonthAndYear = `${environment.apiUrl}api/auth/statistic`

  private apiFindTotalSalaryByMonthAndYear = `${environment.apiUrl}api/auth/statistic`

  private apiTotalSalaryNotPayment = `${environment.apiUrl}api/auth/statistic/TotalSalaryNotPayment`

  private apiGetTotalSalaryNotPayment = `${environment.apiUrl}api/auth/statistic/TotalSalaryNotPayment`;

  private apiGetTotalSalaryAllPayment = `${environment.apiUrl}api/auth/statistic/TotalSalaryAllPayment`;

  constructor(private http: HttpClient) { }

  getTotalSalaryAllPayment(year: number): Observable<any> {
    const url = `${this.apiGetTotalSalaryAllPayment}?year=${year}`;
    console.log('URL:', url);
    return this.http.get<any>(url);
  }
  
  statisticFindTotalSalaryByMonthAndYear(month: number, year: number): Observable<any> {
    const params = new HttpParams()
      .set('month', month.toString())
      .set('year', year.toString());

    return this.http.get<any>(`${this.apiFindTotalSalaryByMonthAndYear}/findTotalSalaryByMonthAndYear`, { params });
  }
  findTotalInputSubMaterialByMonthAndYear(month: number, year: number): Observable<any> {
    const params = new HttpParams()
      .set('month', month.toString())
      .set('year', year.toString());

    return this.http.get<any>(`${this.apiFindTotalInputSubMaterialByMonthAndYear}/findTotalInputSubMaterialByMonthAndYear`, { params });
  }
  countCompletedRequestProductOnOrderByMonthAndYear(month: number, year: number): Observable<any> {
    const url = `${this.apiCountProductByMonthYear}/countCompletedRequestProductOnOrderByMonthAndYear`;
    const params = new HttpParams().set('month', month.toString()).set('year', year.toString());
    return this.http.get<any>(url, { params });
  }

  checkErrorOfJobHaveFixDoneOrNot(jobId: number): Observable<any> {
    const url = `${this.apiCheckJobError}?job_id=${jobId}`;
    return this.http.get<any>(url);
  }
  countCompletedProductOnOrderByMonthAndYear(month: number, year: number): Observable<any> {
    const url = `${this.apiCountProductByMonthYear}/countCompletedProductOnOrderByMonthAndYear`;
    const params = new HttpParams().set('month', month.toString()).set('year', year.toString());
    return this.http.get<any>(url, { params });
  }
  countTotalOrderByMonthAndYear(month: number, year: number): Observable<any> {
    const params = new HttpParams()
      
      .set('month', month.toString())
      .set('year', year.toString());

    return this.http.get(`${this.apiCountTotalOrderByMonthAndYear}`, { params });
  }
  countTotalSpecOrderByMonthAndYear(month: number, year: number): Observable<any> {
    const params = new HttpParams()
      
      .set('month', month.toString())
      .set('year', year.toString());

    return this.http.get(`${this.apiCountTotalSpecOrderByMonthAndYear}`, { params });
  }
  getTotalAmountOrderHaveDone(year: number): Observable<any> {
    const params = new HttpParams().set('year', year.toString());
    return this.http.get<any>(this.apiTotalAmountOrderHaveDone, { params });
  }
  

  getTotalAmountSubMaterial(): Observable<any> {
    return this.http.get<any>(this.apiTotalAmountSubMaterial);
  }

  findTotalSalaryByMonthAndYear(month: number, year: number): Observable<any> {
    const url = `${this.apifindTotalSalaryByMonthAndYear}`;
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
  getCountRQProduct(): Observable<any> {
    return this.http.get<any>(this.apiCountRQProduct);
  }

  getTotalSalaryNotPayment(): Observable<any> {
    return this.http.get<any>(this.apiTotalSalaryNotPayment);
  }
  countProductByMonthYear(status_id: number, month: number, year: number): Observable<any> {
    const params = new HttpParams()
      .set('status_id', status_id.toString())
      .set('month', month.toString())
      .set('year', year.toString());

    return this.http.get(`${this.apiCountProductByMonthYear}/countCompletedJobsForProductByMonthAndYear`, { params });
  }

  countProductRequestByMonthYear(status_id: number, month: number, year: number): Observable<any> {
    const params = new HttpParams()
      .set('status_id', status_id.toString())
      .set('month', month.toString())
      .set('year', year.toString());

    return this.http.get(`${this.apiCountProductByMonthYear}/countCompletedJobsForRequestProductByMonthAndYear`, { params });
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
