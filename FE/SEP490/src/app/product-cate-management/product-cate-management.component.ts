import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ProductListService } from '../service/product/product-list.service';
import { ErrorProductService } from '../service/error-product.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-product-cate-management',
  templateUrl: './product-cate-management.component.html',
  styleUrls: ['./product-cate-management.component.scss']
})
export class ProductCateManagementComponent implements OnInit{
  
  ngOnInit(): void {
   
  }
  constructor(private fb: FormBuilder,
    private route: ActivatedRoute,
    private productListService: ProductListService,
    private errorProductService: ErrorProductService,
    private toastr: ToastrService) {
    }
  isLoadding: boolean = false;
  currentPage: number = 1;
  errorProducts: any[] = [];
}
