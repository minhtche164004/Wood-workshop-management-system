

import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ProvincesService } from 'src/app/service/provinces.service';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/app/environments/environment'; // Ensure this is the correct path
import { Router } from '@angular/router';

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
  user_id: number;
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
  addAccountForm: FormGroup;
  editUserForm: FormGroup;
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  role: Role[] = [];
  position: any[] = [];
  isPositionDisabled: boolean = true;
  user: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  userId: number = 1;
  selectedCategory: any = null;
  selectedRole: any = null; // Assuming selectedRole should be a boolean

  selectedPosition: any = null;

  isPositionEnabled: boolean = false;
  constructor(private provincesService: ProvincesService,
    private productListService: ProductListService,
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
  }

  ngOnInit(): void {
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
    this.editUserForm.get('city')?.valueChanges.subscribe(provinceName => {
      const selectedProvince = this.provinces.find(province => province.name === provinceName);
      this.districts = selectedProvince ? selectedProvince.districts : [];
      this.editUserForm.get('district')?.reset();
      this.editUserForm.get('wards')?.reset();
    });

    this.editUserForm.get('district')?.valueChanges.subscribe(districtName => {
      const selectedDistrict = this.districts.find(district => district.name === districtName);
      this.wards = selectedDistrict ? selectedDistrict.wards : [];
      this.editUserForm.get('wards')?.reset();
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
    // Implement your logic when role selection changes
    // Example condition:
    if (this.selectedRole != 4) {
      this.isPositionEnabled = true;
      this.position = []; // Update with positions relevant to selected role
    } else {
      this.isPositionEnabled = false;
      this.selectedPosition = null; // Reset selected position if needed
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
  loadUserData(): void {
    // Replace with your service method to fetch user data by user_id
    this.http.get<ApiResponse>(`http://localhost:8080/api/auth/admin/GetUserById?user_id=${this.userId}`)
      .subscribe(
        (userData) => {
          if (userData.code === 1000) {
            const user = userData.result; // Assuming user data is fetched properly
            this.populateForm(user);
          } else {
            console.error('Failed to fetch user data:', userData);
          }
        },
        (error) => {
          console.error('Error fetching user data:', error);
        }
      );
  }
  AddNewAccount(): void {
    if (this.addAccountForm.invalid) {
      this.toastr.error('Please fill all required fields correctly.');
      return;
    }

    const addNewAccountRequest: AddNewAccount = this.addAccountForm.value;

    this.http.post<any>(this.apiUrl_AddNewAccount, addNewAccountRequest, { withCredentials: true })
      .subscribe(
        () => {
          this.toastr.success('Thêm tài khoản người dùng thành công.');
          if (this.closeButton) {
            this.closeButton.nativeElement.click(); // Gọi hành động đóng modal
          }
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

      city: user.city,
      district: user.district,
      wards: user.wards
    });

    this.editUserForm.get('city')?.setValue(user.city); // Set initial values for dropdowns
    this.editUserForm.get('district')?.setValue(user.district);
    this.editUserForm.get('wards')?.setValue(user.wards);



  }




  EditUser(): void {
    if (this.editUserForm.invalid) {
      this.toastr.error('Please fill all required fields correctly.');
      return;
    }

    const editUserRequest: EditUserRequest = this.editUserForm.value;

    this.http.post<any>(`${this.apiUrl_EditUser}?user_id=${editUserRequest.user_id}`, editUserRequest)
      .subscribe(
        () => {
          this.toastr.success('User updated successfully.');
          this.router.navigate(['/users']); // Navigate to users list or profile page
        },
        (error: any) => {
          console.error('User update failed', error);
          this.toastr.error('User update failed. Please try again.');
        }
      );
  }
}