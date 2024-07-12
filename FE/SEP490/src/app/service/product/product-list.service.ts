import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductListService {

  private apiAddProduct = `${environment.apiUrl}api/auth/product/AddNewProduct`;
  private apiDeleteProduct = `${environment.apiUrl}api/auth/product/deleteProduct`; // Assuming the delete endpoint
  private api_findProductByCategory = `${environment.apiUrl}api/auth/product`;
  private apiUrlGetProduct = `${environment.apiUrl}api/auth/product/getAllProductForCustomer`;
  private apiUrl = `${environment.apiUrl}api/auth/product/getAllProductForAdmin`;
  private apiUrl_Cate = `${environment.apiUrl}api/auth/product/GetAllCategory`;
  private apiUrl_GetAllUser = `${environment.apiUrl}api/auth/admin/GetAllUser`;
  private apiUrl_Position = `${environment.apiUrl}api/auth/admin/GetAllPosition`;

  private apiUrl_findProduct = `${environment.apiUrl}api/auth/product`;
  private apiUrl_getProductByID = `${environment.apiUrl}api/auth/product/ViewDetailProductById`; // update tu api get product by id

  private api_Url_GetProductError = `${environment.apiUrl}api/auth/job/getAllProductError`;

  private apiUrlCreateProductError = `${environment.apiUrl}api/auth/job/CreateProductError`;
  private apiUrl_GetAllOrder = `${environment.apiUrl}api/auth/order/GetAllOrder`; // Assuming the correct endpoint


  private apiUrl_AllRole = `${environment.apiUrl}api/auth/admin/GetAllRole`; // Assuming the correct endpoint


  private apiUrl_GetMultiProductForCustomer = `${environment.apiUrl}api/auth/product/getMultiFillterProductForCustomer`;

  private apiUrl_AddProduct = `${environment.apiUrl}api/auth/product/AddNewProduct`;

  private apiUrl_EditProduct = `${environment.apiUrl}api/auth/product/EditProduct`;

  private apiUrl_getMultiFillterProductForAdmin = `${environment.apiUrl}api/auth/product/getMultiFillterProductForAdmin`;
  private getAllStatusProduct = `${environment.apiUrl}api/auth/product/GetStatusProduct`;  //sau lay api khac thay vao` api nay bi thieu
  private getAllMaterial = `${environment.apiUrl}api/auth/getAll`;  // lay cac vat lieu
  private getSubMaterialByMaterialId = `${environment.apiUrl}api/auth/submaterial/FilterByMaterial`;  // lay cac vat lieu con theo vat lieu cha 
  private apiUrl_GetAllStatus = `${environment.apiUrl}api/auth/admin/GetAllStatusUser`;
  private apiUrlEditSubMaterialProduct = `${environment.apiUrl}api/auth/submaterial/EditSubMaterialProduct`;


  private api_UrlcreateExportMaterialProduct = `${environment.apiUrl}api/auth/submaterial/createExportMaterialProduct`;  // luu 1 san pham can bao nhieu vat lieu

  private api_UrlexportMaterialProductByProductId = `${environment.apiUrl}api/auth/product/getProductSubMaterialAndMaterialByProductId`;  // lay tat ca vat lieu can co de tao 1 san pham

  //api for product request
  private apiUrlGetProductRequest = `${environment.apiUrl}api/auth/order/GetAllProductRequest`;
  private apiUrl_getMultiFillterRequestProductForAdmin = `${environment.apiUrl}api/auth/product/getMultiFillterRequestProductForAdmin`;
  private apiUrlAddNewProductRequest = `${environment.apiUrl}api/auth/order/AddNewRequestProduct`;
  private api_UrlcreateExportMaterialProductRequest = `${environment.apiUrl}api/auth/submaterial/createExportMaterialProductRequest`;  // lay 1 san pham can bao nhieu vat lieu
  private apiUrl_getAllRequest = `${environment.apiUrl}api/auth/order/GetAllRequest`;
  private apiUrlGetProductRequestByProductRequestId = `${environment.apiUrl}api/auth/order/getRequestProductById`;
  private apiUrlEditProductRequest = `${environment.apiUrl}api/auth/product/EditRequestProduct`;
  private apiUrlDeleteProductRequest = `${environment.apiUrl}api/auth/product/deleteRequestProduct`;
  private api_UrlexportMaterialProductByProductRequestId = `${environment.apiUrl}api/auth/product/getRequestProductSubMaterialAndMaterialByRequestProductId`;  // lay tat ca vat lieu can co de tao 1 san pham theo yeu cau 
  private api_UrlEditSubMateialRequestProduct = `${environment.apiUrl}api/auth/submaterial/EditSubMaterialRequestProduct`;
  //

  constructor(private http: HttpClient) { }
  uploadProduct(productData: any, thumbnail: File, images: File[]): Observable<any> {
    const formData = new FormData();
    formData.append('productDTO', new Blob([JSON.stringify(productData)], { type: 'application/json' }));
    formData.append('file_thumbnail', thumbnail, thumbnail.name);
    images.forEach(image => {
      formData.append('files', image, image.name);
    });

    return this.http.post(this.apiUrl_AddProduct, formData, {
      headers: {
        'Accept': 'application/json'
      }
    });
  }
  createProductError(jobId: number, description: string, solution: string, quantity: number): Observable<any> {
    const body = { description, solution, quantity };
    console.log("create error:  ", body);
    return this.http.post(`${this.apiUrlCreateProductError}?job_id=${jobId}`, body);
  }


  getAllProductError(): Observable<any> {
    return this.http.get<any>(this.api_Url_GetProductError).pipe(
      catchError(this.handleError)
    );
  }





  getProducts(): Observable<any> {
    console.log(this.apiUrl)
    return this.http.get<any>(this.apiUrl).pipe(
      catchError(this.handleError)
    );
  }

  getAllOrder(): Observable<any> {
    console.log(this.apiUrl_GetAllOrder)
    return this.http.get<any>(this.apiUrl_GetAllOrder).pipe(
      catchError(this.handleError)
    );
  }
  getAllStatus(): Observable<any> {
    console.log(this.apiUrl_GetAllStatus)
    return this.http.get<any>(this.apiUrl_GetAllStatus).pipe(
      catchError(this.handleError)
    );
  }

  getAllProductCustomer(): Observable<any> {
    console.log(this.apiUrlGetProduct)
    return this.http.get<any>(this.apiUrlGetProduct).pipe(
      catchError(this.handleError)
    );
  }
  // , headers: HttpHeaders //, { headers }
  addNewProduct(requestData: any): Observable<any> {
    return this.http.post<any>(this.apiAddProduct, requestData).pipe(
      catchError(this.handleError)
    );
  }

  // private handleError(error: any) {
  //   let errorMessage = '';
  //   if (error.error instanceof ErrorEvent) {
  //     errorMessage = `An error occurred: ${error.error.message}`;
  //   } else {
  //     errorMessage = `Backend returned code ${error.status}: ${error.error}`;
  //   }
  //   console.error(errorMessage);
  //   return throwError(errorMessage);
  // }

  handleError(error: HttpErrorResponse) {
    return throwError(error);
  }

  getAllUser(): Observable<any> {
    return this.http.get<any>(this.apiUrl_GetAllUser);
  }

  findProductByNameOrCode(key: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl_findProduct}/findProductByNameorCode?key=${key}`);
  }

  findProductByCategory(key: number): Observable<any> {
    return this.http.get<any>(`${this.api_findProductByCategory}/GetProductByCategory?id=${key}`);
  }

  getAllCategories(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_Cate);
  }

  getAllPosition(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_Position);
  }
  getAllRole(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl_AllRole);
  }

  getProductById(productId: number): Observable<any> {
    const url = `${this.apiUrl_getProductByID}?id=${productId}`;
    return this.http.get<any>(url);
  }
  deleteProduct(productId: number): Observable<any> {
    const url = `${this.apiDeleteProduct}?id=${productId}`;
    return this.http.delete<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  getMultiFillterProductForAdmin(search: string, categoryId: number, statusId: number, sortDirection: string): Observable<any> {
    const params = {
      search: search,
      categoryId: categoryId,
      statusId: statusId,
      sortDirection: sortDirection
    };

    const queryString = Object.entries(params)
      .filter(([key, value]) => {
        if (key === 'search' && value === '') return false;
        if (key === 'statusId' && value === 0) return false;
        if (key === 'categoryId' && value === 0) return false;
        return value != null;
      })
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');

    const url = `${this.apiUrl_getMultiFillterProductForAdmin}?${queryString}`;
    console.log(url);
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  getAllProductStatus(): Observable<any> {
    const url = `${this.getAllStatusProduct}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  getAllMaterialProduct(): Observable<any> {
    const url = `${this.getAllMaterial}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  getAllSubMaterialByMaterialIdProduct(materialId: number): Observable<any> {
    const url = `${this.getSubMaterialByMaterialId}?id=${materialId}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }
  createExportMaterialProduct(requestData: any): Observable<any> {
    return this.http.post<any>(this.api_UrlcreateExportMaterialProduct, requestData).pipe(
      catchError(this.handleError)
    );
  }
  exportMaterialProductByProductId(productId: number): Observable<any> {
    const url = `${this.api_UrlexportMaterialProductByProductId}?id=${productId}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  editProduct(productData: any, thumbnail: File | null, images: File[] | null, productId: number): Observable<any> {
    const formData = new FormData();
    formData.append('productDTO', new Blob([JSON.stringify(productData)], { type: 'application/json' }));
    if (thumbnail !== null) {
      formData.append('file_thumbnail', thumbnail, thumbnail.name);
    } else {
      formData.append('file_thumbnail', new Blob(), '');
    }
    // cho phep null anh neu khong update anh
    if (images !== null && images.length > 0) {
      images.forEach(image => {
        formData.append('files', image, image.name);
      });
    } else {
      formData.append('files', new Blob(), '');
    }
    const url = `${this.apiUrl_EditProduct}?product_id=${productId}`;
    return this.http.put(url, formData, {
      headers: {
        'Accept': 'application/json'
      }
    });
  }

  EditSubMaterialProduct(requestData: any): Observable<any> {
    return this.http.put<any>(this.apiUrlEditSubMaterialProduct, requestData).pipe(
      catchError(this.handleError)
    );
  }

  //for request product
  getAllProductRequest(): Observable<any> {
    return this.http.get<any>(this.apiUrlGetProductRequest).pipe(
      catchError(this.handleError)
    );
  }
  getMultiFillterProductForCustomer(search: string, categoryId: number, statusId: number, sortDirection: string): Observable<any> {
    const params = {
      search: search,
      categoryIds: categoryId,
      statusId: statusId,
      sortDirection: sortDirection
    };

    const queryString = Object.entries(params)
      .filter(([key, value]) => {
        if (key === 'search' && value === '') return false;
        if (key === 'statusId' && value === 0) return false;
        if (key === 'categoryIds' && value === 0) return false;
        if (key === 'sortDirection' && value === '') return false;
        return value != null;
      })
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');

    const url = `${this.apiUrl_GetMultiProductForCustomer}?${queryString}`;
    console.log(url);
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  getMultiFillterRequestProductForAdmin(search: string, statusId: number, sortDirection: string): Observable<any> {
    const params = {
      search: search,
      statusId: statusId,
      sortDirection: sortDirection,
    };

    const queryString = Object.entries(params)
      .filter(([key, value]) => {
        if (key === 'search' && value === '') return false;
        if (key === 'statusId' && value === 0) return false;

        return value != null;
      })
      .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
      .join('&');

    const url = `${this.apiUrl_getMultiFillterRequestProductForAdmin}?${queryString}`;
    console.log(url);
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  addNewProductRequest(productRequestData: any, images: File[]): Observable<any> {
    const formData = new FormData();

    formData.append('productDTO', new Blob([JSON.stringify(productRequestData)], { type: 'application/json' }));
    images.forEach(image => {
      formData.append('files', image, image.name);
    });
    const token = localStorage.getItem('loginToken');

    if (!token) {
      return throwError(new Error('Login token not found in localStorage.'));
    }

    const headers = new HttpHeaders({
      // 'Authorization': `Bearer ${token}`,
      'Accept': 'application/json'
    });

    // console.log("Authorization header:", headers.get('Authorization'));

    return this.http.post(this.apiUrlAddNewProductRequest, formData, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  createExportMaterialProductRequest(requestData: any): Observable<any> {
    return this.http.post<any>(this.api_UrlcreateExportMaterialProductRequest, requestData).pipe(
      catchError(this.handleError)
    );
  }
  getAllRequest(): Observable<any> {
    return this.http.get<any>(this.apiUrl_getAllRequest).pipe(
      catchError(this.handleError)
    );
  }
  getRequestProductById(requestId: number): Observable<any> {
    const url = `${this.apiUrlGetProductRequestByProductRequestId}?id=${requestId}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  editProductRequest(productData: any, images: File[] | null, productId: number): Observable<any> {
    const formData = new FormData();
    formData.append('productDTO', new Blob([JSON.stringify(productData)], { type: 'application/json' }));
    // cho phep null anh neu khong update anh
    if (images !== null && images.length > 0) {
      images.forEach(image => {
        formData.append('files', image, image.name);
      });
    } else {
      formData.append('files', new Blob(), '');
    }
    const url = `${this.apiUrlEditProductRequest}?product_id=${productId}`;
    return this.http.put(url, formData, {
      headers: {
        'Accept': 'application/json'
      }
    });
  }

  deleteProductRequest(productId: number): Observable<any> {
    const url = `${this.apiUrlDeleteProductRequest}?id=${productId}`;
    return this.http.delete<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  exportMaterialProductRequestByProductId(productId: number): Observable<any> {
    const url = `${this.api_UrlexportMaterialProductByProductRequestId}?id=${productId}`;
    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  EditSubMaterialRequestProduct(requestData: any): Observable<any> {
    return this.http.put<any>(this.api_UrlEditSubMateialRequestProduct, requestData).pipe(
      catchError(this.handleError));
  }
  //
}