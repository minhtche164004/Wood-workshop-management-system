import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs';

// @Injectable()
// export class AuthInterceptor implements HttpInterceptor {
//   intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
//     const token = localStorage.getItem('loginToken');
//     if (token) {
//       // Nếu có token, thêm vào header Authorization
//       const authReq = req.clone({
//         headers: req.headers.set('Authorization', `Bearer ${token}`)
//       });
//       return next.handle(authReq);
//     } else {
//       // Nếu không có token, để request đi qua không thay đổi
//       return next.handle(req);
//     }
//   }

// }

  