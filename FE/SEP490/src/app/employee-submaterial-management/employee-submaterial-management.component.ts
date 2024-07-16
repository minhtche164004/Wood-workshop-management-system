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
  subMaterialList: any;
  checkNotFound: boolean = false;
  currentPage: number = 1;
  empSubmtrList: any;
  selectedDetail: any;
  products: any[] = []; // Biến để lưu trữ danh sách sub-materials
  loginToken: string | null = null;

  selectedMaterial: any = null;
  searchKey: string = '';
  categories: any[] = [];
  selectedFile: File | undefined;
  keyword = 'fullname';
  sub_material_name: string = '';
  SubMaterData: any = {};
  description: string = '';
  quantity: number = 0;
  unit_price: number = 0;
  selectedSubMtr: any = {};
  isProduct: boolean = true; // check product or product request
  empList: any;
  employeeList: any[] = [];
  employeeInfoList: { fullname: any }[] = [];
  ngOnInit(): void {
    this.loadSubMaterialForEmp();
    this.getAllEmployee();
  }
  constructor(private empService: EmployeeService, private fb: FormBuilder, private productList: ProductService, private productListService: ProductListService, private jobService: JobService, private toastr: ToastrService, private sanitizer: DomSanitizer) {


  }
  selectProduct(product: any): void {
    this.isLoadding = true;
    this.selectedSubMtr = product; // Adjust based on your product object structure
    console.log('Selected mtr seacu:', this.selectedSubMtr.subMaterialName);


  }
  getAllEmployee(): void {
    this.isLoadding = true;
    this.empService.getAllEmployee().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.employeeList = data.result;

          this.employeeInfoList = this.employeeList.map(employee => {
            return {
              fullname: employee.userInfor?.fullname,
            };
          });

          console.log('fullname: ', this.employeeInfoList);
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data);
           this.isLoadding = false;
        }

      },
      (error) => {
        console.log(error);
        this.isLoadding = false;
      }
    );
  }
  selectedEmp: any;
  selectEmp(product: any): void {
    
    this.selectedEmp = product;
    // Clear the search key before performing the search
    console.log('Selected employee:', this.selectedEmp.fullname);
    this.searchKey = this.selectedEmp.fullname;
    this.searchSalary();
  }
  searchSalary() {
    this.isLoadding = true
    console.log('Search key:', this.searchKey);
    this.empService.searchEmployeeByName(this.searchKey).subscribe(
      (data) => {

        this.subMaterialList = data.result;
        console.log('Search result:', data.result);
        this.checkNotFound = false;
        this.isLoadding = false;
      },
      (error) => {
        console.log('Error:', error);
        this.checkNotFound = true;
        this.isLoadding = false;
      }
    );
  }
  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
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

  }
}
