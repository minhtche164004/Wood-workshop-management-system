import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private apiGetAllEmployee = `${environment.apiUrl}api/auth/submaterial/getAllEmpMate`;

  private apiGetAllPosition = `${environment.apiUrl}api/auth/admin/GetAllPosition`;

  private apiCountJobById = `${environment.apiUrl}api/auth/job/countJobsByUserId`;

  private apiGetAllSubMaterialForEmp = `${environment.apiUrl}api/auth/submaterial/getAllEmpMate`;

  private apiSearchMTREmployee = `${environment.apiUrl}api/auth`;
  constructor(private http: HttpClient) { }

  getAllEmployee(): Observable<any> {
    return this.http.get<any>(this.apiGetAllEmployee).pipe(
      catchError(this.handleError)
   
   );
  }
  searchEmployeeByName(name: string): Observable<any> {
    const params = new HttpParams().set('key', name);
    const url = `${this.apiSearchMTREmployee}/submaterial/findEmployeematerialsByNameEmployee`;
    const fullUrl = `${url}?${params.toString()}`;
  
    console.log(fullUrl); // In ra URL đầy đủ
  
    return this.http.get<any>(url, { params });
  }
  
  getSubMaterialForEmp(): Observable<any> {
    return this.http.get<any>(this.apiGetAllSubMaterialForEmp).pipe(
      catchError(this.handleError)
    );
  }
  getAllPostionEmp(): Observable<any>{
    return this.http.get<any>(this.apiGetAllPosition).pipe(
      catchError(this.handleError)
    );
  }
  private handleError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      // Xảy ra lỗi ở phía client hoặc mạng. Xử lý tương ứng.
      errorMessage = `An error occurred: ${error.error.message}`;
    } else {
      // Backend trả về mã lỗi không thành công.
      // Response body có thể cung cấp dấu hiệu về vấn đề gặp phải.
      errorMessage = `Backend returned code ${error.status}: ${error.error}`;
    }
    console.error(errorMessage); // Ghi log lỗi để debug
    return throwError(errorMessage); // Ném lỗi lại như một observable
  }
  countJobByUserId(id: number): Observable<any> {
    const url = `${this.apiCountJobById}?user_id=${id}`;
    console.log('Count job by user ID:', url);
    return this.http.get<any>(`${this.apiCountJobById}?user_id=${id}`).pipe(
      catchError(this.handleError)
    );
  }
}
