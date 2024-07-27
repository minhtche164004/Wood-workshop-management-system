import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ROUTES, RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module'; // Import AppRoutingModule
import { ToastrModule, provideToastr } from 'ngx-toastr';
import { AppComponent } from './app.component';
import { HomepageComponent } from './2.Customer/homepage/homepage.component';
import { RegisterComponent } from './register/register.component';
import { VerifyOtpComponent } from './verify-otp/verify-otp.component';
import { ProductComponent } from './product/product.component';
import { HeaderComponent } from './Layout/header/header.component';
import { FooterComponent } from './Layout/footer/footer.component';
import { PageAdminComponent } from './Admin/page-admin/page-admin.component';
import { UserManagementComponent } from './Admin/user-management/user-management.component';
import { DashboardComponent } from './Layout/dashboard-manager/dashboard.component';
import { PaymentSalaryComponent } from './payment-salary/payment-salary.component';
import { VerifyMailComponent } from './forgotPassword/verify-mail/verify-mail.component';
import { VerifyOtpMailComponent } from './forgotPassword/verify-otp-mail/verify-otp-mail.component';
import { ApiProvinceComponent } from './api-province/api-province.component';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ProductListComponent } from './product-list/product-list.component';
import { ProductManagementComponent } from './3.Manager/product-management/product-management.component';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { NgxPaginationModule } from 'ngx-pagination';
import { SupplierManagementComponent } from 'src/app/3.Manager/supplier-management/supplier-management.component';
import { ViewProfileComponent } from './2.Customer/view-profile/view-profile.component';
import { SubMaterialManagementComponent } from './3.Manager/sub-material-management/sub-material-management.component';
import { ProductDetailComponent } from './2.Customer/product-detail/product-detail.component';
import { OrderRequiredComponent } from './2.Customer/order-required/order-required.component';
import { CreateOrderComponent } from './2.Customer/create-order/create-order.component';
import { TestUploadComponent } from './test-upload/test-upload.component';
import { OrderDetailManagementComponent } from './3.Manager/order-detail-management/order-detail-management.component';

import { ProcessProductManagementComponent } from './3.Manager/process-product-management/process-product-management.component';
import { OrderRequestComponentComponent } from './3.Manager/order-request-component/order-request-component.component';
import { OrderRqDetailComponent } from './3.Manager/order-rq-detail/order-rq-detail.component';
import { OrderManagementComponent } from './3.Manager/order-management/order-management.component';
import { JobManagementComponent } from './3.Manager/job-management/job-management.component';
import { ForgotPassComponent } from './forgotPassword/forgot-pass/forgot-pass.component';
import { ChangePasswordComponent } from './2.Customer/change-password/change-password.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { ContactComponent } from './contact/contact.component';
import { EmployeeJobComponent } from './employee-job/employee-job.component';
import { BillComponent } from './bill/bill.component';
import { MaterialManagementComponent } from './3.Manager/material-management/material-management.component';
import { ReportManagementComponent } from './3.Manager/report-management/report-management.component';
import { AdvancedBillManagementComponent } from './3.Manager/advanced-bill-management/advanced-bill-management.component';
import { FeedbackDefectiveComponent } from './3.Manager/feedback-defective/feedback-defective.component';
import { TotalSalaryComponent } from './3.Manager/total-salary/total-salary.component';
import { AutoCompleteComponent } from './auto-complete/auto-complete.component';
import {AutocompleteLibModule} from 'angular-ng-autocomplete';
import { WishlistComponent } from './2.Customer/wishlist/wishlist.component';
import { TemplateComponent } from './2.Customer/template/template.component';
import { ListRequestProductComponent } from './2.Customer/list-request-product/list-request-product.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatNativeDateModule } from '@angular/material/core';
import { CalendarModule } from 'primeng/calendar';
import { HistoryOrderComponent } from './2.Customer/history-order/history-order.component';

import { DashboardEmployeeComponent } from './Layout/dashboard-employee/dashboard-employee.component';
import { DashboardAdminComponent } from './Layout/dashboard-admin/dashboard-admin.component';
import { ListJobDoneEmployeeComponent } from './4.Employee/list-job-done-employee/list-job-done-employee.component';
import { ListJobDoneComponent } from './Admin/list-job-done/list-job-done.component';
import { ReportCostManagementComponent } from './3.Manager/report-cost-management/report-cost-management.component';
import { ChartComponent } from './Admin/chart/chart.component';

import { ListRequestCustomerComponent } from './3.Manager/list-request-customer/list-request-customer.component';
import { ListSalaryEmployeeComponent } from './4.Employee/list-salary-employee/list-salary-employee.component';
import { OrderSuccessComponent } from './Order-payment-vnpay/order-success/order-success.component';
import { OrderFailComponent } from './Order-payment-vnpay/order-fail/order-fail.component';
import { EmployeeSubmaterialManagementComponent } from './employee-submaterial-management/employee-submaterial-management.component';
import { ImpactHistoryManagerComponent } from './3.Manager/impact-history-manager/impact-history-manager.component';
import { LoginAdminComponent } from './login-admin/login-admin.component';

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
    BillComponent,
    MaterialManagementComponent,
    ReportManagementComponent,
    AdvancedBillManagementComponent,
    FeedbackDefectiveComponent,
    TotalSalaryComponent,
    WishlistComponent,
    AutoCompleteComponent,
    AutoCompleteComponent,
    TemplateComponent,
    HistoryOrderComponent,
    ListRequestProductComponent,

    DashboardEmployeeComponent,
    DashboardAdminComponent,
    ListJobDoneEmployeeComponent,
    ListJobDoneComponent,
  

    ReportCostManagementComponent,
    ChartComponent,
    ListRequestCustomerComponent,
    ListSalaryEmployeeComponent,
    OrderSuccessComponent,
    OrderFailComponent,
    EmployeeSubmaterialManagementComponent,
    ImpactHistoryManagerComponent,
    LoginAdminComponent,

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
    AutocompleteLibModule,
    MatDatepickerModule,
    MatFormFieldModule,
    CalendarModule,
    MatNativeDateModule,

    
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