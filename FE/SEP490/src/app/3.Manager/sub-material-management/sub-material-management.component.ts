import { HttpClient, HttpEventType, HttpResponse } from '@angular/common/http';
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
  quantity: number ,
  unit_price: number,
  input_price: number
}
declare var $: any; // khai bao jquery

@Component({
  selector: 'app-sub-material-management',
  templateUrl: './sub-material-management.component.html',
  styleUrls: ['./sub-material-management.component.scss']
})

export class SubMaterialManagementComponent implements OnInit {
  subMaterials: any[] = []; // Biến để lưu trữ danh sách sub-materials
  loginToken: string | null = null;
  currentPage: number = 1;
  selectedMaterial: any = null;
  searchKey: string = '';
  categories: any[] = [];
  selectedFile: File | undefined;
  keyword = 'subMaterialName';
  subMaterialAutoComp: any[] = [];
  sub_material_name: string = '';
  SubMaterData: any = {};
  description: string = '';
  quantity: number = 0;
  unit_price: number = 0;
  input_price: number = 0;
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
    unit_price: 0,
    input_price: 0
  };
  checkNotFound: boolean = false;
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
      subMaterialId: [''],
      subMaterialName: [''],
      materialId: [''],
      description: [''],
      materialName: [''],
      quantity: [''],
      unitPrice: [''],
      input_price: ['']
    });
    this.createJobs = this.fb.group({
      sub_material_id: [''],
      sub_material_name: [''],
      material_id: [''],
      description: [''],
      material_name: [''],
      quantity: [''],
      unit_price: [''],
      input_price: ['']
    });
  }

  ngOnInit(): void {
   // console.log("Bắt đầu chạy sub-material-management.component.ts")
    this.getAllMaterials();
    this.getAllSubMaterials();
    this.getAllSubMaterialsForAutoCom();
  }

  getAllSubMaterialsForAutoCom(): void {
    this.isLoadding = true;
    this.materialService.getAllSubMaterials().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.subMaterialAutoComp = data.result;
          console.log('Danh sách Sub-Materials:', this.subMaterialAutoComp);
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch sub-materials:', data);
          this.toastr.warning('Không thể lấy danh sách sub-materials!', 'Lỗi');
          this.isLoadding = false;// Hiển thị thông báo lỗi
        }
      },
      (error) => {
        console.error('Error fetching sub-materials:', error);
        this.toastr.warning('Có lỗi xảy ra!', 'Lỗi'); // Hiển thị thông báo lỗi chung
        this.isLoadding = false;
      }
    );
  }
  getAllSubMaterials(): void {
    this.isLoadding = true;
    this.materialService.getAllSubMaterials().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.subMaterials = data.result;
          console.log('Danh sách Sub-Materials:', this.subMaterials);
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch sub-materials:', data);
          this.toastr.warning('Không thể lấy danh sách sub-materials!', 'Lỗi');
          this.isLoadding = false;// Hiển thị thông báo lỗi
        }
      },
      (error) => {
        console.error('Error fetching sub-materials:', error);
        this.toastr.warning('Có lỗi xảy ra!', 'Lỗi'); // Hiển thị thông báo lỗi chung
        this.isLoadding = false;
      }
    );
  }

  linkDowloadExcel(): void {
    
  }
  dowloadExcelLink(event: Event): void {
    this.isLoadding = true;
    event.preventDefault(); 
    this.subMaterialService.downloadExcel().subscribe(
      (response) => {
        // Assuming the response is the file data
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
        const url = window.URL.createObjectURL(blob);

        // Creating an anchor element to trigger download
        const anchor = document.createElement('a');
        anchor.href = url;
        anchor.download = 'NhapVatLieu.xlsx'; // Specify the file name here
        document.body.appendChild(anchor); // Append anchor to the body to make it clickable
        anchor.click();

        // Clean up
        document.body.removeChild(anchor);
        window.URL.revokeObjectURL(url); // Revoke the blob URL to free up resources
        this.isLoadding = false;
      },
      (error) => {
        console.error('Download error:', error);
        this.isLoadding = false;
      }
    );
  }
  getAllMaterials(): void {
    this.isLoadding = true;
    this.materialService.getAllMaterial().subscribe(
      (data) => {
        if (data.code === 1000) {
          this.categories = data.result;
   //       console.log('Danh sách Materials:', this.categories);
          this.isLoadding = false;
        } else {
          console.error('Failed to fetch materials:', data);
          this.toastr.warning('Không thể lấy danh sách materials!', 'Lỗi');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error fetching materials:', error);
        this.toastr.warning('Có lỗi xảy ra!', 'Lỗi');
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
      input_price: this.input_price,
    };
    if (Object.values(subMaterial).includes(null)) {
      this.isLoadding = false;
      this.toastr.info('Vui lòng điền đầy đủ thông tin, không được để trống!', 'Thông báo');
      return;
    }if (!subMaterial.sub_material_name || subMaterial.sub_material_name.length < 3) {
      this.isLoadding = false;
      this.toastr.info('Tên vật liệu phụ phải lớn hơn 3 ký tự!', 'Thông báo');
      return;
  }
  if (subMaterial.description === null || subMaterial.description === '') {
    this.isLoadding = false;
    this.toastr.info('Không được để trống mô tả sản phẩm', 'Thông báo');
    return;
}
  if (subMaterial.quantity <= 0) {
      this.isLoadding = false;
      this.toastr.info('Số lượng phải lớn hơn 0!', 'Thông báo');
      return;
  }
  
  if (subMaterial.unit_price <= 0) {
      this.isLoadding = false;
      this.toastr.info('Giá bán phải lớn hơn 0!', 'Thông báo');
      return;
  }
  
  if (subMaterial.input_price <= 0) {
      this.isLoadding = false;
      this.toastr.info('Giá nhập phải lớn hơn 0!', 'Thông báo');
      return;
  }
  if(subMaterial.input_price > subMaterial.unit_price){
    this.isLoadding = false;
    this.toastr.info('Giá nhập phải nhỏ hơn giá bán!', 'Lỗi');
    return;
  }
    console.log('SubMaterial request:', subMaterial);
    this.materialService.addNewSubMaterial(subMaterial).subscribe(
      (response) => {
        if (response.code === 1000) {
          this.toastr.success('Thêm nguyên vật liệu mới thành công!', 'Success');
          this.getAllSubMaterials(); // Refresh the list of sub-materials
          $('[data-dismiss="modal"]').click();
          this.isLoadding = false;
        } else {
          this.toastr.warning('Failed to add sub-material!', 'Error');
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error adding sub-material:', error);
        this.toastr.warning('Có lỗi xảy ra!', 'Lỗi');
        this.isLoadding = false;
      }
    );

  }
  saveChanges() {
    this.isLoadding = true;

    // Lấy giá trị từ form
    const formData = this.editForm.value;

    // Kiểm tra các điều kiện
    if (!formData.subMaterialName || formData.subMaterialName.length < 3) {
        this.isLoadding = false;
        this.toastr.warning('Tên vật liệu phụ phải lớn hơn 3 ký tự!', 'Thông báo');
        return;
    }

    if (formData.quantity <= 0) {
        this.isLoadding = false;
        this.toastr.warning('Số lượng phải lớn hơn 0!', 'Thông báo');
        return;
    }

    if (formData.unitPrice <= 0) {
        this.isLoadding = false;
        this.toastr.warning('Giá bán phải lớn hơn 0!', 'Thông báo');
        return;
    }

    // Gửi dữ liệu đến API
    this.subMaterialService.editSubMaterial(formData.subMaterialId, formData).subscribe(
        (data) => {
            if (data.code === 1000) {
                this.toastr.success('Cập nhật nguyên vật liệu thành công!', 'Thành công');
                this.getAllSubMaterials();
                $('[data-dismiss="modal"]').click();
                this.isLoadding = false;
            } else {
                console.error('Failed to search sub-materials:', data);
                this.toastr.warning('Không thể cập nhật nguyên vật liệu!', 'Thông báo');
                this.isLoadding = false;
            }
        },
        (error) => {
            console.error('Error updating sub-materials:', error);
            this.toastr.warning('Có lỗi xảy ra!', 'Lỗi');
            this.isLoadding = false;
        }
    );
}

  loadSubMaterialDetails(subMaterialId: number) {
    this.isLoadding = true;
    this.subMaterialService.getSubMaterialById(subMaterialId)
      .subscribe((response: any) => {
        if (response.code === 1000 && response.result) {
          const subMaterial = response.result;
          console.log('Sub-Material details:', subMaterial);
          this.selectedSubMaterial = {
            sub_material_id: subMaterial.subMaterialId,
            sub_material_name: subMaterial.subMaterialName,
            material_id: subMaterial.materialId,
            description: subMaterial.description,
            material_name: subMaterial.materialName,
            quantity: subMaterial.quantity,
            unit_price: subMaterial.unitPrice,
            input_price: subMaterial.input_price
          };
          this.originalSubMaterial = { ...this.selectedSubMaterial };

          this.editForm.patchValue({
            subMaterialId: this.selectedSubMaterial.sub_material_id,
            subMaterialName: this.selectedSubMaterial.sub_material_name,
            materialId: this.selectedSubMaterial.material_id,
            description: this.selectedSubMaterial.description,
            materialName: this.selectedSubMaterial.material_name,
            quantity: this.selectedSubMaterial.quantity,
            unitPrice: this.selectedSubMaterial.unit_price,
            input_price: this.selectedSubMaterial.input_price
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
    this.checkNotFound = false;
    
    this.searchKey.trim();
   
    this.multiFilterSubmaterial(this.searchKey,this.selectedMaterial);
    

  }
 
  sanitize(name: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(name);
  }

  onFileSelectedAndUpload(event: Event) {
    this.isLoadding = true;
    const inputElement = event.target as HTMLInputElement;
    if (inputElement.files && inputElement.files.length > 0) {
      this.selectedFile = inputElement.files[0];
      
      // Kiểm tra loại file
      const fileExtension = this.selectedFile.name.split('.').pop()?.toLowerCase();
      const allowedExtensions = ['xls', 'xlsx'];
      if (!fileExtension || !allowedExtensions.includes(fileExtension)) {
        console.error('Invalid file type. Please upload an Excel file.');
        this.toastr.info('Loại file không hợp lệ. Vui lòng tải lên file Excel.', 'Tải file không thành công!');
        this.selectedFile = undefined;
        this.isLoadding = false;
        return;
      }
  
      // Kiểm tra kiểu MIME (optional)
      const allowedMimeTypes = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'];
      if (!allowedMimeTypes.includes(this.selectedFile.type)) {
        console.error('Invalid file type. Please upload an Excel file.');
        this.selectedFile = undefined;
        this.toastr.info('Loại file không hợp lệ. Vui lòng tải lên file Excel.', 'Lỗi');
        this.isLoadding = false;
        return;
      }
  
      this.uploadFile(this.selectedFile, inputElement);
    } else {
      this.selectedFile = undefined;
      console.error('No file selected.');
      this.isLoadding = false;
    }
  }
  
  uploadFile(file: File, inputElement: HTMLInputElement) {
    this.isLoadding = true;
    this.subMaterialService.uploadExcel(file).subscribe(
      (event: any) => {
        if (event.type === HttpEventType.UploadProgress) {
          const progress = Math.round((100 * event.loaded) / event.total);
      //    console.log(`File is ${progress}% uploaded.`);
        } else if (event instanceof HttpResponse) {
          if (event.body.result == null || event.body.result.length === 0) {
            this.toastr.success('Cập nhật nguyên vật liệu thành công!', 'Thành công');
          } else {
            const errors = event.body.result.map((error: any) => 
              `Hàng: ${error.row} - Cột: ${error.column}, Lỗi: ${error.errorMessage}`
            ).join('<br>');
            this.toastr.warning(
              `<div">${errors}</div>`, 
              'Lỗi', 
              { enableHtml: true }
            );
          }
        
          this.getAllSubMaterials();
          this.resetFileInput(inputElement); // Reset file input
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Upload error:', error);
        this.toastr.warning('Tải lên thất bại!', 'Thông báo');
        this.isLoadding = false;
        this.getAllMaterials();
        this.resetFileInput(inputElement); // Reset file input
      }
    );
  }
  
resetFileInput(inputElement: HTMLInputElement) {
  inputElement.value = ''; // Reset the file input
  this.selectedFile = undefined; // Optionally reset the selected file in the component
}
  selectProduct(product: any): void {
   
    this.selectedSubMtr = product; // Adjust based on your product object structure
     console.log('Selected mtr seacu:', this.selectedSubMtr);

   this.multiFilterSubmaterial(this.selectedSubMtr.subMaterialName,this.selectedSubMtr.materialId);

  }
  onChangeSearch(event: any) {
    this.selectedSubMtr = event.target.value;
    console.log('Selected submaterial:', event.target.value);
  }

  filterByMaterialId(): void {
   // this.isLoadding = true;
    console.log("selected mater id: ", this.selectedMaterial);
    this.multiFilterSubmaterial(this.searchKey,this.selectedMaterial);
  }

  
  multiFilterSubmaterial(search: string, materialId: any): void {
    this.isLoadding = true;
    console.log("Thực hiện chức năng lọc sub-materials theo nguyên vật liệu: ", materialId);
    if(materialId === null){
      materialId = ''
    }
    this.subMaterialService.multiFilterSubmaterial(search,materialId).subscribe(
      (data) => {
        if (data.code === 1000) {
          this.subMaterials = data.result;
          this.currentPage = 1;
          //    this.toastr.success('Vật liệu ' + this.selectedMaterial.materialName + ' thành công!', 'Thành công');
          console.log('Kết quả lọc Sub-Materials:', this.subMaterials);
          this.isLoadding = false;
        } else {
          console.error('Failed to filter sub-materials:', data);
          //this.toastr.error('Không thể lọc sub-materials!', 'Lỗi');
          this.checkNotFound = true;
          this.isLoadding = false;
        }
      },
      (error) => {
        console.error('Error filtering sub-materials:', error);
        this.toastr.warning('Có lỗi xảy ra!', 'Lỗi');

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