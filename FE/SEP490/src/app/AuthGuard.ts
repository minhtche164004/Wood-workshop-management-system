import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthenListService } from 'src/app/service/authen.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private toastr: ToastrService,
    private authenListService: AuthenListService,
    private router: Router
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (!this.authenListService.isLoggedIn()) {
      this.toastr.error('Vui lòng đăng nhập');
      this.router.navigate(['/login']);
      return false;
    }

    //
    const expectedRoles = route.data['roles'];
    if (!expectedRoles || expectedRoles.length === 0 || expectedRoles.includes('GUEST')) {
      this.router.navigate(['/login']);
      return true;  
    }

    return this.authenListService.getUserProfile().pipe(
      map(data => {
        const userRole = data.result?.role_name;
        if (expectedRoles.includes(userRole)) {
          return true;
        } else {
          this.router.navigate(['/homepage']);
          // this.toastr.error('Vai trò của bạn không hợp lệ');
          return false;
        }
      })
    );
  }
}
