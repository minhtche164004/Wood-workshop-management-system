import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { JobService } from 'src/app/service/job.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { CalendarModule } from 'primeng/calendar';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { ProductService } from 'src/app/service/product.service';
import { SalaryService } from 'src/app/service/salary.service';
import { EmployeeService } from 'src/app/service/employee.service';
import { EmailService } from 'src/app/service/email.service';
import 'jquery';
import { DataService } from 'src/app/service/data.service';
declare var $: any;
@Component({
  selector: 'app-total-salary',
  templateUrl: './total-salary.component.html',
  styleUrls: ['./total-salary.component.scss']
})

export class TotalSalaryComponent implements OnInit {
  keyword = 'username';
  searchKey: string = '';
  totalSalary: any[] = [];
  currentPage: number = 1;
  employeeList: any[] = [];
  selectedEmp: any = {};
  positionEmpList: any[] = [];
  selectedPosition: any = 0;
  fromDate: string = '';
  toDate: string = '';
  isProduct: boolean = true; // check product or product request
  isLoadding: boolean = false;
  selectedPositionEmp: any = 0;
  bankList: any[] = [];
  ndChuyenKhoan: string = 'DoGoSyDung thanh toan tien cong (Ma: {{code}})'
  qrImageUrl: string = '';
  constructor(private dataService: DataService, private http: HttpClient, private toastr: ToastrService, private employeeService: EmployeeService, private jobService: JobService, private fb: FormBuilder, private productListService: ProductListService, private sanitizer: DomSanitizer, private productService: ProductService, private salaryService: SalaryService) { }


  ngOnInit(): void {
    this.getTotalSalary();
    this.getAllEmployee();
    this.getAllPostionEmp();
    this.getBankList();
  }
  getBankList(): void {
    this.salaryService.getBanks().subscribe(
      (data) => {
        this.bankList = data.data;
        //  console.log('Response from getBanks:', this.bankList);
        this.isLoadding = false;
      },
      (error) => {
        console.error('Error from getBanks:', error);
        this.isLoadding = false;
      }
    );
  }
  search() {
    this.isLoadding = true;
    console.log('From Date:', this.fromDate);
    console.log('To Date:', this.toDate);
    console.log('Selected Position:', this.selectedPosition);
    console.log('Keyword:', this.searchKey);

    this.salaryService.multSearchSalary(this.searchKey, this.fromDate, this.toDate, '').subscribe(
      (data) => {
        if (data.code === 1000) {
          this.totalSalary = data.result;
          console.log('Total salary: ', this.totalSalary); this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data); this.isLoadding = false;
        }

      },
      (error) => {
        console.log(error); this.isLoadding = false;
      }
    );

  }
  getTotalSalary() {
    this.isLoadding = true;
    this.salaryService.getSalary().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.totalSalary = data.result;
          console.log('Total salary: ', this.totalSalary); this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data); this.isLoadding = false;
        }

      },
      (error) => {
        console.log(error);
      }
    );
  }
  getAllPostionEmp(): void {
    this.isLoadding = true;
    this.employeeService.getAllPostionEmp().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.positionEmpList = data.result;
          console.log('Danh sách chuc vu nhan vien: ', this.positionEmpList); this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data); this.isLoadding = false;
        }

      },
    )
  }
  getAllEmployee(): void {
    this.isLoadding = true;
    this.employeeService.getAllEmployee().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.employeeList = data.result;
          console.log('Danh sách nhan vien: ', this.employeeList); this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data); this.isLoadding = false;
        }

      },
      (error) => {
        console.log(error); this.isLoadding = false;
      }
    );
  }
  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
  }
  selectEvent(item: any) {

  }
  selectEmp(product: any): void {
    this.selectedEmp = product; // Adjust based on your product object structure
    console.log('Lương nhân viên:', this.selectedEmp.username);
    this.searchSalary();

  }
  searchSalary(): void {
    this.isLoadding = true;
    console.log('Selected emp:', this.selectedEmp.username);
    this.salaryService.multSearchSalary(this.selectedEmp.username, '', '', '').subscribe(
      (data) => {
        if (data.code === 1000) {
          this.totalSalary = data.result;
          console.log('search luong bang ten: ', this.totalSalary); this.isLoadding = false;
        } else {
          console.error('Failed to fetch products:', data); this.isLoadding = false;
        }

      },
    )
  }

  onChangeSearch(event: any) {
    this.isLoadding = true;
    this.selectedEmp = event.target.value;
    console.log('Selected salary emp:', event.target.value); this.isLoadding = false;
  }

  onFocused(e: any) {
  }
  onSearchPosition(selectedPosition: any) {
    this.isLoadding = true;
    this.selectedPosition = selectedPosition;
    console.log('Selected position:', this.selectedPosition);
    this.countJobByUserId(this.selectedPosition);
  }
  countJobByUserId(id: number): void {
    this.isLoadding = true;
    this.employeeService.countJobByUserId(id).subscribe(
      (data) => {
        if (data.code === 1000) {
          console.log('Count job by user id:', data.result);
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
  getBinByBankNameOrShortName(bankName: string): string{
    // Chuyển đổi bankName sang chữ thường để so sánh không phân biệt hoa thường
    const normalizedBankName = bankName.toLowerCase();

    // Tìm ngân hàng trong bankList dựa trên shortName hoặc name
    const bankInfo = this.bankList.find(bank =>
      bank.shortName.toLowerCase() === normalizedBankName ||
      bank.name.toLowerCase() === normalizedBankName
    );

    // console.log('Return BIN:', bankInfo);
    return bankInfo ? bankInfo.bin : undefined;
  }
  getImageQR() {
    this.salaryService.getBanks().subscribe();

  }
  thanhToan(product: any): void {
    this.isLoadding = true;
    console.log('Thanh toan:', product);
    console.log('Amount: ', product.amount);
    console.log('accountId: ', product.user?.userInfor?.bank_number)
    console.log('username: ', product.user?.username)
    console.log('code: ', product.code)
   // const bankName = product.user?.userInfor?.bank_name;
    const formattedNdChuyenKhoan = this.ndChuyenKhoan.replace('{{code}}', product.code.toString());

    console.log('orderInfo:  ', formattedNdChuyenKhoan);

     const bin = this.getBinByBankNameOrShortName(product.user?.userInfor?.bank_name);
    console.log('BIN:', bin);
    this.salaryService.getQRBanking(product.amount, product.user?.userInfor?.bank_number, product.user?.username, bin, formattedNdChuyenKhoan)
      .subscribe(
        (response) => {

          this.qrImageUrl = response;
          console.log('QR Image URL:', this.qrImageUrl);
          this.isLoadding = false;

        },  
        (error) => {
          console.error('API Error:', error);
          this.toastr.error('Có lỗi xảy ra khi tạo mã QR thanh toán. Vui lòng thử lại sau.', 'Lỗi');
          // Xử lý lỗi nếu có


          $('[data-dismiss="modal"]').click();
          this.isLoadding = false;
        }

      );

  }

}


