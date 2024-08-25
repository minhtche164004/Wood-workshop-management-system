import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { AuthenListService } from 'src/app/service/authen.service';
import { LoadingService } from './LoadingService';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private toastr: ToastrService,
    private authenListService: AuthenListService,
    private router: Router,
    private loadingService: LoadingService

  ) {}
  
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    this.loadingService.setLoading(true);

    if (!this.authenListService.isLoggedIn()) {
      this.toastr.warning('Vui lòng đăng nhập');
      this.router.navigate(['/homepage']);
      this.loadingService.setLoading(false);
      return false;
    }

    //
    const expectedRoles = route.data['roles'];
    if (!expectedRoles || expectedRoles.length === 0) {
      this.loadingService.setLoading(false);
      return true;  
    }

    const token = localStorage.getItem('loginToken');

    return this.authenListService.getUserProfile().pipe(
      map(data => {
        const userRole = data.result?.role_name;
        this.loadingService.setLoading(false);
        if (expectedRoles.includes(userRole)) {
          this.loadingService.setLoading(false);
          return true;
        } 
        else {
          this.router.navigate(['/homepage']);
          this.loadingService.setLoading(false);
          // this.toastr.error('Vai trò của bạn không hợp lệ');
          return false;
        }
      }),
      catchError(error => {
        this.loadingService.setLoading(false);
        if (error.status === 401 && error.error?.code === 1004) {
          this.router.navigate(['/login']); // co token nhung token het han
        } else {
          this.router.navigate(['/homepage']);
        }
        return of(false);
      })
    );
  }
}
