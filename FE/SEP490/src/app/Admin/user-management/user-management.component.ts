

import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ProvincesService } from 'src/app/service/provinces.service';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/app/environments/environment'; // Ensure this is the correct path
import { Router } from '@angular/router';
import { AuthenListService } from 'src/app/service/authen.service';

interface AddNewAccount {
  username: string;
  password: string;
  checkPass: string;
  email: string;
  phoneNumber: string;
  address: string;
  fullname: string;
  status: number;
  position: number;
  role: number;
  bank_name: string,
  bank_number: string,
  city: string;
  district: string;
  wards: string;
}
interface EditUserRequest {
  userId: number;
  username: string;
  email: string;
  phoneNumber: string;
  address: string;
  fullname: string;
  status_name: string;
  position_name: string;
  bank_name: string;
  role_name: string;
  bank_number: string;
  city: string;
  district: string;
  wards: string;
}
interface Role {
  roleId: number;
  roleName: string;
}

interface ApiResponse {
  code: number;
  result: any[];
}

interface Province {
  code: string;
  name: string;
  districts: District[];
}

interface District {
  code: string;
  name: string;
  wards: Ward[];
}

interface Ward {
  code: string;
  name: string;
}
@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {






  @ViewChild('closeButton') closeButton: ElementRef | undefined;
  private apiUrl_AddNewAccount = `${environment.apiUrl}api/auth/admin/AddNewAccount`; // URL của backend
  private apiUrl_EditUser = `${environment.apiUrl}api/auth/admin/EditUser`;
  private apiUrl_GetUserById = `${environment.apiUrl}api/auth/admin/GetUserById`;
  addAccountForm: FormGroup;
  editUserForm: FormGroup;
  provinces: Province[] = [];
  districts: District[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wards: Ward[] = [];
  role: Role[] = [];
  position: any[] = [];
  isPositionDisabled: boolean = true;
  user: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  userId: number = 1;
  selectedCategory: any = null;
  selectedRoleAdd: any = null; 
  selectedRole: any = null; // Assuming selectedRole should be a boolean
  selectedPosition: any = null;
  isPositionEnabled: boolean = false;

  selectProvince: any = null;
  selectDistricts: any = null;
  selectWards: any = null;
  selectedUser: any;
  userData: any = {};

  selectedProvince: any;
  selectedDistrict: any;

  constructor(private provincesService: ProvincesService,
    private productListService: ProductListService,
    private authenListService: AuthenListService,
    private fb: FormBuilder,
    private http: HttpClient,
    private toastr: ToastrService,
    private router: Router) {
    this.addAccountForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      checkPass: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      address: ['', Validators.required],
      fullname: ['', Validators.required],
      status: [0],
      position: ['', Validators.required],
      role: ['', Validators.required],
      bank_name: ['', Validators.required],
      bank_number: ['', Validators.required],
      city: ['', Validators.required],
      district: ['', Validators.required],
      wards: ['', Validators.required]
    });
    this.editUserForm = this.fb.group({
      user_id: [this.userId],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      address: ['', Validators.required],
      fullname: ['', Validators.required],
      status: [0],
      position: ['', Validators.required],
      role: ['', Validators.required],
      bank_name: ['', Validators.required],
      bank_number: ['', Validators.required],
      city: ['', Validators.required],
      district: ['', Validators.required],
      wards: ['', Validators.required]
    });
  }

  isUserDataLoaded: boolean = false;

  ngOnInit(): void {
    this.userData = {
      username: '',
      email: '',
      phoneNumber: '',
      address: '',
      fullname: '',
      status_name: '',
      position_name: '',
      role_name: '',
      bank_name: '',
      bank_number: '',
      city_province: '',
      district: '',
      wards: '',
    };
    this.role = []; // Initialize with your roles data
    this.position = []; // Initialize with your positions data
    this.loginToken = localStorage.getItem('loginToken');
    this.loadAllRole();
    this.loadProvinces();
    this.loadPosition();

    if (this.loginToken) {
      console.log('Retrieved loginToken:', this.loginToken);
      this.productListService.getAllUser().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.user = data.result;
            console.log('Danh sách người dùng:', this.user);
          } else {
            console.error('Failed to fetch products:', data);
          }
        },
        (error) => {
          console.error('Error fetching products:', error);
        }
      );
    } else {
      console.error('No loginToken found in localStorage.');
    }
    this.addAccountForm.get('city')?.valueChanges.subscribe(provinceName => {
      const selectedProvince = this.provinces.find(province => province.name === provinceName);
      this.districts = selectedProvince ? selectedProvince.districts : [];
      this.addAccountForm.get('district')?.reset();
      this.addAccountForm.get('wards')?.reset();
    });

    this.addAccountForm.get('district')?.valueChanges.subscribe(districtName => {
      const selectedDistrict = this.districts.find(district => district.name === districtName);
      this.wards = selectedDistrict ? selectedDistrict.wards : [];
      this.addAccountForm.get('wards')?.reset();
    });

    // khi load trang cai nay` no ghi de` vao` gia tri user nen bi loi~
    this.editUserForm.get('city')?.valueChanges.subscribe(provinceName => {
      const selectedProvince = this.provinces.find(province => province.name === provinceName);
      this.districts = selectedProvince ? selectedProvince.districts : [];
      if (!selectedProvince || this.editUserForm.get('district')?.value) {
        this.editUserForm.get('district')?.reset();
        this.editUserForm.get('wards')?.reset();
      }
    });
    
    this.editUserForm.get('district')?.valueChanges.subscribe(districtName => {
      const selectedDistrict = this.districts.find(district => district.name === districtName);
      this.wards = selectedDistrict ? selectedDistrict.wards : [];
      if (!selectedDistrict || this.editUserForm.get('wards')?.value) {
        this.editUserForm.get('wards')?.reset();
      }
    });
  }

  loadAllRole(): void {
    this.productListService.getAllRole().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.role = data.result as Role[];
        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching roles:', error);
      }
    );
  }
  loadAllRoleEmployee(): void {
    this.productListService.getAllRole().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.role = data.result as Role[];

          // Filter roles based on roleId == 4
          this.role = this.role.filter(role => role.roleId === 4);

          // Now this.role will contain only roles where roleId == 4
          console.log('Filtered roles:', this.role);
        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching roles:', error);
      }
    );
  }
  onRoleChange() {
    if (this.selectedRoleAdd !== 4) {
      this.isPositionEnabled = false;


    } else {
      this.isPositionEnabled = true;


    }
  }
  onRoleChangeUpdate() {
    if (this.userData.role_name !== 'EMPLOYEE' && this.selectedRoleAdd !== 4) {
      this.isPositionEnabled = false;
    } else {
      this.isPositionEnabled = true;

    }
  }


  loadPosition(): void {
    this.productListService.getAllPosition().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.position = data.result;
        } else {
          console.error('Invalid data returned:', data);
        }
      },
      (error) => {
        console.error('Error fetching positions:', error);
      }
    );
  }

  loadProvinces(): void {
    this.provincesService.getProvinces().subscribe((data: Province[]) => {
      this.provinces = data;
    });
  }

  loadAllUsers(): void {
    this.productListService.getAllUser().subscribe(
      (data: ApiResponse) => {
        if (data.code === 1000) {
          // Handle users data here
        } else {
          console.error('Failed to fetch users:', data);
        }
      },
      (error) => {
        console.error('Error fetching users:', error);
      }
    );
  }

  AddNewAccount(): void {

    const addNewAccountRequest: AddNewAccount = this.addAccountForm.value;
    console.log('Request Data:', addNewAccountRequest);
    this.http.post<any>(this.apiUrl_AddNewAccount, addNewAccountRequest, { withCredentials: true })
      .subscribe(
        () => {
          this.toastr.success('Thêm tài khoản người dùng thành công.');
          if (this.closeButton) {
            this.closeButton.nativeElement.click(); // Gọi hành động đóng modal
          }
          this.addAccountForm.reset(); // Reset the form after successful addition
        },
        (error: any) => {
          console.error('Registration failed', error);
          this.toastr.error('Registration failed. Please try again.');
        }
      );
  }
  populateForm(user: any): void {
    this.editUserForm.patchValue({
      username: user.username,
      email: user.email,
      phoneNumber: user.phoneNumber,
      address: user.address,
      fullname: user.fullname,
      status: user.status,
      position: user.position,
      role: user.role,
      bank_name: user.bank_name,
      bank_number: user.bank_number,

      city: user.city_province,
      district: user.district,
      wards: user.wards
    });

    // this.editUserForm.get('city')?.setValue(user.city_province); // Set initial values for dropdowns
    // this.editUserForm.get('district')?.setValue(user.district);
    // this.editUserForm.get('wards')?.setValue(user.wards);

  }

  getUserData(user_id: string): void {
    this.authenListService.getUserById(user_id).subscribe(
      (data) => {
        this.userData = data.result;
        console.log("User data:" ,data)
      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    );

 
  }
 

  onProvinceChange() {
    const selectedProvinceName = this.userData.city_province; // assuming 'city' is bound to ngModel of the province dropdown
    this.selectedProvince = this.provinces.find(province => province.name === selectedProvinceName);

    // Update districts based on the selected province
    this.districts = this.selectedProvince ? this.selectedProvince.districts : [];

    // Reset selected district and ward
    this.userData.district = ''; // reset selected district in the model
    this.userData.wards = ''; // reset selected ward in the model
  }

  onDistrictChange() {
    const selectedDistrictName = this.userData.district; // assuming 'city' is bound to ngModel of the province dropdown
    this.selectedDistrict = this.districts.find(district => district.name === selectedDistrictName);

    // Update districts based on the selected province
    this.wards = this.selectedDistrict ? this.selectedDistrict.wards : [];

    this.userData.wards = ''; // reset selected ward in the model
  }
  EditUser(): void {


    const editUserRequest: EditUserRequest = this.editUserForm.value;
    const userId = this.userData.userId; // Lấy userId từ userData

    this.authenListService.editUserById(userId, editUserRequest).subscribe(
      () => {
        this.toastr.success('User updated successfully.');
        this.router.navigate(['/user_management']); // Navigate to users list or profile page
      },
      (error: any) => {
        console.error('User update failed', error);
        this.toastr.error('User update failed. Please try again.');
      }
    );
  }


}