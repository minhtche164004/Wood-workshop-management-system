import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class SalaryService {

  private apiGetSalary = `${environment.apiUrl}api/auth/job/getListJobWasDone`;
  constructor(private http: HttpClient) { }

  getSalary(): Observable<any> {
    return this.http.get<any>(this.apiGetSalary);
  }
}
