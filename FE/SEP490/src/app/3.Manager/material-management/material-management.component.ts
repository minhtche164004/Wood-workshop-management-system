import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { SupplierService } from 'src/app/service/supplier.service';
import { ToastrService } from 'ngx-toastr';
import { MaterialService } from 'src/app/service/material.service';
import { TypeMaterialService } from 'src/app/service/type-material.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthenListService } from 'src/app/service/authen.service';
import { timer } from 'rxjs';
interface Material {

  materialName: string;
  type: string;
}
interface EditMaterial {
  materialName: string;
  type: string;
}
@Component({
  selector: 'app-material-management',
  templateUrl: './material-management.component.html',
  styleUrls: ['./material-management.component.scss']
})
export class MaterialManagementComponent implements OnInit {
  @ViewChild('closeButton', { static: false }) closeButton!: ElementRef<any>;
  ngAfterViewInit() {
    if (this.closeButton) {
      this.closeButton.nativeElement.click();
    }
  }
  isLoadding: boolean = false;
  selectedMaterialName: any = null;
  material: any[] = [];
  products: any[] = [];
  currentPage: number = 1;
  MaterialData: any = {};
  materialName: string = '';
  type: string = '';
  materials: any[] = []; // Array to store supplier data
  deleteId: any; // variable to store supplierId to be deleted
  editMaterialForm: FormGroup;
  ngOnInit(): void {
    this.getAllMaterials();
    this.MaterialData = {
      materialId: '',
      materialName: '',
      type: '',

    };
    this.loadMaterial();
  }
  constructor(private authenListService: AuthenListService
    , private typeMaterialService: TypeMaterialService, private toastr: ToastrService, private fb: FormBuilder) {
    this.editMaterialForm = this.fb.group({
      materialName: ['', Validators.required],
      type: ['', [Validators.required]],

    });
  }

  loadMaterial(): void {
    this.isLoadding = true;
    this.authenListService.getAllMaterialName().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.material = data.result;
          this.isLoadding = false;

        } else {
          console.error('Invalid data returned:', data);
          this.isLoadding = false;

        }
      },
      (error) => {
        console.error('Error fetching positions:', error);
        this.isLoadding = false;

      }
    );
  }
  getAllMaterials(): void {
    this.isLoadding = true;
    this.typeMaterialService.getAllTypeMateriall().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.products = data.result;
          this.isLoadding = false;
          console.log('Danh sách Type-Materials:', this.products);
        } else {
          console.error('Failed to fetch type-materials:', data);
          this.isLoadding = false;

          this.toastr.error('Không thể lấy danh sách type-materials!', 'Lỗi');
        }
      },
      (error) => {
        console.error('Error fetching materials:', error);
        this.isLoadding = false;

        this.toastr.error('Có lỗi xảy ra!', 'Lỗi'); // Hiển thị thông báo lỗi chung
      }
    )
  }
  addMaterial(): void {
    this.isLoadding = true;

    const material: Material = {
      materialName: this.materialName,
      type: this.type,


    };
    console.log('Supplier Request:', material);
    this.authenListService.addNewMaterial(material)
      .subscribe(
        (data) => {
          if (data.code === 1000) {
            this.toastr.success('Nhà cung cấp đã được thêm thành công!', 'Thành công');
            this.isLoadding = false;
            timer(200).subscribe(() => {
              window.location.reload();
            });
            this.materials.push(material);
            this.materialName = '';
            this.type = '';
          } else {
            console.error('Failed to add material:', data);
            this.isLoadding = false;

            this.toastr.error('Có lỗi khi thêm nhà cung cấp!', 'Lỗi');
          }
        },
        (error) => {
          console.error('Error adding material:', error);
          this.isLoadding = false;

          this.toastr.error('Có lỗi khi thêm nhà cung cấp!', 'Lỗi');
        }
      );


  }
  getMaterById(id: string): void {
    this.isLoadding = true;

    this.authenListService.getMaterialById(id).subscribe(
      (data) => {
        this.MaterialData = data.result;
        this.isLoadding = false;

        // this.selectedRole = this.role.find(role => role.roleName === this.userData.role_name)?.roleId;
      },
      (error) => {
        this.isLoadding = false;

        console.error('Error fetching user data:', error);
      }
    );

  }
  EditMaterial(): void {
    this.isLoadding = true;

    const editMaterial: EditMaterial = this.editMaterialForm.value;
    const material_Id = this.MaterialData.materialId; // Lấy userId từ userData
    console.log("Data: ", editMaterial)
    console.log("Data: ", material_Id)
    this.authenListService.editMaterial(material_Id, editMaterial).subscribe(
      () => {

        this.toastr.success('Thay đổi thông tin thành công.');
        this.ngAfterViewInit();
        this.isLoadding = false;

        setTimeout(() => {
          window.location.reload();
        }, 2000); // Delay 1 second before reload
      },
      (error: any) => {
        this.isLoadding = false;

        // if ( error.error.code === 1033) {
        //   this.toastr.error('Không thể thay đổi quyền của nhân viên này vì họ đang đảm nhận công việc ở vị trí của họ',);
        // }

      }
    );
  }
}
