import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment'; // Đường dẫn đúng tới file môi trường
import { AddNewAccount } from '../Admin/user-management/user-management.component';
@Injectable({
  providedIn: 'root'
})
export class AuthenListService {
  private apiUrl_ViewProfile = `${environment.apiUrl}api/auth/user/ViewProfile`;
  private apiUrl_UpdateProfile = `${environment.apiUrl}api/auth/user/UpdateProfile`;
  private apiUrl_ChangePass = `${environment.apiUrl}api/auth/user/ChangePass`;
  private apiUrl_GetById = `${environment.apiUrl}api/auth/admin/GetById`;
  private apiUrl_EditUser = `${environment.apiUrl}api/auth/admin/EditUser`;
  private apiUrl_GetByIdWishList = `${environment.apiUrl}api/auth/order/GetWhiteListByUser`;
  private apiUrl_GetHistoryOrderCustomer = `${environment.apiUrl}api/auth/order/historyOrder`;

  private apiUrl_GetListProductCustomer = `${environment.apiUrl}api/auth/order/GetAllRequestByUserId`;

  private apiUrl_DeleteWhiteList = `${environment.apiUrl}api/auth/order/DeleteWhiteList`;
  private apiUrl_GetOrderDeTailById = `${environment.apiUrl}api/auth/order/getOrderDetailById`;
  private apiUrl_AddNewAccount = `${environment.apiUrl}api/auth/admin/AddNewAccount`;
  private apiUrl_SearchUserByNameorAddress = `${environment.apiUrl}api/auth/admin/SearchUserByNameorAddress`;

  private apiUrl_deleteRequest = `${environment.apiUrl}api/auth/order/deleteRequest`;
  private apiUrl_GetRequestByIdCustomer = `${environment.apiUrl}api/auth/order/GetRequestById`;
  private apiUrl_EditRequestProduct = `${environment.apiUrl}api/auth/order/CustomerEditRequest`;
  private apiUrl_getSupplierById = `${environment.apiUrl}api/auth/supplier/GetSupplierById`;
  private apiUrl_GetMaterialById = `${environment.apiUrl}api/auth/GetMaterialById`;
  private apiUrl_GetAddMaterial = `${environment.apiUrl}api/auth/addNewMaterial`;
  private apiUrl_getMaterialyId = `${environment.apiUrl}api/auth/GetMaterialById`;
  private apiUrl_EditMaterial = `${environment.apiUrl}api/auth/EditMaterial`;
  private api_getAllMaterialName = `${environment.apiUrl}api/auth/getAllName`;
  private api_getAllSubMaterialName = `${environment.apiUrl}api/auth/submaterial/getall`;
  private apiUrl_EditSupplier = `${environment.apiUrl}api/auth/supplier/EditSupplier`;
  private apiUrl_AddProductRequired = `${environment.apiUrl}api/auth/order/AddNewRequest`;

  private apiUrl_NameATM = 'https://api.vietqr.io/v2/banks';
  constructor(private http: HttpClient) { }
  isLoggedIn(): boolean {
    const token = localStorage.getItem('loginToken');
    return token !== null; // Trả về true nếu tồn tại token trong localStorage, ngược lại false
  }
  findSearchUserByNameorAddress(query: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl_SearchUserByNameorAddress}?query=${query}`);
  }
  AddNewAccountForAdmin(addNewAccountRequest: AddNewAccount): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log('Authorization header:', headers.get('Authorization'));

    return this.http.post<any>(this.apiUrl_AddNewAccount, addNewAccountRequest, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  getNameATM(): Observable<any> {
    const url = `${this.apiUrl_NameATM}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  getUserById(user_id: string): Observable<any> {
    const url = `${this.apiUrl_GetById}?user_id=${user_id}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  getSupplierById(id: string): Observable<any> {
    const url = `${this.apiUrl_getSupplierById}?id=${id}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  getSubMaterialById(id: string): Observable<any> {
    const url = `${this.apiUrl_GetMaterialById}?id=${id}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  getOrderDetailById(order_detail_id: string): Observable<any> {
    const url = `${this.apiUrl_GetOrderDeTailById}?id=${order_detail_id}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  editRequestProductForCustomer(requestData: any, images: File[] | null, request_id: number): Observable<any> {
    const formData = new FormData();
    formData.append('requestEditCusDTO', new Blob([JSON.stringify(requestData)], { type: 'application/json' }));
    // cho phep null anh neu khong update anh
    if (images !== null && images.length > 0) {
      images.forEach(image => {
        formData.append('files', image, image.name);
      });
    } else {
      formData.append('files', new Blob(), '');
    }
    const url = `${this.apiUrl_EditRequestProduct}?request_id=${request_id}`;
    return this.http.put(url, formData, {
      headers: {
        'Accept': 'application/json'
      }
    });
  }



  getHistoryOrderCustomer(): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.get<any>(this.apiUrl_GetHistoryOrderCustomer, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  getListRequestProductCusomer(): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.get<any>(this.apiUrl_GetListProductCustomer, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getRequestByIdCustomer(id: number): Observable<any> {
    const url = `${this.apiUrl_GetRequestByIdCustomer}?id=${id}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  GetByIdWishList(): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.get<any>(this.apiUrl_GetByIdWishList, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  deleteWishList(wishlist_id: number): Observable<any> {
    const url = `${this.apiUrl_DeleteWhiteList}?wishlist_id=${wishlist_id}`;
    return this.http.delete<any>(url).pipe(
      catchError(this.handleError)
    );

  }
  deleteRequestProductCustomer(requestId: number): Observable<any> {
    const url = `${this.apiUrl_deleteRequest}?requestId=${requestId}`;
    return this.http.delete<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  editUserById(user_id: string, userData: any): Observable<any> {
    const token = localStorage.getItem('loginToken');
    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const url = `${this.apiUrl_EditUser}?user_id=${user_id}`;
    return this.http.put<any>(url, userData, { headers }).pipe(
      catchError(this.handleError)
    );
  }



  getUserProfile(): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.get<any>(this.apiUrl_ViewProfile, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  updateUserProfile(userProfile: any): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    return this.http.put<any>(this.apiUrl_UpdateProfile, userProfile, { headers }).pipe(
      catchError(this.handleError)
    );
  }


  getChangePass(oldPassword: string, newPassword: string): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const body = {
      old_pass: oldPassword,
      new_pass: newPassword,
      check_pass: newPassword // Assuming check_pass is for password confirmation
    };

    return this.http.put<any>(this.apiUrl_ChangePass, body, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error.message);
    return throwError(error);
  }


  addNewMaterial(MaterialData: any): Observable<any> {
    
    return this.http.post<any>(this.apiUrl_GetAddMaterial, MaterialData).pipe(
      catchError(this.handleError)
    );
  }
  getMaterialById(id: string): Observable<any> {
    const url = `${this.apiUrl_getMaterialyId}?id=${id}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  editMaterial(id: string, MaterialData: any): Observable<any> {
    const url = `${this.apiUrl_EditMaterial}?id=${id}`;
    return this.http.put<any>(url, MaterialData).pipe(
      catchError(this.handleError)
    );
  }

  getAllMaterialName(): Observable<any>{
    return this.http.get<any>(this.api_getAllMaterialName).pipe(
      catchError(this.handleError) 
    );
  }
  getAllSubMaterialName(): Observable<any>{
    return this.http.get<any>(this.api_getAllSubMaterialName).pipe(
      catchError(this.handleError) 
    );
  }
  
  EditSupplier(id: string, suplierData: any): Observable<any> {
    const token = localStorage.getItem('loginToken');
    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const url = `${this.apiUrl_EditSupplier}?id=${id}`;
    return this.http.put<any>(url, suplierData, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  
  uploadProductRequired(productRequiredData: any, images: File[]): Observable<any> {
    const formData = new FormData();

    formData.append('requestDTO', new Blob([JSON.stringify(productRequiredData)], { type: 'application/json' }));
    images.forEach(image => {
      formData.append('files', image, image.name);
    });
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Accept': 'application/json'
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.post(this.apiUrl_AddProductRequired, formData, { headers }).pipe(
      catchError(this.handleError)
    );
  }

}
