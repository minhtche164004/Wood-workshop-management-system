import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { JobService } from 'src/app/service/job.service';
import { FormBuilder } from '@angular/forms';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ProductService } from 'src/app/service/product.service';
import { EmployeeService } from '../service/employee.service';

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
  searchKey: any;
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
  employeeInfoList: any[] = [];
  selectedEmp: any;

  constructor(
    private empService: EmployeeService,
    private fb: FormBuilder,
    private productList: ProductService,
    private productListService: ProductListService,
    private jobService: JobService,
    private toastr: ToastrService,
    private sanitizer: DomSanitizer
  ) { }

  ngOnInit(): void {
    this.loadSubMaterialForEmp();
    this.getAllEmployee();
  }

  selectProduct(event: any): void {
    this.searchKey = event.item.fullname;
    console.log('Selected fullname:', this.searchKey);
  }

  selectEmp(employee: any): void {
    this.selectedEmp = employee;
    this.searchKey = this.selectedEmp.fullname;
    console.log('Selected employee:', this.selectedEmp);
    console.log('Search key:', this.searchKey, typeof this.searchKey);
    console.log('Selected employee :', typeof this.searchKey);
  }
  searchSalary() {
    this.isLoadding = true;
    
    console.log('Search key seacrSaka:',this.searchKey.fullname as string);
    
    this.empService.searchEmployeeByName(this.searchKey.fullname as string || this.searchKey as string).subscribe(
      (data) => {
        if(data.code === 1000) {
          this.currentPage = 1;
          this.subMaterialList = data.result;
  
          console.log('Search result:', data.result);
          console.log('Search cpde:', data.code);
          this.checkNotFound = false;
          this.isLoadding = false;
        } else if(data.code ===1015){
          this.checkNotFound = true;
          this.isLoadding = false;
          this.subMaterialList = '';
        }

       
      },
      (error) => {
        console.error('Error fetching categories:', error);
        this.checkNotFound = false;this.isLoadding = false;
      }
 
    );
  }


  getAllEmployee(): void {
    this.isLoadding = true;
    this.empService.getAllEmployee().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.employeeList = data.result;
          console.log("Employee list:", this.employeeList);
          const uniqueUserIds = new Set<number>();

          this.employeeInfoList = this.employeeList.filter(employee => {
            if (!uniqueUserIds.has(employee.userId)) {
              uniqueUserIds.add(employee.userId);
              return true;
            }
            return false;
          }).map(employee => {
            return {
              fullname: employee.userInfor.fullname,
              userId: employee.userId
            };
          });

          // this.employeeInfoList = [
          //   {'fullname': 'hehe'},
          //   {'fullname': 'haha'}
          // ];
          //console.log('employeeList: ', this.employeeList);
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

  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
  }

  loadSubMaterialForEmp() {
    this.isLoadding = true;
    this.empService.getSubMaterialForEmp().subscribe(
      (data) => {
        this.subMaterialList = data.result;
     //   console.log('SubMaterial for emp:', this.subMaterialList);
        this.isLoadding = false;
      },
      (error) => {
        console.log('Error:', error);
        this.isLoadding = false;
      }
    );
  }

  showDetails(subMaterial: any) {
    this.selectedDetail = subMaterial;
    console.log('Selected detail:', this.selectedDetail);
  }
}
