import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { SupplierService } from 'src/app/service/supplier.service';
import { ToastrService } from 'ngx-toastr';
import { MaterialService } from 'src/app/service/material.service';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { SubMaterialService } from 'src/app/service/sub-material.service';
import { AuthenListService } from 'src/app/service/authen.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { data } from 'jquery';

interface SubMaterial {
  sub_material_name: string,
  material_name: string,
  description: string,
  quantity: number | undefined,
  unit_price: number
}
declare var $: any; // khai bao jquery

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
  keyword = 'sub_material_name';
  sub_material_name: string = '';
  SubMaterData: any = {};
  description: string = '';
  quantity: number = 0;
  unit_price: number = 0;
  selectedSubMtr: any = {};
  isProduct: boolean = true; // check product or product request
  isLoadding: boolean = false;
  originalSubMaterial: any = {};
  selectedSubMaterial: any = {
    sub_material_id: null,
    sub_material_name: '',
    material_id: null,
    description: '',
    material_name: '',
    quantity: 0,
    unit_price: 0
  };
  editForm: FormGroup;
  createJobs: FormGroup;
  selectedSubMtr2: any = {
    sub_material_name: '',
    description: '',
    quantity: 0,
    unit_price: 0
    // Add more fields as needed
  };
  constructor(
    private subMaterialService: SubMaterialService,
    private materialService: MaterialService,
    private toastr: ToastrService,
    private authenListService: AuthenListService,
    private sanitizer: DomSanitizer,
    private fb: FormBuilder,
  ) {
    this.editForm = this.fb.group({
      sub_material_id: [''],
      sub_material_name: [''],
      material_id: [''],
      description: [''],
      material_name: [''],
      quantity: [''],
      unit_price: ['']
    });
    this.createJobs = this.fb.group({
      sub_material_id: [''],
      sub_material_name: [''],
      material_id: [''],
      description: [''],
      material_name: [''],
      quantity: [''],
      unit_price: ['']
    });
  }

  ngOnInit(): void {
    console.log("Bắt đầu chạy sub-material-management.component.ts")
    this.getAllMaterials();
    this.getAllSubMaterials();

  }

  getAllSubMaterials(): void {
    this.isLoadding = true;
    this.materialService.getAllSubMaterials().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          console.log('Danh sách Sub-Materials:', this.products);
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch sub-materials:', data);
          this.toastr.error('Không thể lấy danh sách sub-materials!', 'Lỗi'); 
          this.isLoadding = false;// Hiển thị thông báo lỗi
        }
      },
      (error) => {
        console.error('Error fetching sub-materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Hiển thị thông báo lỗi chung
        this.isLoadding = false;
      }
    );
  }
  dowloadExcelLink(): void {
    this.subMaterialService.downloadExcel().subscribe(
      (response) => {
        // Assuming the response is the file data
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const url = window.URL.createObjectURL(blob);

        // Creating an anchor element to trigger download
        const anchor = document.createElement('a');
        anchor.href = url;
        anchor.download = 'downloaded_file.xlsx'; // Specify the file name here
        document.body.appendChild(anchor); // Append anchor to the body to make it clickable
        anchor.click();

        // Clean up
        document.body.removeChild(anchor);
        window.URL.revokeObjectURL(url); // Revoke the blob URL to free up resources
      },
      (error) => {
        console.error('Download error:', error);
      }
    );
  }
  getAllMaterials(): void {
    this.isLoadding = true;
    this.materialService.getAllMaterial().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.categories = data.result;
          console.log('Danh sách Materials:', this.categories);
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch materials:', data);
          this.toastr.error('Không thể lấy danh sách materials!', 'Lỗi');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error fetching materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    );
  }
  addSubMaterial(): void {
    this.isLoadding = true;
    //  console.log("Bắt đầu chạy thêm vật liệu")
    const subMaterial: SubMaterial = {
      sub_material_name: this.sub_material_name,
      material_name: this.selectedMaterial,
      description: this.description,
      quantity: this.quantity,
      unit_price: this.unit_price,
    };

    console.log('SubMaterial request:', subMaterial);
    this.materialService.addNewSubMaterial(subMaterial).subscribe(
      (response) => {
        if (response.code === 1000) {
          this.toastr.success('Thêm nguyên vật liệu mới thành công!', 'Success');
          this.getAllSubMaterials(); // Refresh the list of sub-materials
          $('[data-dismiss="modal"]').click();
          this.isLoadding = false;
        } else {
          this.toastr.error('Failed to add sub-material!', 'Error');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error adding sub-material:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    );

  }
  saveChanges() {
    this.isLoadding = true;
    // Here, you can access the updated values from selectedSubMtr2
    const formData = this.editForm.value;
    console.log('Updated Data:', formData);
    this.subMaterialService.editSubMaterial(formData.sub_material_id, formData).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.toastr.success('Cập nhật nguyên vật liệu thành công!', 'Thành công');
          this.getAllSubMaterials();
          $('[data-dismiss="modal"]').click();
          this.isLoadding = false;
        } else {
          console.error('Failed to search sub-materials:', data);
          this.toastr.error('Không thể tìm kiếm sub-materials!', 'Lỗi');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error searching sub-materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    )

    // );
    // Example: You can send the updated data to your API endpoint here
    // Replace with your actual API call logic
    // this.yourService.updateSubMaterial(this.selectedSubMtr2).subscribe(response => {
    //   console.log('Updated successfully:', response);
    //   // Handle success or error response
    // });
  }
  loadSubMaterialDetails(subMaterialId: number) {
    this.isLoadding = true;
    this.subMaterialService.getSubMaterialById(subMaterialId)
      .subscribe((response: any) => {
        if (response.code === 1000 && response.result) {
          const subMaterial = response.result;
          this.selectedSubMaterial = {
            sub_material_id: subMaterial.sub_material_id,
            sub_material_name: subMaterial.sub_material_name,
            material_id: subMaterial.material_id,
            description: subMaterial.description,
            material_name: subMaterial.material_name,
            quantity: subMaterial.quantity,
            unit_price: subMaterial.unit_price
          };
          this.originalSubMaterial = { ...this.selectedSubMaterial };

          this.editForm.patchValue({
            sub_material_id: this.selectedSubMaterial.sub_material_id,
            sub_material_name: this.selectedSubMaterial.sub_material_name,
            material_id: this.selectedSubMaterial.material_id,
            description: this.selectedSubMaterial.description,
            material_name: this.selectedSubMaterial.material_name,
            quantity: this.selectedSubMaterial.quantity,
            unit_price: this.selectedSubMaterial.unit_price
          });

          console.log('Form Values after patchValue:', this.editForm.value);
          this.isLoadding = false;
        } else {
          console.error('Failed to load submaterial details.');
          this.isLoadding = false;
        }
      });
  }


  searchSubMaterial(): void {
    this.isLoadding = true;
    this.materialService.searchSubMaterial(this.searchKey).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          //   console.log('Kết quả tìm kiếm Sub-Materials:', this.products);
        } else {
          console.error('Failed to search sub-materials:', data);
          this.toastr.error('Không thể tìm kiếm sub-materials!', 'Lỗi');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error searching sub-materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    );


  }

  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
  }

  selectProduct(product: any): void {
    this.isLoadding = true;
    this.selectedSubMtr = product; // Adjust based on your product object structure
    // console.log('Selected mtr seacu:', this.selectedSubMtr.sub_material_name);

    this.materialService.searchSubMaterial(this.selectedSubMtr.sub_material_name).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          console.log('Kết quả tìm kiếm Sub-Materials:', this.products);
          this.isLoadding = false;
        } else {
          console.error('Failed to search sub-materials:', data);
          this.toastr.error('Không thể tìm kiếm sub-materials!', 'Lỗi');

          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error searching sub-materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    );

  }
  onChangeSearch(event: any) {
    this.selectedSubMtr = event.target.value;
    console.log('Selected submaterial:', event.target.value);
  }

  filterByMaterialId(): void {
    this.isLoadding = true;
    console.log("Thực hiện chức năng lọc theo nguyên vật liệu: ", this.selectedMaterial);
    this.subMaterialService.filterByMaterial(this.selectedMaterial).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          //    this.toastr.success('Vật liệu ' + this.selectedMaterial.materialName + ' thành công!', 'Thành công');
          console.log('Kết quả lọc Sub-Materials:', this.products);
        } else {
          console.error('Failed to filter sub-materials:', data);
          this.toastr.error('Không thể lọc sub-materials!', 'Lỗi');
          
this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error filtering sub-materials:', error);
        this.toastr.error('Có lỗi xảy ra!', 'Lỗi');
        
this.isLoadding = false;
      }
    );

  }
  impottExcel() {

  }
  searchSelectedMaterial(material: any): void {
    this.isLoadding = true;
    this.selectedMaterial = material;
    console.log("Thực hiện chức năng tìm kiếm nguyên vật liệu: ", this.selectedMaterial);
    // console.log("Thực hiện chức năng tìm kiếm nguyên vật liệu: ", this.selectedMaterial.materialName);
    if (this.selectedMaterial === null) {
      this.getAllSubMaterials();
      this.isLoadding = false;
    } else {
      this.filterByMaterialId();
      this.isLoadding = false;
    }
  }

  getDatasupplierMaterial(id: string): void {
    this.isLoadding = true;
    this.authenListService.getSupplierById(id).subscribe(
      (data) => {
        this.SubMaterData = data.result;
        this.isLoadding = false;
        // this.selectedRole = this.role.find(role => role.roleName === this.userData.role_name)?.roleId;
      },
      (error) => {
        console.error('Error fetching user data:', error);
        this.isLoadding = false;
      }
    );

  }
}