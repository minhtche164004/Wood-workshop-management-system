import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
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
      this.toastr.error('Vui lòng đăng nhập');
      this.router.navigate(['/login']);
      this.loadingService.setLoading(false);
      return false;
    }

    //
    const expectedRoles = route.data['roles'];
    if (!expectedRoles || expectedRoles.length === 0) {
      this.loadingService.setLoading(false);
      return true;  
    }

    return this.authenListService.getUserProfile().pipe(
      map(data => {
        const userRole = data.result?.role_name;
        this.loadingService.setLoading(false);
        if (expectedRoles.includes(userRole)) {
          return true;
        } 
        else if (this.authenListService.isLoggedIn() && !expectedRoles.includes(userRole)) { 
          this.router.navigate(['/login']); // co token nhung token het han
          return false;
        }
        else {
          this.router.navigate(['/homepage']);
          // this.toastr.error('Vai trò của bạn không hợp lệ');
          return false;
        }
      })
    );
  }
}
