import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SupplierService } from 'src/app/service/supplier.service';
import { ToastrService } from 'ngx-toastr';
import { MaterialService } from 'src/app/service/material.service';
import { FormControl } from '@angular/forms';

interface ApiResponse {
  code: number;
  result: any[]; // Or a specific interface for the result structure
}
interface Material {
  materialId: string;
  materialName: string;
}
interface Supplier {
  sub_material_id: number;
  supplierName: string;
  phoneNumber: string;
}
@Component({
  selector: 'app-supplier-management',
  templateUrl: './supplier-management.component.html',
  styleUrls: ['./supplier-management.component.scss']
})
export class SupplierManagementComponent implements OnInit{
  suppliers: any[] = []; // Array to store supplier data
  currentPage: number = 1;
  searchKey: string = '';
  supplierName: string = '';
  phoneNumber: string = '';
  materials: any[] = [];
  selectedMaterial: any = null;
  materialControl = new FormControl();
  constructor(private supplierService: SupplierService, private materialService: MaterialService, private toastr: ToastrService) { }

  ngOnInit() {
    this.loadMaterials();
    this.supplierService.getAllSuppliers().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.suppliers = data.result;
          console.log('Danh sách nhà cung cấp: :', this.suppliers);
        } else {
          console.error('Failed to fetch products:', data);
          this.toastr.error('Không thể lấy danh sách nhà cung cấp!', 'Lỗi'); // Display error toast
        }
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Display generic error toast
      }
    );
  }
  searchSupplier(): void {
    console.log("Thực hiện tìm kiếm nhà cung cấp: ", this.searchKey);
    if (this.searchKey) {
      this.supplierService.findSupplierName(this.searchKey)
        .subscribe(
          (data) => {
            if (data.code === 1000) {
              this.suppliers = data.result;
              console.log('Tìm kiếm thành công:', this.suppliers);
              console.log('searchKey:', this.searchKey);
              this.toastr.success('Tìm kiếm nhà sản xuất vật liệu!', 'Thành công');
            } else if (data.code === 1015) {
              this.suppliers = []; // Clear previous results
              console.error('Tìm kiếm không thành công:', data);
              this.toastr.error('Không tìm thấy nhà sản xuất vật liệu!', 'Tìm kiếm thất bại');
              // Handle specific error message
            }
          }
        );
    } else {
      console.warn('Từ khóa tìm kiếm trống.');
      // Optionally display a message to the user indicating an empty search term
    }
  }

  addSupplier(): void {
    
    const supplier: Supplier = {
      sub_material_id: this.selectedMaterial, 
      supplierName: this.supplierName,
      phoneNumber: this.phoneNumber,
    };
    console.log('Supplier Request:', supplier);
    this.supplierService.addNewSupplier(supplier)
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.toastr.success('Nhà cung cấp đã được thêm thành công!', 'Thành công');
            this.suppliers.push(supplier);
            this.supplierName = '';
            this.phoneNumber = '';
          } else {  
            console.error('Failed to add supplier:', data);
            this.toastr.error('Có lỗi khi thêm nhà cung cấp!', 'Lỗi');
          }
        },
        (error) => {
          console.error('Error adding supplier:', error);
          this.toastr.error('Có lỗi khi thêm nhà cung cấp!', 'Lỗi');
        }
      );


  }

 
  loadMaterials(): void {
    this.materialService.getAllMaterial().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.materials = data.result;
          console.log('Danh sách Vật liệu:', this.materials);
        } else {
          console.error('Dữ liệu trả về không hợp lệ:', data);
        }
      },
      (error) => {
        console.error('Lỗi khi lấy danh sách Loại:', error);
      }
    );

  }
}
