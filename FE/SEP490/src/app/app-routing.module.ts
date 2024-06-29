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
import { ProductListComponent } from './product-list/product-list.component';
import { VerifyOtpMailComponent } from './forgotPassword/verify-otp-mail/verify-otp-mail.component';
import { ProductManagementComponent } from './Admin/product-management/product-management.component';
import { ViewProfileComponent } from './features/view-profile/view-profile.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { SubMaterialManagementComponent } from './Admin/sub-material-management/sub-material-management.component';
import { SupplierManagementComponent } from './supplier-management/supplier-management.component';
import { OrderDetailManagementComponent } from './Admin/order-detail-management/order-detail-management.component';
import { OrderRequestComponentComponent } from './Admin/order-request-component/order-request-component.component';
import { OrderRqDetailComponent } from './Admin/order-rq-detail/order-rq-detail.component';
import { OrderRequiredComponent } from './order-required/order-required.component';
import { CreateOrderComponent } from './create-order/create-order.component';
import { OrderManagementComponent } from './Admin/order-management/order-management.component';
import { ProductDetailComponent } from './features/product-detail/product-detail.component';
import { JobManagementComponent } from './Admin/job-management/job-management.component';
import { JobDetailComponent } from './Admin/job-detail/job-detail.component';
import { ContactComponent } from './contact/contact.component';
import { AboutUsComponent } from './about-us/about-us.component';
import { EmployeeJobComponent } from './employee-job/employee-job.component';


const routes: Routes = [
  { path: 'homepage', component: HomepageComponent },
  { path: 'api-province', component: ApiProvinceComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'otp', component: VerifyOtpComponent },
  { path: 'admin', component: PageAdminComponent },
  { path: 'verifyMail', component: VerifyMailComponent },
  { path: 'verifyOtp', component: VerifyOtpMailComponent },
  { path: 'change_pass', component: ChangePasswordComponent },
  { path: 'user_management', component: UserManagementComponent },
  { path: 'payment-salary', component: PaymentSalaryComponent },
  { path: 'verifyOtp/:email', component: VerifyOtpMailComponent },  // Route with email as a parameter
  { path: 'change_pass/:email', component: ChangePasswordComponent },  // Route with email as a parameter
  { path: 'product', component: ProductComponent }, // Add route for ProductComponent
  { path: 'productsList', component: ProductListComponent },
  { path: 'product_management', component: ProductManagementComponent },
  { path: 'order_rq', component: OrderRequiredComponent },
  { path: 'supplier_management', component: SupplierManagementComponent },
  { path: 'profile', component: ViewProfileComponent },
  { path: 'submtr_management', component: SubMaterialManagementComponent },
  { path: 'create_order', component: CreateOrderComponent },
  { path: 'upload', component: FileUploadComponent },
  { path: 'product_list', component: ProductListComponent },
  { path: 'order_detail_management', component: OrderDetailManagementComponent },
  { path: 'order_request_management', component: OrderRequestComponentComponent },
  { path: 'order_request_management/:id', component: OrderRequestComponentComponent },
  { path: 'orderRq_detail', component: OrderRqDetailComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'about-us', component:  AboutUsComponent},
  { path: 'order_management', component: OrderManagementComponent },
  { path: 'product_details', component: ProductDetailComponent },
  { path: 'job_management', component: JobManagementComponent },
  { path: 'job_detail', component: JobDetailComponent },
  { path: 'employee-job', component: EmployeeJobComponent },
  { path: '', redirectTo: '/homepage', pathMatch: 'full' },
  { path: '**', redirectTo: '/homepage', pathMatch: 'full' },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
