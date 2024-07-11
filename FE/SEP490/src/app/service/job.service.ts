import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class JobService {
  private apiGetListProductRQ=`${environment.apiUrl}api/auth/job/getListProductRequestForJob`
  private apiGetListProduct=`${environment.apiUrl}api/auth/job/getListProductForJob`
  private apiSearchProductJob = `${environment.apiUrl}api/auth/product/findProductByNameorCode`
  private apiAddJob = `${environment.apiUrl}api/auth/job`
  private apiGetPosition3 = `${environment.apiUrl}api/auth/job/findUsersWithPositionAndLessThan3Jobs`
  private apiAddProductForJob = `${environment.apiUrl}api/auth/job`
  private apiGetStatusJob =`${environment.apiUrl}api/auth/job/getAllStatusJob`
  private apiGetStatusJobByType = `${environment.apiUrl}api/auth/job/getAllStatusType`
  private apiCreateProductForjob = `${environment.apiUrl}api/auth/job/CreateJobs`

  private apiSearchProductByNameOrCode = `${environment.apiUrl}api/auth/job/getListProductJobByNameOrCodeProduct`  //tim viec lam theo san pham co san theo ten

  private apiEditJob = `${environment.apiUrl}api/auth/job/EditJob`
  private apiAcceptJob = `${environment.apiUrl}api/auth/job/acceptJob`
  private apiGetProductSubMaterial = `${environment.apiUrl}api/auth/submaterial/getProductSubMaterialByProductId`;
  private apiCreateExportProd = `${environment.apiUrl}api/auth/submaterial/createExportMaterialProductTotalJob`
    // user_id={{$random.integer(100)}}&
    // p_id={{$random.integer(100)}}&                                  ?job_id=152&status_id=9
    // status_id={{$random.integer(100)}}`

  constructor(private http: HttpClient) { }
  getSubMTRProduct(productId: number, mateId: number): Observable<any> {
    const url = `${this.apiGetProductSubMaterial}?id=${productId}&mate_id=${mateId}`;
    return this.http.get<any>(url);
  }

  editJob(jobId: number, data: any): Observable<any> {
    return this.http.put(`${this.apiEditJob}?job_id=${jobId}`, data);
  }

  getProductJobByNameOrCode(key: string): Observable<any> {
    return this.http.get<any>(`${this.apiSearchProductByNameOrCode}?key=${key}`);
  }
  createExportMaterialProductTotalJob(id:number, mate_id: number, emp_id: number, productForm: any): Observable<any> {
    const url = `${this.apiCreateExportProd}?id=${id}&mate_id=${mate_id}&emp_id=${emp_id}`;
    console.log('Total product for job URL:', url);
    return this.http.post(url, productForm);
  }
  acceptJob(jobId: number, statusId: any): Observable<any> {
    const url = `${this.apiAcceptJob}?job_id=${jobId}&status_id=${statusId}`;
    console.log('Accept job URL:', url);
    const payload = {}; // Bạn có thể thêm payload nếu cần thiết
  
    return this.http.put<any>(url, payload);
  }
  
  addJob(user_id: number, p_id: number, status_id: number, job_id: number, type_id:number, jobData: any): Observable<any> {
    const url = `${this.apiCreateProductForjob}?user_id=${user_id}&p_id=${p_id}&status_id=${status_id}&&type_job=${type_id}&job_id=${job_id}`;
    // console.log('Add job URL:', url);
    return this.http.post<any>(url, jobData);
  }
  addProductForJob(p_id: number, quantity: number) {
    const url = `${this.apiAddProductForJob}/CreateProductForJobs?p_id=${p_id}&quantity=${quantity}`;
    return this.http.post<any>(url, {});
  }
  createJob(user_id: number, p_id: number, status_id: number, job_id: number, type_id: number, jobData: any): Observable<any> {
    const url = `${this.apiAddJob}/CreateJobs`; // Endpoint for creating jobs
   
    const params = { user_id, p_id, status_id, job_id, type_id };
    console.log('Constructed URL:', url);
    console.log('Constructed params:', params);
    return this.http.post(url, jobData, { params });
  }
  getStatusJobByType(type: number): Observable<any> {
    console.log(this.apiGetStatusJobByType)
    return this.http.get<any>(`${this.apiGetStatusJobByType}?type=${type}`).pipe(
      catchError(this.handleError)
    );
  }
  getStatusByType(): Observable<any> {
    console.log(this.apiGetStatusJob)
    return this.http.get<any>(this.apiGetStatusJob).pipe(
      catchError(this.handleError)
    );
  }

  GetPosition3(type: any): Observable<any> {
    console.log(this.apiGetPosition3)
    return this.http.get<any>(`${this.apiGetPosition3}?type=${type}`).pipe(
      catchError(this.handleError)
    );
  }
  getListProductRQ(): Observable<any> {
    console.log(this.apiGetListProductRQ)
    return this.http.get<any>(this.apiGetListProductRQ).pipe(
      catchError(this.handleError)
    );
  }
  getListProduct(): Observable<any> {
    console.log(this.apiGetListProduct)
    return this.http.get<any>(this.apiGetListProduct).pipe(
      catchError(this.handleError)
    );
  }
  searchProduct(key: string): Observable<any> {
    console.log(this.apiGetListProduct)
    return this.http.get<any>(`${this.apiSearchProductJob}?key=${key}`);
  
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
