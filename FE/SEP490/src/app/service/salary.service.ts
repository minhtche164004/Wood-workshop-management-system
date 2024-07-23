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

  private apiQR = `${environment.apiUrl}api/auth/getQRBankingForEmployee`

  private acceptPaymentStatus = `${environment.apiUrl}api/auth/salary/updatebanking`;
  constructor(private http: HttpClient) { }

  getSalary(): Observable<any> {
    return this.http.get<any>(this.apiGetSalary);
  }
  updateBanking(id: number, status: any): Observable<any> {
    const url = `${this.acceptPaymentStatus}?id=${id}&is_advance_success=${status}`;
    return this.http.put<any>(url, {});
  }
  getBanks(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }
  getQRBanking(amount: number, accountId: number, username: string, bin_bank: string, orderInfo: string): Observable<string> {
    const url = `${this.apiQR}?amount=${amount}&accountNo=${accountId}&username=${username}&bin_bank=${bin_bank}&orderInfo=${orderInfo}`;
    
    // Yêu cầu dữ liệu trả về là dạng text (base64 của hình ảnh)
    return this.http.post<string>(url, {}, { responseType: 'text' as 'json' });
  }
  // multSearchSalary(username: string, fromDate: string, toDate: string, sortDirection: string, position_id: string, isAdvanceSuccess: string): Observable<any> {
  //   const params = {
  //     username: username,

  //     position_id: position_id,
  //     sortDirection: sortDirection,
  //     isAdvanceSuccess: isAdvanceSuccess
  //   };
  //   const body = {

  //     fromDate: fromDate,
  //     toDate: toDate
  //   };
  //   const queryString = Object.entries(params)
  //     .filter(([key, value]) => value != null && value !== '') // Loại bỏ các tham số có giá trị null, undefined hoặc rỗng
  //     .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
  //     .join('&');
  
  //   const url = `${this.apiMultiSearchSalary}?${queryString}`;
  //   console.log("total url search: ",url);
  //   return this.http.post<any>(url,body).pipe(
  //     catchError(this.handleError)
  //   );
  // }

  multSearchSalary(username: string, startDate: string, endDate: string, sortDirection: string, position_id: string, isAdvanceSuccess: string): Observable<any> {
    const params = {
      username: username,
      position_id: position_id,
      isAdvanceSuccess: isAdvanceSuccess,
      sortDirection:sortDirection,
      // startDate: startDate,
      // endDate: endDate
    };
    const body = {

      startDate: startDate,
      endDate: endDate
    };
    const queryString = Object.entries(params)
      .filter(([key, value]) => {
        if (key === 'username' && value === '') return false;
        if (key === 'statusId' && value === "0") return false;
        if (key === 'isAdvanceSuccess' && value === "0") return false;
        return value != null && value !== '';
      })
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');

    const url = `${this.apiMultiSearchSalary}?${queryString}`;
    console.log(url);
    return this.http.post<any>(url,body).pipe(
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
