// app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomepageComponent } from './2.Customer/homepage/homepage.component';
import { ProductComponent } from './product/product.component'; // Import ProductComponent
import { RegisterComponent } from './register/register.component';
import { VerifyOtpComponent } from './verify-otp/verify-otp.component';
import { PageAdminComponent } from './Admin/page-admin/page-admin.component';
import { UserManagementComponent } from './Admin/user-management/user-management.component';
import { PaymentSalaryComponent } from './payment-salary/payment-salary.component';
import { VerifyMailComponent } from './forgotPassword/verify-mail/verify-mail.component';

import { ApiProvinceComponent } from './api-province/api-province.component';
import { ProductListComponent } from './product-list/product-list.component';
import { VerifyOtpMailComponent } from './forgotPassword/verify-otp-mail/verify-otp-mail.component';
import { ProductManagementComponent } from './3.Manager/product-management/product-management.component';
import { ViewProfileComponent } from './2.Customer/view-profile/view-profile.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { SubMaterialManagementComponent } from './3.Manager/sub-material-management/sub-material-management.component';
import { SupplierManagementComponent } from './3.Manager/supplier-management/supplier-management.component';
import { OrderDetailManagementComponent } from './3.Manager/order-detail-management/order-detail-management.component';
import { OrderRequestComponentComponent } from './3.Manager/order-request-component/order-request-component.component';
import { OrderRqDetailComponent } from './3.Manager/order-rq-detail/order-rq-detail.component';
import { OrderRequiredComponent } from './2.Customer/order-required/order-required.component';
import { CreateOrderComponent } from './2.Customer/create-order/create-order.component';
import { OrderManagementComponent } from './3.Manager/order-management/order-management.component';
import { ProductDetailComponent } from './2.Customer/product-detail/product-detail.component';
import { JobManagementComponent } from './3.Manager/job-management/job-management.component';


import { ForgotPassComponent } from './forgotPassword/forgot-pass/forgot-pass.component';
import { ChangePasswordComponent } from './2.Customer/change-password/change-password.component';



import { ContactComponent } from './contact/contact.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { EmployeeJobComponent } from './employee-job/employee-job.component';

import { WishlistComponent } from './2.Customer/wishlist/wishlist.component';
import { TemplateComponent } from './2.Customer/template/template.component';

import { BillComponent } from './bill/bill.component';
import { MaterialManagementComponent } from './3.Manager/material-management/material-management.component';
import { ReportManagementComponent } from './3.Manager/report-management/report-management.component';
import { AdvancedBillManagementComponent } from './3.Manager/advanced-bill-management/advanced-bill-management.component';
import { FeedbackDefectiveComponent } from './3.Manager/feedback-defective/feedback-defective.component';
import { TotalSalaryComponent } from './3.Manager/total-salary/total-salary.component';
import { AutoCompleteComponent } from './auto-complete/auto-complete.component';
import { HistoryOrderComponent } from './2.Customer/history-order/history-order.component';

import { ListRequestProductComponent } from './2.Customer/list-request-product/list-request-product.component';
import { DashboardComponent } from './Layout/dashboard-manager/dashboard.component';
import { ListJobDoneEmployeeComponent } from './4.Employee/list-job-done-employee/list-job-done-employee.component';
import { ListJobDoneComponent } from './Admin/list-job-done/list-job-done.component';

import { ReportCostManagementComponent } from './3.Manager/report-cost-management/report-cost-management.component';
import { Chart } from 'chart.js';
import { ChartComponent } from './Admin/chart/chart.component';
import { ListRequestCustomerComponent } from './3.Manager/list-request-customer/list-request-customer.component';
import { ListSalaryEmployeeComponent } from './4.Employee/list-salary-employee/list-salary-employee.component';
//danh` cho order-vnpay
import { OrderSuccessComponent } from './Order-payment-vnpay/order-success/order-success.component';
import { OrderFailComponent } from './Order-payment-vnpay/order-fail/order-fail.component';
import { AuthGuard } from './AuthGuard';
import { EmployeeSubmaterialManagementComponent } from './employee-submaterial-management/employee-submaterial-management.component';





const routes: Routes = [
  { path: 'homepage', component: HomepageComponent }, 
  { path: 'api-province', component: ApiProvinceComponent },
  { path: 'login', component: RegisterComponent },
  { path: 'otp', component: VerifyOtpComponent },
  { path: 'admin', component: UserManagementComponent , canActivate: [AuthGuard], data: { roles: ['ADMIN'] }},
  { path: 'verifyMail', component: VerifyMailComponent },
  { path: 'verifyOtp', component: VerifyOtpMailComponent },
  { path: 'change_pass', component: ChangePasswordComponent },
  { path: 'admin_manager_user', component: UserManagementComponent , canActivate: [AuthGuard], data: { roles: ['ADMIN'] }},
  { path: 'payment-salary', component: PaymentSalaryComponent },
  { path: 'verifyOtp/:email', component: VerifyOtpMailComponent },  // Route with email as a parameter
  { path: 'forgot_pass/:email', component: ForgotPassComponent },

  { path: 'product', component: ProductComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }}, 
  { path: 'productsList', component: ProductListComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }}, 
  { path: 'product_management', component: ProductManagementComponent, canActivate: [AuthGuard], data: { roles: ['MANAGER'] } },
  { path: 'order_rq', component: OrderRequiredComponent , canActivate: [AuthGuard], data: { roles: ['CUSTOMER'] }}, 
  { path: 'supplier_management', component: SupplierManagementComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }}, 
  { path: 'profile', component: ViewProfileComponent },
  { path: 'submtr_management', component: SubMaterialManagementComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }}, 
  { path: 'create_order', component: CreateOrderComponent , canActivate: [AuthGuard], data: { roles: ['CUSTOMER'] }}, 
  { path: 'upload', component: FileUploadComponent },
  { path: 'product_list', component: ProductListComponent },
  { path: 'order_detail_management', component: OrderDetailManagementComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'order_request_management', component: OrderRequestComponentComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'order_request_management/:id', component: OrderRequestComponentComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'orderRq_detail', component: OrderRqDetailComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'contact', component: ContactComponent },
  { path: 'about-us', component:  AboutUsComponent},
  { path: 'order_management', component: OrderManagementComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'product-details', component: ProductDetailComponent },
  { path: 'job_management', component: JobManagementComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }}, 
  { path: 'changePassWord', component: ChangePasswordComponent },
  { path: 'employee-job', component: EmployeeJobComponent },
  { path: 'bill', component: BillComponent },
  { path: 'product-details/:id', component: ProductDetailComponent },

  { path: 'wishlist', component: WishlistComponent },
  { path: 'template', component: TemplateComponent },

  { path: 'ds', component: DashboardComponent },

  { path: 'static-report', component: ChartComponent, canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'material_management', component: MaterialManagementComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'report_management', component: ReportManagementComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'report_management/:id', component: ReportManagementComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'advanced_bill', component: AdvancedBillManagementComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'feedback_defective', component: FeedbackDefectiveComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'total_salary', component: TotalSalaryComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }  },
  { path: 'autoComplete', component: AutoCompleteComponent },
  { path: 'history_order', component: HistoryOrderComponent , canActivate: [AuthGuard], data: { roles: ['CUSTOMER'] }}, 
  { path: 'request_product', component: ListRequestProductComponent , canActivate: [AuthGuard], data: { roles: ['CUSTOMER'] }}, 
  
  { path: 'employee', component: ListJobDoneEmployeeComponent , canActivate: [AuthGuard], data: { roles: ['EMPLOYEE'] }}, 
  { path: 'list_job_done', component: ListJobDoneComponent , canActivate: [AuthGuard], data: { roles: ['ADMIN'] }}, 
  { path: 'list_request_customer', component: ListRequestCustomerComponent , canActivate: [AuthGuard], data: { roles: ['MANAGER'] }}, 
  { path: 'list_salary_employee', component: ListSalaryEmployeeComponent , canActivate: [AuthGuard], data: { roles: ['EMPLOYEE'] }}, 

  //danh` cho order-vnpay
  { path: 'order-vnpay-success', component: OrderSuccessComponent },
  { path: 'order-vnpay-fail', component: OrderFailComponent },

  
  // { path: '', redirectTo: '/homepage', pathMatch: 'full' },
  { path: 'report-cost', component: ReportCostManagementComponent },
  // { path: '', redirectTo: '/homepage', pathMatch: 'full' },

  // { path: '**', redirectTo: '/homepage', pathMatch: 'full' },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }