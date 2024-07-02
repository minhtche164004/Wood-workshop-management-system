import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';
interface Material {
  materialId: number;
  materialName: string;
  type: string;
}

interface UpdateMaterialResponse {
  code: number;
  result: Material;
}
@Injectable({
  providedIn: 'root'
})
export class TypeMaterialService {
   private api_getAllTypeMaterial = `${environment.apiUrl}api/auth/getAll`;
   private api_AddTypeMaterial = `${environment.apiUrl}api/auth/addNewMaterial`;
   private api_editMaterial = `${environment.apiUrl}api/auth/EditMaterial`;
  constructor(private http: HttpClient) { }

  getAllTypeMateriall(): Observable<any>{
    return this.http.get<any>(this.api_getAllTypeMaterial).pipe(
      catchError(this.handleError) 
    );
  }
  addTypeMaterial(typeMaterialData: any): Observable<any> {
    return this.http.post<any>(this.api_AddTypeMaterial, typeMaterialData).pipe(
      catchError(this.handleError)
    );
  }
  updateMaterial(id: number, material: { materialName: string, type: string }): Observable<UpdateMaterialResponse> {
    const url = `${this.api_editMaterial}?id=${id}`;
    return this.http.put<UpdateMaterialResponse>(url, material);
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

}
