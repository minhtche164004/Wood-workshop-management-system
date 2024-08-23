import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
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
  private apicreateExportMaterialRequestTotalJob = `${environment.apiUrl}api/auth/submaterial/createExportMaterialRequestTotalJob`  //Dang doi api detJobByJobId

  //Dang doi api detJobByJobId  
  private apiGetJobById = `${environment.apiUrl}api/auth/job/getJobById`  //tim viec lam theo san pham co san theo ten

  private apiSearchRequestByName = `${environment.apiUrl}api/auth/job/getRequestProductInOrderDetailByCode`  //tim viec lam theo san pham co san theo ten
  private apiEditJob = `${environment.apiUrl}api/auth/job/EditJob`
  private apiAcceptJob = `${environment.apiUrl}api/auth/job/acceptJob`
  private apiGetProductSubMaterial = `${environment.apiUrl}api/auth/submaterial/getProductSubMaterialByProductId`;
  private apiCreateExportProd = `${environment.apiUrl}api/auth/submaterial/createExportMaterialProductTotalJob`
  private apiGetSubMaterialOfProductRQ = `${environment.apiUrl}api/auth/submaterial/getRequestProductSubMaterialByRequestProductId`
  private apicheckErrorOfJobHaveFixDoneOrNot = `${environment.apiUrl}api/auth/job/checkErrorOfJobHaveFixDoneOrNot`
  private apiMultiSearchJob = `${environment.apiUrl}api/auth/job/MultiFilterListProductJob`

  private apiMultiSeachJobRequest = `${environment.apiUrl}api/auth/job/MultiFilterRequestProductInJob`

  private apiEmpAbsent = `${environment.apiUrl}api/auth/job/EmployeeSick`;
  private apigetAllProductErrorByJobId = `${environment.apiUrl}api/auth/job`
    // user_id={{$random.integer(100)}}&
    // p_id={{$random.integer(100)}}&                                  ?job_id=152&status_id=9
    // status_id={{$random.integer(100)}}`
    private apiGetTotalCostOfSubMateInJob = `${environment.apiUrl}api/auth/job/getToTalCostOfSubMateInJob`;

  constructor(private http: HttpClient) { }

  getTotalCostOfSubMateInJob(jobId: number): Observable<any> {
    const url = `${this.apiGetTotalCostOfSubMateInJob}?job_id=${jobId}`;
    console.log('URL:', url);
    return this.http.get<any>(url);
  }
  
  getSubMTRProduct(productId: number, mateId: number): Observable<any> {
    const url = `${this.apiGetProductSubMaterial}?id=${productId}&mate_id=${mateId}`;
    console.log('URL:', url);
    return this.http.get<any>(url);
  }
 
  getEmployeeSick(userId: number, jobId: number, costEmployee: number, quantity: number): Observable<any> {
    const url = `${this.apiEmpAbsent}?user_id=${userId}&job_id=${jobId}&cost_employee=${costEmployee}&quantity_product_done=${quantity}`;

    console.log('URL:', url);
    return this.http.post(url, '');
  }
  //gam get detail by id 
  getJobDetailById(jobId: number): Observable<any> {
    const url = `${this.apiGetJobById}?job_id=${jobId}`;
    return this.http.get<any>(url);
  }
  getAllProductErrorsByJobId(jobId: number): Observable<any> {
    const url = `${this.apigetAllProductErrorByJobId}/getAllProductErrorByJobId`;
    const params = new HttpParams().set('job_id', jobId.toString());
    return this.http.get<any>(url, { params });
  }
  
  checkErrorOfJob(jobId: number): Observable<any> {
    return this.http.get<any>(`${this.apicheckErrorOfJobHaveFixDoneOrNot}?job_id=${jobId}`);
  }
  seachRequestByName(key: string): Observable<any> {
    const url = `${this.apiSearchRequestByName}?key=${key}`;
    console.log('URL:', url);
    return this.http.get<any>(`${this.apiSearchRequestByName}?key=${key}`);
  }
  getSubMTRProductRQ(id: number, mate_id: number): Observable<any> {
    const url = `${this.apiGetSubMaterialOfProductRQ}?id=${id}&mate_id=${mate_id}`;
    return this.http.get<any>(url);
  }
  editJob(jobId: any, data: any): Observable<any> {
    return this.http.put(`${this.apiEditJob}?job_id=${jobId}`, data);
  }

  getProductJobByNameOrCode(key: string): Observable<any> {
    return this.http.get<any>(`${this.apiSearchProductByNameOrCode}?key=${key}`);
  }
  createExportMaterialProductTotalJob(id:number, mate_id: number, emp_id: any, productForm: any): Observable<any> {
    const url = `${this.apiCreateExportProd}?id=${id}&mate_id=${mate_id}&emp_id=${emp_id}`;
   // console.log('Total product for job URL:', url);
    return this.http.post(url, productForm);
  }
  createExportMaterialRequestTotalJob(id:number, mate_id: number, emp_id: any, productForm: any): Observable<any> {
    const url = `${this.apicreateExportMaterialRequestTotalJob}?id=${id}&mate_id=${mate_id}&emp_id=${emp_id}`;
 //   console.log('Total product for job URL:', url);
    return this.http.post(url, productForm);
  }
  acceptJob(jobId: string, statusId: any): Observable<any> {
    const url = `${this.apiAcceptJob}?job_id=${jobId}&status_id=${statusId}`;
    console.log('Accept job URL:', url);
    const payload = {}; // Bạn có thể thêm payload nếu cần thiết
  
    return this.http.put<any>(url, payload);
  }
  
  addJob(user_id: any, p_id: number, status_id: number, job_id: number, type_id:number, jobData: any): Observable<any> {
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

  multiSearchJob(search: string, status_id: string, position_id: string): Observable<any> {
    const params = {
      search: search,
      status_id: status_id,
      position_id: position_id,
    };
    
    const queryString = Object.entries(params)
      .filter(([key, value]) => value != null && value !== '') // Loại bỏ các tham số có giá trị null, undefined hoặc rỗng
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');
  
    const url = `${this.apiMultiSearchJob}?${queryString}`;
    console.log('URL:', url);
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  multiSearchJobRequest(search: string, status_id: string, position_id: string): Observable<any> {
    const params = {
      search: search,
      status_id: status_id,
      position_id: position_id,
    };
    
    const queryString = Object.entries(params)
      .filter(([key, value]) => value != null && value !== '') // Loại bỏ các tham số có giá trị null, undefined hoặc rỗng
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');
  
    const url = `${this.apiMultiSeachJobRequest}?${queryString}`;
    console.log('URL:', url);
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
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
