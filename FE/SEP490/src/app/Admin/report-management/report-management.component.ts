import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { ProductListService } from 'src/app/service/product/product-list.service';

@Component({
  selector: 'app-report-management',
  templateUrl: './report-management.component.html',
  styleUrls: ['./report-management.component.scss']
})
export class ReportManagementComponent implements OnInit{
  constructor(private fb: FormBuilder,
    private productListService: ProductListService,
    private toastr: ToastrService) { }

    errorProducts: any[] = [];
  ngOnInit(): void {
  }
  getAllProductError(): void {
    this.productListService.getAllProductError().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.errorProducts = data.result;
          console.log('Danh sách sản phẩm:', this.errorProducts);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách sản phẩm!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    ); 
  }
}
