import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { JobService } from 'src/app/service/job.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { CalendarModule } from 'primeng/calendar';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ProductService } from 'src/app/service/product.service';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import 'jquery';
import { EmployeeService } from '../service/employee.service';

declare var $: any;
export interface Position {
  position_id: number;
  position_name: string;
}

export interface Employee {
  userId: number;
  username: string;
  password: string;
  email: string;
  position: Position;
  hireDate: string;
}

export interface Status {
  status_id: number;
  status_name: string;
  type: number;
}

export interface Category {
  categoryId: number;
  categoryName: string;
}

export interface Product {
  productId: number;
  productName: string;
  description: string;
  quantity: number;
  price: number;
  image: string;
  completionTime: string;
  enddateWarranty: string;
  code: string;
  type: number;
  status: Status;
  categories: Category;
}

export interface SubMaterial {
  subMaterialId: number;
  subMaterialName: string;
  description: string;
  quantity: number;
  unitPrice: number;
  code: string;
}

export interface ProductSubMaterial {
  productSubMaterialId: number;
  subMaterials: SubMaterial[];
  product: Product;
  quantity: number;
}

export interface ApiResponse {
  empMaterialId: number;
  employee: Employee;
  productSubMaterial: ProductSubMaterial;
  requestProductsSubmaterials: any; // Define this more specifically based on actual data structure if available
}

@Component({
  selector: 'app-employee-submaterial-management',
  templateUrl: './employee-submaterial-management.component.html',
  styleUrls: ['./employee-submaterial-management.component.scss']
})

export class EmployeeSubmaterialManagementComponent implements OnInit {
  isLoadding: boolean = false;
  subMaterialList: any[] = [];
  checkNotFound: boolean = false;
  currentPage: number = 1;
  empSubmtrList: any;
  selectedDetail: any;
  ngOnInit(): void {
    this.loadSubMaterialForEmp();
  }
  constructor(private empService: EmployeeService, private fb: FormBuilder, private productList: ProductService, private productListService: ProductListService, private jobService: JobService, private toastr: ToastrService, private sanitizer: DomSanitizer) {


  }

  loadSubMaterialForEmp() {
    this.isLoadding = true;
    this.empService.getSubMaterialForEmp().subscribe(
      (data) => {
        this.subMaterialList = data.result;
        console.log('SubMaterial for emp:', this.subMaterialList);
        this.isLoadding = false
      },
      (error) => {
        console.log('Error:', error);
        this.isLoadding = false;
      } 
    );
  }
  product: any;

  showDetails(subMaterial: any) {
    this.selectedDetail = subMaterial;
    console.log('Selected detail:', this.selectedDetail);
    this.empSubmtrList = this.selectedDetail.productSubMaterial.subMaterial; // Assuming subMaterial is a single object
    console.log('SubMaterial:', this.empSubmtrList);
  }
}
