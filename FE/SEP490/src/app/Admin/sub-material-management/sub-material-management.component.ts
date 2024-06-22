import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SupplierService } from 'src/app/service/supplier.service';
import { ToastrService } from 'ngx-toastr';
import { MaterialService } from 'src/app/service/material.service';
import { FormControl } from '@angular/forms';

interface SubMaterial {
  sub_material_name: string,
  material_id: number,
  description: string,
  quantity: number | undefined,
  unit_price: number
}
@Component({
  selector: 'app-sub-material-management',
  templateUrl: './sub-material-management.component.html',
  styleUrls: ['./sub-material-management.component.scss']
})
export class SubMaterialManagementComponent implements OnInit {
  products: any[] = []; // Biến để lưu trữ danh sách sub-materials
  loginToken: string | null = null;
  currentPage: number = 1;
  selectedMaterial: any = null;
  searchKey: string = '';
  categories: any[] = [];

  sub_material_name: string ='';
  
  description: string = '';
  quantity: number = 0;
  unit_price: number = 0;
  constructor(
    private materialService: MaterialService,
    private toastr: ToastrService
  ) { }

  ngOnInit() {
    this.getAllMaterials();
    this.getAllSubMaterials();

  }

  getAllSubMaterials(): void {
    this.materialService.getAllSubMaterials().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          console.log('Danh sách Sub-Materials:', this.products);
        } else {
          console.error('Failed to fetch sub-materials:', data);
          this.toastr.error('Không thể lấy danh sách sub-materials!', 'Lỗi'); // Hiển thị thông báo lỗi
        }
      },
      (error) => {
        console.error('Error fetching sub-materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Hiển thị thông báo lỗi chung
      }
    );
  }
  getAllMaterials(): void {
    this.materialService.getAllSubMaterials().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.categories = data.result;
          console.log('Danh sách Materials:', this.categories);
        } else {
          console.error('Failed to fetch materials:', data);
          this.toastr.error('Không thể lấy danh sách materials!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }
  addSubMaterial(): void {
    console.log("Bắt đầu chạy thêm vật liệu")
    const subMaterial: SubMaterial = {
      sub_material_name: this.sub_material_name,
      material_id: this.selectedMaterial,
      description: this.description,
      quantity: this.quantity,
      unit_price: this.unit_price,
    };

    console.log('SubMaterial request:', subMaterial);
    this.materialService.addNewSubMaterial(subMaterial).subscribe(
      (response) => {
        if (response.code === 1000) {
          this.toastr.success('Sub-material added successfully!', 'Success');
          this.getAllSubMaterials(); // Refresh the list of sub-materials
        } else {
          this.toastr.error('Failed to add sub-material!', 'Error');
        }
      },
      (error) => {
        console.error('Error adding sub-material:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );

  }

  searchSubMaterial(): void {
    console.log("Thực hiện chức năng tìm kiếm nguyên vật liệu: ", this.searchKey);
    this.materialService.searchSubMaterial(this.searchKey).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          console.log('Kết quả tìm kiếm Sub-Materials:', this.products);
        } else {
          console.error('Failed to search sub-materials:', data);
          this.toastr.error('Không thể tìm kiếm sub-materials!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error searching sub-materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
      }
    );
  }

}
