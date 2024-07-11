import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class CreateOrderService {
    private apiGetListPhoneNumber = `${environment.apiUrl}api/auth/order/getListPhoneNumber`;
    private apiGetUserInfoByPhone = `${environment.apiUrl}api/auth/order/getInfoUserByPhoneNumber`;
    private apiGetAllProudctForCustumer = `${environment.apiUrl}api/auth/product/getAllProductForCustomer`;
    private apiGetProductByID = `${environment.apiUrl}api/auth/product/ViewDetailProductById`;
    private apiAddOrder = `${environment.apiUrl}api/auth/order/AddOrder`;
    private apiGetAllRequestByUserId = `${environment.apiUrl}api/auth/order/GetAllRequestByAccountId`;
    private apiGetProductRequestByRequestId = `${environment.apiUrl}api/auth/product/getRequestProductByRequestId`;
    private apiSubmitOrder = `${environment.apiUrl}api/auth/submitOrder`;

    constructor(private http: HttpClient) { }
    // private handleError(error: any) {
    //     let errorMessage = '';
    //     if (error.error instanceof ErrorEvent) {
    //         errorMessage = `An error occurred: ${error.error.message}`;
    //     } else {
    //         errorMessage = `Backend returned code ${error.status}: ${error.error}`;
    //     }
    //     console.error(errorMessage);
    //     return throwError(errorMessage);
    // }

    getGetListPhoneNumber(): Observable<any> {
        const url = `${this.apiGetListPhoneNumber}`;
        return this.http.get<any>(url);
    }
    getUserInfoByPhone(phoneNumber: string): Observable<any> {
        const url = `${this.apiGetUserInfoByPhone}?phoneNumber=${phoneNumber}`;
        return this.http.get<any>(url).pipe(
            catchError(this.handleError)
        );
    }
    getAllProductCustomer(): Observable<any> {
        const url = `${this.apiGetAllProudctForCustumer}`;
        return this.http.get<any>(url).pipe(
            catchError(this.handleError)
        );
    }
    getProductById(productId: number): Observable<any> {
        const url = `${this.apiGetProductByID}?id=${productId}`;
        return this.http.get<any>(url);
    }

    addNewOrder(orderData: any): Observable<any> {
        return this.http.post(this.apiAddOrder, orderData).pipe(
            catchError(this.handleError)
        );
    }

    GetAllRequestByUserId(userId: number): Observable<any> {
        const url = `${this.apiGetAllRequestByUserId}?acc_id=${userId}`;
        return this.http.get<any>(url);
    }

    GetAllProductRequestByRequestId(requestId: number): Observable<any> {
        const url = `${this.apiGetProductRequestByRequestId}?id=${requestId}`;
        return this.http.get<any>(url);
    }

    submitOrder(amount: number, orderInfo: string): Observable<any> {
        const url = `${this.apiSubmitOrder}?amount=${amount}&orderInfo=${orderInfo}`;
        return this.http.post<any>(url, {});
    }

    handleError(error: HttpErrorResponse) {
        return throwError(error);
    }
}
