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
  cancelReason: string = '';
  private apiUrl_ViewProfile = `${environment.apiUrl}api/auth/user/ViewProfile`;
  private apiUrl_UpdateProfile = `${environment.apiUrl}api/auth/user/UpdateProfile`;
  private apiUrl_ChangePass = `${environment.apiUrl}api/auth/user/ChangePass`;
  private apiUrl_GetById = `${environment.apiUrl}api/auth/admin/GetById`;
  private apiUrl_EditUser = `${environment.apiUrl}api/auth/admin/EditUser`;
  private apiUrl_GetByIdWishList = `${environment.apiUrl}api/auth/order/GetWhiteListByUser`;
  private apiUrl_GetHistoryOrderCustomer = `${environment.apiUrl}api/auth/order/historyOrder`;

  private apiUrl_GetListProductCustomer = `${environment.apiUrl}api/auth/order/GetAllRequestByUserId`;

  private apiUrl_DeleteWhiteList = `${environment.apiUrl}api/auth/order/DeleteWhiteList`;
  private apiUrl_GetOrderDeTailById = `${environment.apiUrl}api/auth/order/GetOrderById`;
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
  private apiUrl_getfindAllJobForDoneByEmployeeID = `${environment.apiUrl}api/auth/job/findAllJobForDoneByEmployeeID`;
  private api_getListJobWasDoneAdmin = `${environment.apiUrl}api/auth/job/getListJobWasDone`;
  private api_getAllRequest = `${environment.apiUrl}api/auth/order/GetAllRequest`;
  private api_getAllStatusRequest = `${environment.apiUrl}api/auth/admin/GetAllStatusRequest`;
  private apiUrl_getRequestById = `${environment.apiUrl}api/auth/order/GetRequestById`;
  private apiUrl_EditRequest = `${environment.apiUrl}api/auth/order/ManagerEditRequest`;
  private apiUrl_getAllStatusOrder = `${environment.apiUrl}api/auth/order/getStatusOrder`;
  private apiUrl_chanegStatusOrder = `${environment.apiUrl}api/auth/order/ChangeStatusOrder`;
  private apiUrl_getFilterStatus = `${environment.apiUrl}api/auth/order/MultiFilterOrder`;
  private apiUrl_getSalaryByEmployeeID = `${environment.apiUrl}api/auth/salary/getSalaryByEmployeeID`;
  private apiUrl_changeStatusOrderRequest = `${environment.apiUrl}api/auth/order/Cancel_Order`;
  private apiUrl_getAllStatusOrderRequest = `${environment.apiUrl}api/auth/admin/GetAllStatusRequest`;
  private apiUrl_cancelOrder = `${environment.apiUrl}api/auth/order/Cancel_Order`;
  private apiUrl_getFilterRole = `${environment.apiUrl}api/auth/admin/FilterByPosition`;


  private api_getAllInputSubMaterial = `${environment.apiUrl}api/auth/submaterial/getAllInputSubMaterial`;
  private apiUrl_getMultiFillterRHistoryImpact = `${environment.apiUrl}api/auth/submaterial/MultiFilterInputSubMaterial`;

  private api_getAllOrderDetailById = `${environment.apiUrl}api/auth/order/getAllOrderDetailByOrderId`;
  private apiUrl_Paymentmoney = `${environment.apiUrl}api/auth/order/ConfirmPayment`;
  private apiUrl_SendMail = `${environment.apiUrl}api/auth/order/SendMailToNotifyCationAboutOrder`;
  private apiUrl_getFilterUser = `${environment.apiUrl}api/auth/admin/getMultiFillterUser`;
  
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
  getOrderDetailByOrderId(orderId: string): Observable<any> {
    const url = `${this.api_getAllOrderDetailById}?orderId=${orderId}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  getRequestById(id: string): Observable<any> {
    const url = `${this.apiUrl_getRequestById}?id=${id}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  cancelOrder(orderId: number, specialOrderId: boolean, cancelReason: string): Observable<string> {
    const url = `${this.apiUrl_cancelOrder}?order_id=${orderId}&special_order_id=${specialOrderId}`;
    const headers = new HttpHeaders({ 'Content-Type': 'text/plain' });

    return this.http.post<string>(url, cancelReason, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  Paymentmoney(orderId: number): Observable<string> {
    const url = `${this.apiUrl_Paymentmoney}?order_id=${orderId}`;
    const headers = new HttpHeaders({ 'Content-Type': 'text/plain' });

    return this.http.post<string>(url, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  SendMail(orderId: number): Observable<string> {
    const url = `${this.apiUrl_SendMail}?order_id=${orderId}`;
    const headers = new HttpHeaders({ 'Content-Type': 'text/plain' });

    return this.http.post<string>(url, { headers }).pipe(
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
  getfindAllJobForDoneByEmployeeID(): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.get<any>(this.apiUrl_getfindAllJobForDoneByEmployeeID, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  getSalaryByEmployeeID(): Observable<any> {
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    console.log("Authorization header:", headers.get('Authorization'));

    return this.http.get<any>(this.apiUrl_getSalaryByEmployeeID, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getListJobWasDoneAdmin(): Observable<any> {


    return this.http.get<any>(this.api_getListJobWasDoneAdmin).pipe(
      catchError(this.handleError)
    );
  }
  getAllRequest(): Observable<any> {

    return this.http.get<any>(this.api_getAllRequest).pipe(
      catchError(this.handleError)
    );
  }
  getAllInputSubMaterial(): Observable<any> {
    return this.http.get<any>(this.api_getAllInputSubMaterial).pipe(
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

  getAllMaterialName(): Observable<any> {
    return this.http.get<any>(this.api_getAllMaterialName).pipe(
      catchError(this.handleError)
    );
  }
  getAllSubMaterialName(): Observable<any> {
    return this.http.get<any>(this.api_getAllSubMaterialName).pipe(
      catchError(this.handleError)
    );
  }

  getAllStatusRequest(): Observable<any> {
    return this.http.get<any>(this.api_getAllStatusRequest).pipe(
      catchError(this.handleError)
    );
  }
  EditRequest(request_id: string, RequestData: any): Observable<any> {
    const url = `${this.apiUrl_EditRequest}?request_id=${request_id}`;
    return this.http.put<any>(url, RequestData).pipe(
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
  getAllStatusOrder(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_getAllStatusOrder);
  }
  getAllStatusOrderRequest(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_getAllStatusOrderRequest);
  }
  changeStatusOrder(orderId: string, status_id: string): Observable<any> {
    const token = localStorage.getItem('loginToken');
    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const url = `${this.apiUrl_chanegStatusOrder}?orderId=${orderId}&status_id=${status_id}`;
    return this.http.put<any>(url, null, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  changeStatusOrderRequest(orderId: string, status_id: string): Observable<any> {
    const token = localStorage.getItem('loginToken');
    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    const url = `${this.apiUrl_changeStatusOrderRequest}?order_id=${orderId}&special_order_id=${status_id}`;
    return this.http.put<any>(url, null, { headers }).pipe(
      catchError(this.handleError)
    );
  }
  getFilterStatus(search: string, statusId: string, specialOrder: number, startDate: string, endDate: string): Observable<any> {

    const params = {
      search: search,
      statusId: statusId,
      specialOrder: specialOrder,
      // startDate: startDate,
      // endDate: endDate
    };
    const body = {

      startDate: startDate,
      endDate: endDate
    };
    const queryString = Object.entries(params)
      .filter(([key, value]) => {
        if (key === 'search' && value === '') return false;
        if (key === 'statusId' && value === "0") return false;
        if (key === 'specialOrder' && value === -1) return false;

        return value != null && value !== '';
      })
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');

    const url = `${this.apiUrl_getFilterStatus}?${queryString}`;
    console.log(url);
    
    return this.http.post<any>(url, body).pipe(
      catchError(this.handleError)
    );
  }
  getFilterUser(search: string, roleId: string, position_id: string): Observable<any> {

    const params = {
      search: search,
      roleId: roleId,
      position_id: position_id,
      // startDate: startDate,
      // endDate: endDate
    };
  
    const queryString = Object.entries(params)
      .filter(([key, value]) => {
        if (key === 'search' && value === '') return false;
        if (key === 'roleId' && value === "0") return false;
        if (key === 'position_id' && value === "0") return false;
   

        return value != null && value !== '';
      })
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');

    const url = `${this.apiUrl_getFilterUser}?${queryString}`;
    console.log(url);
    
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  getFilterRole(query: number): Observable<any> {
    const params = {

      query: query,

    };

    const queryString = Object.entries(params)
      .filter(([key, value]) => {

        if (key === 'query' && value === 0) return false;
        return value != null;
      })
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');

    const url = `${this.apiUrl_getFilterRole}?${queryString}`;
    console.log(url);
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  getMultiFillterRHistoryImpact(search: string, sortDirection: string, startDate: string, endDate: string): Observable<any> {
    const params = {
      search: search,
      sortDirection: sortDirection,
    };
    const body = {

      startDate: startDate,
      endDate: endDate
    };
    const queryString = Object.entries(params)
      .filter(([key, value]) => {
        if (key === 'search' && value === '') return false;


        return value != null;
      })
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');

    const url = `${this.apiUrl_getMultiFillterRHistoryImpact}?${queryString}`;
    console.log(url);
    return this.http.post<any>(url, body).pipe(
      catchError(this.handleError)
    );
  }

}
