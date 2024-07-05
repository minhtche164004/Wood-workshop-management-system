import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class SubMaterialService {
  private apiDowloadExcel = `${environment.apiUrl}api/auth/submaterial/download-form-submaterial-data-excel`;
  constructor(private http: HttpClient) { }

  downloadExcel(): Observable<any> {
    return this.http.get(this.apiDowloadExcel);
  }
}
