import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SupplierService } from 'src/app/service/supplier.service';
import { ToastrService } from 'ngx-toastr';
import { MaterialService } from 'src/app/service/material.service';
import { TypeMaterialService } from 'src/app/service/type-material.service';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-material-management',
  templateUrl: './material-management.component.html',
  styleUrls: ['./material-management.component.scss']
})
export class MaterialManagementComponent implements OnInit {
  products: any[] = [];
  currentPage: number = 1;
  
  ngOnInit(): void {
    this.getAllMaterials();
  }
  constructor(private typeMaterialService: TypeMaterialService, private toastr: ToastrService) { }

  getAllMaterials(): void {
    this.typeMaterialService.getAllTypeMateriall().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          console.log('Danh sách Type-Materials:', this.products);
        } else {
          console.error('Failed to fetch type-materials:', data);
          this.toastr.error('Không thể lấy danh sách type-materials!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Hiển thị thông báo lỗi chung
      }
    )
  }
  
}
