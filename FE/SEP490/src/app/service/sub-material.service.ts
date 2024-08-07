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

  private api_FilterByMaterial = `${environment.apiUrl}api/auth/submaterial/FilterByMaterial`;

  private api_getSubMtrById =`${environment.apiUrl}api/auth/submaterial/getSubmaterialById`;
  private apiNhapExcel = `${environment.apiUrl}api/auth/submaterial/upload-submaterial-data`
  private api_editSubMaterial = `${environment.apiUrl}api/auth/submaterial/editSubMaterial`;
  private apiUrl_multiFilterSubmaterial = `${environment.apiUrl}api/auth/submaterial/MultiFilterSubmaterial`;

   constructor(private http: HttpClient) { }
   multiFilterSubmaterial(search: string, materialId: number): Observable<any> {
    
    const url = `${this.apiUrl_multiFilterSubmaterial}?search=${encodeURIComponent(search)}&materialId=${materialId}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
   uploadExcel(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post(this.apiNhapExcel, formData, {
      headers: new HttpHeaders({
        // Nếu bạn cần thêm bất kỳ headers nào khác
      }),
      reportProgress: true,
      observe: 'events'
    });
  }
  downloadExcel(): Observable<any> {
    return this.http.get(this.apiDowloadExcel, { responseType: 'blob' });
  }
  getSubMaterialById(id: number): Observable<any> {
    const url = `${this.api_getSubMtrById}?id=${id}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  editSubMaterial(subMaterialId: number, data: any): Observable<any> {
    return this.http.put(`${this.api_editSubMaterial}?id=${subMaterialId}`, data);
  }
  filterByMaterial(materialId: number): Observable<any> {
    const url = `${this.api_FilterByMaterial}?id=${materialId}`;
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
