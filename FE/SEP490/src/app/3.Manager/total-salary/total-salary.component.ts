import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
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
  @ViewChild('launchModalButton')
  launchModalButton!: ElementRef;
  keyword = 'fullname';
  searchKey: string = '';
  totalSalary: any[] = [];
  currentPage: number = 1;
  employeeList: any[] = [];
  selectedEmp: any = {};
  positionEmpList: any[] = [];
  selectedPosition: any = '';
  fromDate: string = '';
  toDate: string = '';
  isProduct: boolean = true; // check product or product request
  isLoadding: boolean = false;
  selectedPositionEmp: any = '';
  bankList: any[] = [];
  ndChuyenKhoan: string = 'DoGoSyDung thanh toan tien cong (Ma: {{code}})'
  qrImageUrl: string = '';
  startDate: string = '';
  endDate: string = '';
  position: number = 0;
  selectedBanking: any;
  selectedStatusBanking: string = '0';
  advanceSuccessValues: any = {
    trueValue: true,
    falseValue: false
  };

  constructor(private dataService: DataService, private http: HttpClient, private toastr: ToastrService, private employeeService: EmployeeService, private jobService: JobService, private fb: FormBuilder, private productListService: ProductListService, private sanitizer: DomSanitizer, private productService: ProductService, private salaryService: SalaryService) { }

  ngOnInit(): void {
    this.loadData();
  }
  
  loadData(): void {
    this.isLoadding = true;
  
    const employeePromise = this.getAllEmployee();
    const positionEmpPromise = this.getAllPostionEmp();
    const bankListPromise = this.getBankList();
    const totalSalaryPromise = this.getTotalSalary();
  
    Promise.all([employeePromise, positionEmpPromise, bankListPromise, totalSalaryPromise])
      .then(() => {
        this.isLoadding = false;
        console.log('All data loaded successfully');
      })
      .catch((error) => {
        console.error('Error loading data:', error);
        this.isLoadding = false;
      });
  }
  
  
  getBankList(): Promise<void> {
    console.log('Get bank list');
    return new Promise((resolve, reject) => {
      this.salaryService.getBanks().subscribe(
        (data) => {
          this.bankList = data.data;
          resolve();
        },
        (error) => {
          console.error('Error from getBanks:', error);
          reject(error);
        }
      );
    });
  }
  
  getAllEmployee(): Promise<void> {
    console.log('Get all employee');
    return new Promise((resolve, reject) => {
      this.employeeService.getAllEmployee().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.employeeList = data.result;
            this.employeeInfoList = this.employeeList.map(employee => ({
              fullname: employee.userInfor?.fullname,
            }));
            resolve();
          } else {
            console.error('Failed to fetch employees:', data);
            reject(data);
          }
        },
        (error) => {
          console.error('Error from getAllEmployee:', error);
          reject(error);
        }
      );
    });
  }
  
  getAllPostionEmp(): Promise<void> {
    console.log('Get all position emp');
    return new Promise((resolve, reject) => {
      this.employeeService.getAllPostionEmp().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.positionEmpList = data.result;
            resolve();
          } else {
            console.error('Failed to fetch positions:', data);
            reject(data);
          }
        },
        (error) => {
          console.error('Error from getAllPostionEmp:', error);
          reject(error);
        }
      );
    });
  }
  search() {
    this.isLoadding = true;
    this.searchSalary();

  }
  
  getTotalSalaryCalled: boolean = false;

  getTotalSalary(): Promise<void> {
    if (this.getTotalSalaryCalled) {
      return Promise.resolve();  // Trả về promise ngay lập tức nếu hàm đã được gọi trước đó
    }
    
    this.getTotalSalaryCalled = true;  // Đặt cờ để chỉ thị hàm đã được gọi
  
    return new Promise((resolve, reject) => {
      this.salaryService.getSalary().subscribe(
        (data) => {
          if (data.code === 1000) {
            this.totalSalary = data.result;
            console.log('Total salary: ', this.totalSalary);
            resolve();
          } else {
            console.error('Failed to fetch salary:', data);
            reject(data);
          }
        },
        (error) => {
          console.error('Error from getTotalSalary:', error);
          reject(error);
        }
      );
    });
  }
  
 
  employeeFullnames: any[] = [];
  employeeInfoList: { fullname: any }[] = [];
  
  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
  }

  selectEmp(product: any): void {
    this.selectedEmp = product;
    // Clear the search key before performing the search
    //  console.log('Selected employee:', this.selectedEmp.fullname);
    this.searchKey = this.selectedEmp.fullname;
    this.searchSalary();
  }

  checkNotFound: boolean = false;


  searchSalary(): void {
    this.checkNotFound = false;
    this.isLoadding = true;
    this.searchKey.trim();
     console.log('Search key before search:', this.searchKey);
     console.log('Selected position:', this.selectedStatusBanking);
    let startDate: string = this.fromDate || '';
    let endDate: string = this.toDate || '';
    if (this.fromDate) {
      startDate = this.fromDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }

    if (this.toDate) {
      endDate = this.toDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }
    if(this.fromDate > this.toDate) {
      this.toastr.warning('Từ ngày không được sau ngày kết thúc', 'Thông báo');
      this.isLoadding = false;
    }
    this.salaryService.multSearchSalary(this.searchKey, startDate, endDate, '', this.selectedPosition, this.selectedStatusBanking).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.totalSalary = data.result;
          console.log('search luong bang ten: ', this.totalSalary);
          this.currentPage = 1;
          //this.selectedEmp = '';
          //     this.searchKey = '';
       
        } else if(data.code === 1015) {
          this.totalSalary = [];

    

        }
        this.isLoadding = false;
      },
      (error) => {
        console.log(error);
        this.checkNotFound = true;
        this.isLoadding = false;
      }
    )
  }

  updateBanking(jobId: number, event: Event): void {
    this.isLoadding = true;
    const newValue = (event.target as HTMLSelectElement).value;
    //   console.log('Giá trị được chọn:', newValue);
    // console.log('Job ID:', jobId);
    this.salaryService.updateBanking(jobId, newValue).subscribe(
      (data) => {
        if (data.code === 1000) {
          //    console.log('Update banking thanh cong');
          this.toastr.success('Cập nhật trạng thái thanh toán thành công', 'Thành công');
          this.getTotalSalary();
          this.isLoadding = false;

        } else {
          //     console.error('Failed to fetch products:', data);
          this.toastr.warning('Có lỗi xảy ra khi cập nhật trạng thái thanh toán. Vui lòng thử lại sau.', 'Thông báo');
          this.isLoadding = false;
        }
      }
    )
  }
  // onChangeSearch(event: any) {
  //   this.isLoadding = true;
  //   this.selectedEmp = event.target.value;
  //   console.log('Selected salary emp:', event.target.value);
  //   this.isLoadding = false;
  // }

  // onFocused(e: any) {
  // }
  onSearchPosition(selectedPosition: any) {
    this.isLoadding = true;
    this.selectedPosition = selectedPosition;
    //   console.log('Selected position:', this.selectedPosition);
    this.searchSalary();
  }
  countJobByUserId(id: number): void {
    this.isLoadding = true;
    this.employeeService.countJobByUserId(id).subscribe(
      (data) => {
        if (data.code === 1000) {
          //      console.log('Count job by user id:', data.result);
          this.isLoadding = false;
        } else {
          //      console.error('Failed to fetch products:', data);
          this.isLoadding = false;
        }

      },
      (error) => {
        console.log(error);
        this.isLoadding = false;
      }
    );
  }
  getBinByBankNameOrShortName(bankName: string): string {
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
  updateBankingModal(jobId: any): void {
    this.isLoadding = true;
    console.log("Selected Banking: ", jobId)
    const newValue = true;
    console.log('Job ID:', jobId, 'newValue:', newValue);
    this.salaryService.updateBanking(jobId, newValue).subscribe(
      (data) => {
        if (data.code === 1000) {
          //   console.log('Update banking thanh cong');
          this.toastr.success('Cập nhật trạng thái thanh toán thành công', 'Thành công');
          this.getTotalSalary();
          this.isLoadding = false;
          $('[data-dismiss="modal"]').click();
        } else {
          //    console.error('Failed to fetch products:', data);
          this.toastr.warning('Có lỗi xảy ra khi cập nhật trạng thái thanh toán. Vui lòng thử lại sau.', 'Thông báo');
          $('[data-dismiss="modal"]').click();
          this.isLoadding = false;
        }
      }
    )
  }
  confirmThanhToan(): void {
    //   console.log('Confirm thanh toan: ', this.selectedBanking);

  }
  changeSalaryStatus: boolean = false;
  modalThanhToan(job: any, event: Event): void {
    console.log('Job salary modal:', job);
    this.selectedBankingID = job;
    const newValue2 = (event.target as HTMLSelectElement).value;
   
    console.log('Giá trị được chọn:', newValue2);
    this.launchModalButton.nativeElement.click();

  }
  selectedBankingID: any;
  thanhToan(product: any): void {
    this.isLoadding = true;
    this.qrImageUrl = '';

     console.log('Thanh toan:', product);
    
    //  console.log('Amount: ', product.amount);
    //  console.log('accountId: ', product.user?.userInfor?.bank_number)
    //  console.log('username: ', product?.user?.userInfor?.fullname)
    //  console.log('code: ', product.code)
    this.selectedBankingID = product.advanceSalaryId;
    console.log('selectedBankingId: ', this.selectedBankingID);
    const bankName = product.user?.userInfor?.bank_name;
    const formattedNdChuyenKhoan = this.ndChuyenKhoan.replace('{{code}}', product.code.toString());

    //  console.log('orderInfo:  ', formattedNdChuyenKhoan);

    const bin = this.getBinByBankNameOrShortName(product.user?.userInfor?.bank_name);
    //    console.log('BIN:', bin);
    this.salaryService.getQRBanking(product.amount, bankName, product?.user?.userInfor?.fullname, bin, formattedNdChuyenKhoan)
      .subscribe(
        (response) => {
          if (response) {
            this.qrImageUrl = response;
        //    console.log('QR Image URL:', this.qrImageUrl);
            $('[data-dismiss="modal"]');
            this.isLoadding = false;
           
          } else {
            // Nếu phản hồi không có dữ liệu hợp lệ
            console.error('Invalid response:', response);
            this.toastr.warning('Có lỗi xảy ra khi tạo mã QR thanh toán. Vui lòng thử lại sau.', 'Thông báo');
            $('[data-dismiss="modal"]');
          this.isLoadding = false;
         
          }
      
        },
        (error) => {
          console.error('API Error:', error);
          this.toastr.error('Có lỗi xảy ra khi tạo mã QR thanh toán. Vui lòng thử lại sau.', 'Thông báo');
          // Xử lý lỗi nếu có
          $('[data-dismiss="modal"]');
          this.isLoadding = false;
         
        }
        
      );

  }
  closeModal():void {
    console.log('Close modal');
    this.getTotalSalaryCalled = false;
    this.searchSalary();
  }

}
