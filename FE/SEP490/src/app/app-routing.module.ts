// app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomepageComponent } from './features/homepage/homepage.component';
import { ProductComponent } from './product/product.component'; // Import ProductComponent
import { RegisterComponent } from './register/register.component';
import { VerifyOtpComponent } from './verify-otp/verify-otp.component';
import { PageAdminComponent } from './Admin/page-admin/page-admin.component';
import { UserManagementComponent } from './Admin/user-management/user-management.component';
import { PaymentSalaryComponent } from './payment-salary/payment-salary.component';
import { VerifyMailComponent } from './forgotPassword/verify-mail/verify-mail.component';
import { ChangePasswordComponent } from './forgotPassword/change-password/change-password.component';
import { ApiProvinceComponent } from './api-province/api-province.component';

const routes: Routes = [
  { path: 'homepage', component: HomepageComponent },
  { path: 'api-province', component: ApiProvinceComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'otp', component: VerifyOtpComponent },
  { path: 'admin', component: PageAdminComponent },
  { path: 'verifyMail', component: VerifyMailComponent },
  { path: 'verifyOtp', component: VerifyOtpComponent },
  { path: 'change_pass', component: ChangePasswordComponent },
  { path: 'user_management', component: UserManagementComponent },
  { path: 'payment-salary', component: PaymentSalaryComponent },
  { path: 'product', component: ProductComponent }, // Add route for ProductComponent
  { path: '', redirectTo: '/homepage', pathMatch: 'full' },
  { path: '**', redirectTo: '/homepage', pathMatch: 'full' },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
