

import { Component, OnInit } from '@angular/core';
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


  constructor(  private provincesService: ProvincesService,
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
        city: ['', Validators.required],
        district: ['', Validators.required],
        wards: ['', Validators.required]
      });
    }

  ngOnInit(): void {
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
          this.toastr.success('Registration successful. Check your email for OTP.');
          this.router.navigate(['/user_management']);
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
      city: user.city,
      district: user.district,
      wards: user.wards
    });

    this.editUserForm.get('city')?.setValue(user.city); // Set initial values for dropdowns
    this.editUserForm.get('district')?.setValue(user.district);
    this.editUserForm.get('wards')?.setValue(user.wards);

    // Enable/disable position dropdown based on selected role
    this.isPositionDisabled = user.role !== 2; // Assuming 2 is the roleId for 'Employee'
  }
  onRoleChange() {
    const selectedRole = this.editUserForm.get('role')?.value;
    this.isPositionDisabled = selectedRole !== 2; // Assuming 2 is the roleId for 'Employee'
    if (this.isPositionDisabled) {
      this.editUserForm.get('position')?.reset();
    }
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