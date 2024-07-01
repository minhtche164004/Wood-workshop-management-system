import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ROUTES, RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module'; // Import AppRoutingModule
import { ToastrModule, provideToastr } from 'ngx-toastr';
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

import { VerifyOtpMailComponent } from './forgotPassword/verify-otp-mail/verify-otp-mail.component';
import { ApiProvinceComponent } from './api-province/api-province.component';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ProductListComponent } from './product-list/product-list.component';

import { ProductManagementComponent } from './Admin/product-management/product-management.component';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { NgxPaginationModule } from 'ngx-pagination';

import { SupplierManagementComponent } from 'src/app/supplier-management/supplier-management.component';

import { ViewProfileComponent } from './features/view-profile/view-profile.component';
import { FileUploadComponent } from './file-upload/file-upload.component';

import { SubMaterialManagementComponent } from './Admin/sub-material-management/sub-material-management.component';

import { ProductDetailComponent } from './features/product-detail/product-detail.component';

import { OrderRequiredComponent } from './order-required/order-required.component';
import { CreateOrderComponent } from './create-order/create-order.component';
import { TestUploadComponent } from './test-upload/test-upload.component';
import { OrderDetailManagementComponent } from './Admin/order-detail-management/order-detail-management.component';
import { OrderDetailComponent } from './Admin/order-detail/order-detail.component';
import { ProcessProductManagementComponent } from './Admin/process-product-management/process-product-management.component';
import { OrderRequestComponentComponent } from './Admin/order-request-component/order-request-component.component';
import { OrderRqDetailComponent } from './Admin/order-rq-detail/order-rq-detail.component';

import { OrderManagementComponent } from './Admin/order-management/order-management.component';

import { JobManagementComponent } from './Admin/job-management/job-management.component';


import { ForgotPassComponent } from './forgotPassword/forgot-pass/forgot-pass.component';
import { ChangePasswordComponent } from './features/change-password/change-password.component';

import { AboutUsComponent } from './about-us/about-us.component';
import { ContactComponent } from './contact/contact.component';
import { EmployeeJobComponent } from './employee-job/employee-job.component';
import { SubSubmaterialProductComponent } from './sub-submaterial-product/sub-submaterial-product.component';
import { BillComponent } from './bill/bill.component';
import { MaterialManagementComponent } from './Admin/material-management/material-management.component';
import { ReportManagementComponent } from './Admin/report-management/report-management.component';
import { AdvancedBillManagementComponent } from './Admin/advanced-bill-management/advanced-bill-management.component';
import { FeedbackDefectiveComponent } from './Admin/feedback-defective/feedback-defective.component';
import { TotalSalaryComponent } from './Admin/total-salary/total-salary.component';







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
    ForgotPassComponent,
    VerifyOtpMailComponent,
    ApiProvinceComponent,
    ProductListComponent,
    OrderRequiredComponent,
    ProductManagementComponent,
    ViewProfileComponent,
    SubMaterialManagementComponent,
    SupplierManagementComponent,

    ProductDetailComponent,
    CreateOrderComponent,

    TestUploadComponent,
    OrderDetailManagementComponent,
    OrderDetailComponent,
    ProcessProductManagementComponent,
    OrderRequestComponentComponent,
    OrderRqDetailComponent,

    OrderManagementComponent,
    ChangePasswordComponent,
    JobManagementComponent,


    ForgotPassComponent,

    AboutUsComponent,
    ContactComponent,
    EmployeeJobComponent,
    SubSubmaterialProductComponent,
    BillComponent,
    MaterialManagementComponent,
    ReportManagementComponent,
    AdvancedBillManagementComponent,
    FeedbackDefectiveComponent,
    TotalSalaryComponent,

    

  

  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule, // Import RouterModule
    NgxPaginationModule,
    AppRoutingModule, // Import AppRoutingModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    ConfirmDialogModule,
    ConfirmPopupModule,
  
  ],
  
  providers: [
    // {
    //   provide: HTTP_INTERCEPTORS,
    //   useClass: AuthInterceptor,
    //   multi: true
    // },

    provideToastr(),


  ],
  bootstrap: [AppComponent]
})
export class AppModule { }