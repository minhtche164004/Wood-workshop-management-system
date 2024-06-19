import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module'; // Import AppRoutingModule

import { AppComponent } from './app.component';
import { HomepageComponent } from './features/homepage/homepage.component';
import { RegisterComponent } from './register/register.component';
import { VerifyOtpComponent } from './verify-otp/verify-otp.component';
import { ProductComponent } from './product/product.component';
import { HeaderComponent } from './Layout/header/header.component';
import { FooterComponent } from './Layout/footer/footer.component';
import { PageAdminComponent } from './Admin/page-admin/page-admin.component';
import { UserManagementComponent } from './Admin/user-management/user-management.component';
import { DashboardComponent } from './Layout/dashboard/dashboard.component';
import { PaymentSalaryComponent } from './payment-salary/payment-salary.component';
import { VerifyMailComponent } from './forgotPassword/verify-mail/verify-mail.component';
import { ChangePasswordComponent } from './forgotPassword/change-password/change-password.component';
import { VerifyOtpMailComponent } from './forgotPassword/verify-otp-mail/verify-otp-mail.component';
import { ApiProvinceComponent } from './api-province/api-province.component';
import { ReactiveFormsModule } from '@angular/forms';

import { ProductListComponent } from './product-list/product-list.component';
import { OrderRequiredComponent } from './order-required/order-required.component';
import { ProductManagementComponent } from './Admin/product-management/product-management.component';
import { NgxPaginationModule } from 'ngx-pagination';

// import { AuthInterceptor } from './service/auth.interceptor';
@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    RegisterComponent,
    VerifyOtpComponent,
    ProductComponent,
    HeaderComponent,
    FooterComponent,
    PageAdminComponent,
    UserManagementComponent,
    DashboardComponent,
    PaymentSalaryComponent,
    VerifyMailComponent,
    ChangePasswordComponent,
    VerifyOtpMailComponent,
    ApiProvinceComponent,
    ProductListComponent,
    OrderRequiredComponent,
    ProductManagementComponent,

  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule, // Import RouterModule
    NgxPaginationModule,
    AppRoutingModule, // Import AppRoutingModule,
    ReactiveFormsModule

  ],
  providers: [
    // {
    //   provide: HTTP_INTERCEPTORS,
    //   useClass: AuthInterceptor,
    //   multi: true
    // }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
