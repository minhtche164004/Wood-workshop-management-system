import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/app/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductListService {

  private apiAddProduct = `${environment.apiUrl}api/auth/product/AddNewProduct`;
  private apiDeleteProduct = `${environment.apiUrl}api/auth/product/DeleteProduct`; // Assuming the delete endpoint
  private api_findProductByCategory = `${environment.apiUrl}api/auth/product`;
  private apiUrlGetProduct = `${environment.apiUrl}api/auth/product/getAllProductForCustomer`;
  private apiUrl = `${environment.apiUrl}api/auth/product/getAllProductForAdmin`;
  private apiUrl_Cate = `${environment.apiUrl}api/auth/product/GetAllCategory`;
  private apiUrl_GetAllUser = `${environment.apiUrl}api/auth/admin/GetAllUser`;
  private apiUrl_Position = `${environment.apiUrl}api/auth/admin/GetAllPosition`;

  private apiUrl_findProduct = `${environment.apiUrl}api/auth/product`;
  private apiUrl_getProductByID = `${environment.apiUrl}api/auth/product/GetProductById`; // Assuming the correct endpoint

  private apiUrl_GetAllOrder = `${environment.apiUrl}api/auth/order/GetAllOrder`; // Assuming the correct endpoint


  private apiUrl_AllRole = `${environment.apiUrl}api/auth/admin/GetAllRole`; // Assuming the correct endpoint



  private apiUrl_AddProduct = `${environment.apiUrl}api/auth/product/AddNewProduct`;

  private apiUrl_UpdateProduct = `${environment.apiUrl}api/auth/product/AddNewProduct`;

  private apiUrl_getMultiFillterProductForAdmin = `${environment.apiUrl}api/auth/product/getMultiFillterProductForAdmin`;
  private getAllStatusProduct = `${environment.apiUrl}api/auth/product/GetStatusProduct`;  //sau lay api khac thay vao` api nay bi thieu
  private getAllMaterial = `${environment.apiUrl}api/auth/getAll`;  // lay cac vat lieu
  private getSubMaterialByMaterialId = `${environment.apiUrl}api/auth/submaterial/FilterByMaterial`;  // lay cac vat lieu con theo vat lieu cha 
  
  private api_UrlcreateExportMaterialProduct = `${environment.apiUrl}api/auth/submaterial/createExportMaterialProduct`;  // luu 1 san pham can bao nhieu vat lieu
 
  private api_UrlexportMaterialProductByProductId = `${environment.apiUrl}api/auth/submaterial/FilterByMaterial`;  // lay tat ca vat lieu can co de tao 1 san pham

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
    const url = `${this.apiUrl_getProductByID}?product_id=${productId}`;
    return this.http.get<any>(url);
  }
  deleteProduct(productId: number): Observable<any> {
    const url = `${this.apiDeleteProduct}?product_id=${productId}`;
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
    return this.http.post<any>(this.api_UrlcreateExportMaterialProduct, productId).pipe(
      catchError(this.handleError)
    );
  }
}