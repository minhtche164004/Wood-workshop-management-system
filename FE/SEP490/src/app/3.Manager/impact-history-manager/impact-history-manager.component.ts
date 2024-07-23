import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { AuthenListService } from 'src/app/service/authen.service';
import { OrderRequestService } from 'src/app/service/order-request.service';
interface ApiResponse {
  code: number;
  result: any[];
}
@Component({
  selector: 'app-impact-history-manager',
  templateUrl: './impact-history-manager.component.html',
  styleUrls: ['./impact-history-manager.component.scss']
})
export class ImpactHistoryManagerComponent implements OnInit {
  list_submaterinput_manage: any[] = [];
  isLoadding: boolean = false;
  loginToken: string | null = null;
  currentPage: number = 1;
  searchKey: string = '';
  history_impact: any[] = [];
  selectedSDate: string = '';
  selectedEDate: string = '';
  selectedSortByPrice: string = '0';
  constructor(

    private authenListService: AuthenListService,
    private orderRequestService: OrderRequestService,
    private fb: FormBuilder,
    private http: HttpClient,
    private toastr: ToastrService,
    private router: Router) {

  }
  ngOnInit(): void {


    this.loginToken = localStorage.getItem('loginToken');
    this.loadAllHistoryImpact();


  }
  loadAllHistoryImpact() {
    this.isLoadding = true;
    this.loginToken = localStorage.getItem('loginToken');
    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.authenListService.getAllInputSubMaterial().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.list_submaterinput_manage = data.result;
            this.isLoadding = false;

          } else {
            console.error('Failed to fetch products:', data);
            this.isLoadding = false;
          }
        },
        (error) => {
          console.error('Error fetching products:', error);
          this.isLoadding = false;
        }
      );
    } else {
      console.error('No loginToken found in localStorage.');
      this.isLoadding = false;

    }
  }
  filterHistory_Impace(): void {
    // console.log("Lọc sản phẩm với từ khóa:", this.searchKey, ", danh mục:", this.selectedCategory, "và giá:", this.selectedSortByPrice);
    this.isLoadding = true;
    let startDate: string = this.selectedSDate || '';
    let endDate: string = this.selectedEDate || '';
    if (this.selectedSDate) {
      startDate = this.selectedSDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }

    if (this.selectedEDate) {
      endDate = this.selectedEDate.replace(/(\d{2})\/(\d{2})\/(\d{4})/, "$3/$2/$1");
    }
    this.authenListService.getMultiFillterRHistoryImpact(this.searchKey, this.selectedSortByPrice, startDate,
      endDate)
      .subscribe(
        (data) => {
          this.isLoadding = false;
          if (data.code === 1000) {
            this.list_submaterinput_manage = data.result;


          } else if (data.code === 1015) {
            this.list_submaterinput_manage = [];
            //    console.error('Lọc lịch sử thay đổi không thành công:', data);

          }
        }
      );
  }
}
