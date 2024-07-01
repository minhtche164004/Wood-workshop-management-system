import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';
@Injectable({
  providedIn: 'root'
})
export class MaterialService {

  private api_getAllMaterial = `${environment.apiUrl}api/auth/getAll`;
  private api_getAllSubMtr = `${environment.apiUrl}api/auth/submaterial/getall`
  private api_addsubMtr = `${environment.apiUrl}api/auth/submaterial/AddNewSubMaterial`
  private api_editMaterial = `${environment.apiUrl}api/auth/EditMaterial`;

  constructor(private http: HttpClient) { }
  getAllMaterial(): Observable<any>{
    return this.http.get<any>(this.api_getAllMaterial).pipe(
      catchError(this.handleError) 
    );
  }
  updateMaterial(id: number, material: { materialName: string, type: string }): Observable<any> {
    const url = `${this.api_editMaterial}?id=${id}`;
    return this.http.put(url, material);
  }
  private handleError(error: HttpErrorResponse) {
    let errorMessage = '';
    if (error.error instanceof ErrorEvent) {
      
      errorMessage = `An error occurred: ${error.error.message}`;
    } else {
       errorMessage = `Backend returned code ${error.status}: ${error.error}`;
    }
    console.error(errorMessage); // Ghi log lỗi để debug
    return throwError(errorMessage); // Ném lỗi lại như một observable
  }


  getAllSubMaterials(): Observable<any>{
    return this.http.get<any>(this.api_getAllSubMtr)
  }
  

  addNewSubMaterial(subMaterialData: any): Observable<any> {
    
    return this.http.post<any>(this.api_addsubMtr, subMaterialData).pipe(
      catchError(this.handleError)
    );
  }
  private api_searchSubMaterial = `http://localhost:8080/api/auth/submaterial/search`;
  searchSubMaterial(searchKey: string): Observable<any> {
    return this.http.get<any>(`${this.api_searchSubMaterial}?q=${searchKey}`).pipe(
      catchError(this.handleError)
    );
  }
}
