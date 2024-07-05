import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SupplierService } from 'src/app/service/supplier.service';
import { ToastrService } from 'ngx-toastr';
import { MaterialService } from 'src/app/service/material.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenListService } from '../service/authen.service';

interface ApiResponse {
  code: number;
  result: any[]; // Or a specific interface for the result structure
}

interface Material {
  materialId: string;
  materialName: string;
}
interface SubMaterial {
  subMaterialId: string;
  subMaterialName: string;
}
interface Supplier {
  sub_material_id: number;
  supplierName: string;
  phoneNumber: string;
}
interface EditUserRequest {
supplierName: string,
  phoneNumber: string,
  sub_material_id: string
}
@Component({
  selector: 'app-supplier-management',
  templateUrl: './supplier-management.component.html',
  styleUrls: ['./supplier-management.component.scss']
})
export class SupplierManagementComponent implements OnInit {
  suppliers: any[] = []; // Array to store supplier data
  currentPage: number = 1;
  searchKey: string = '';
  supplierName: string = ''; 
  phoneNumber: string = '';
  subMaterialName: string = '';
  subMaterialId: string = '';
  selectedMaterial_Update: any = null; // Assuming selectedRole should be a boolean
  materials: any[] = []; 
  sub_material: SubMaterial[] = [];
  selectedMaterial: any = null;
  suplierData: any = {};
  editSupplierForm: FormGroup;
  materialControl = new FormControl();
  deleteId: any; // variable to store supplierId to be deleted

  constructor(private authenListService: AuthenListService,
    private supplierService: SupplierService, private materialService: MaterialService, private toastr: ToastrService, private fb: FormBuilder) {
      this.editSupplierForm = this.fb.group({
        supplierName: ['', Validators.required],
        phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9]{10}$/)]], // Adjusted phone number validation to regex
        sub_material_id: ['', Validators.required],
        subMaterialId: [''], // Add control for subMaterialName
        subMaterialName: [''], // Add control for subMaterialName
      });
    }

  ngOnInit() { 
    this.suplierData = {
      supplierMaterial: '',
      supplierName: '',
      phoneNumber: '',
      subMaterialId: '',
      subMaterialName: '',
    };
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
      supplierName: this.supplierName,
      phoneNumber: this.phoneNumber,
      sub_material_id: this.selectedMaterial, 
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
            window.location.reload();
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

  setDeleteId(supplierId: number) {
    console.log('Set deleteId:', supplierId);
    this.deleteId = supplierId;
  }

  deleteSupplier() {
    console.log('Delete supplier:', this.deleteId);
    this.supplierService.deleteSupplier(this.deleteId).subscribe(
      response => {
        console.log('Supplier deleted successfully!', response);
        this.toastr.success('Nhà cung cấp đã được xóa thành công!', 'Thành công');
        this.ngOnInit(); // Refresh the list of suppliers
        window.location.reload();
      },
      error => {
        console.error('Error deleting supplier:', error);
        // Handle error logic
      }
    );
  }

  loadMaterials(): void {
    this.authenListService.getAllSubMaterialName().subscribe(
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



  getDatasupplierMaterial(id: string): void {
    this.authenListService.getSupplierById(id).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.suplierData = data.result;
          this.selectedMaterial = this.materials.find(material => material.subMaterialName === this.suplierData.subMaterial.subMaterialName)?.subMaterialId;
          console.log('KVL: ', this.selectedMaterial);
        } else {
          console.error('Failed to fetch supplier data:', data);
        }
      },
      (error) => {
        console.error('Error fetching supplier data:', error);
      }
    );
  }

  EditSupMaterial(): void {

    const editUserRequest: EditUserRequest = this.editSupplierForm.value;
    const userId = this.suplierData.supplierMaterial; // Lấy userId từ userData
    console.log("Data: ", editUserRequest)
    this.authenListService.EditSupplier(userId, editUserRequest).subscribe(
      () => {

        this.toastr.success('Thay đổi thông tin thành công.');
        window.location.reload();
      (error: any) => {
        this.toastr.error('Lỗi');
       
      }}
    );
  }

}
