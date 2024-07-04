

import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ProvincesService } from 'src/app/service/provinces.service';
import { ProductListService } from 'src/app/service/product/product-list.service';
import { FormControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { environment } from 'src/app/environments/environment'; // Ensure this is the correct path
import { Router } from '@angular/router';
import { AuthenListService } from 'src/app/service/authen.service';

export interface AddNewAccount {
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
interface BankName {
  id: string;
  shortName: string;
  logo: string;
}
interface EditUserRequest {
  userId: number;
  username: string;
  email: string;
  address: string;
  fullname: string;
  phoneNumber: string;
  status_id: number;
  position_id: number;
  role_id: number;
  bank_name: string;
  bank_number: string;
  city_province: string;
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

interface ApiResponse1 {
  code: string;
  message?: string;
  data: BankName[];
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
  @ViewChild('closeButton', { static: false }) closeButton!: ElementRef<any>;

  ngAfterViewInit() {
    if (this.closeButton) {
      this.closeButton.nativeElement.click();
    }
  }
  searchKey: string = '';
  addAccountForm: FormGroup;
  editUserForm: FormGroup;
  provinces: Province[] = [];
  districts: District[] = [];
  bankname: BankName[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  wards: Ward[] = [];
  role: Role[] = [];
  position: any[] = [];
  status: any[] = [];
  banks: any[] = [];
  isPositionDisabled: boolean = true;
  user: any[] = [];
  loginToken: string | null = null;
  currentPage: number = 1;
  userId: number = 1;
  selectedCategory: any = null;
  selectedRoleAdd: any = null;
  selectedRole: any = null; // Assuming selectedRole should be a boolean
  selectedPosition: any = null;
  selectedStatus: any = null;

  selectBankName: any = null;
  isPositionEnabled: boolean = false;
  isPositionEnabled_Update: boolean = false;
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
      username: [''],
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
      status_id: [],
      position_id: ['', Validators.required],
      role_id: ['', Validators.required],
      bank_name: ['', Validators.required],
      bank_number: ['', Validators.required],
      city_province: ['', Validators.required],
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
    this.loadStatus();
    this.loadAllBankName();
    if (this.loginToken) {

      this.productListService.getAllUser().subscribe(
        (data: ApiResponse) => {
          if (data.code === 1000) {
            this.user = data.result;

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
    this.editUserForm.get('city_province')?.valueChanges.subscribe(provinceName => {
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
    this.editUserForm.get('role_id')?.valueChanges.subscribe((roleId) => {
      this.selectedRole = roleId;
      this.onRoleChangeUpdate();
    });
    this.onRoleChangeUpdate(); // gọi hàm này khi form vừa được khởi tạo
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
  loadStatus(): void {
    this.productListService.getAllStatus().subscribe(
      (data: any) => {
        if (data.code === 1000) {
          this.status = data.result;

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
  loadAllBankName(): void {
    this.authenListService.getNameATM().subscribe(
      (response: ApiResponse1) => {
        if (response.code === "00") {
          this.bankname = response.data;

        } else {
          console.error('Failed to fetch banks:', response);
        }
      },
      (error) => {
        console.error('Error fetching banks:', error);
      }
    );
  }
  getUserData(user_id: string): void {
    this.authenListService.getUserById(user_id).subscribe(
      (data) => {
        this.userData = data.result;
        this.selectedRole = this.role.find(role => role.roleName === this.userData.role_name)?.roleId;
        this.selectedPosition = this.position.find(position => position.position_name === this.userData.position_name)?.position_id;
        this.selectedStatus = this.status.find(sa => sa.status_name === this.userData.status_name)?.status_id;
        this.selectBankName = this.userData.bank_name;

      },
      (error) => {
        console.error('Error fetching user data:', error);
      }
    );

  }
  onRoleChange() {
    if (this.selectedRoleAdd !== 4) {
      this.isPositionEnabled = false;
      this.addAccountForm.patchValue({
        position: null,
        bank_name: null,
        bank_number: ''
      });
    } else {
      this.isPositionEnabled = true;
    }
  }
  onRoleChangeUpdate() {
    if (this.selectedRole === 4) {
      this.isPositionEnabled_Update = true;
      // Enable position and bank fields
    } else {
      this.isPositionEnabled_Update = false;
      this.editUserForm.patchValue({
        position: null,
        bank_name: null,
        bank_number: ''
      });

    }
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
  validateUsername(username: string): boolean {
    const usernameRegex = /^[a-zA-Z0-9]{3,20}$/;
    return usernameRegex.test(username);
  }

  validatePassword(password: string): boolean {
    const passwordRegex = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{6,20}$/;
    return passwordRegex.test(password);
  }

  validateEmail(email: string): boolean {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  }
  
  validatePhoneNumber(phoneNumber: string): boolean {
    const phoneNumberRegex = /^[0-9]{10,12}$/;
    return phoneNumberRegex.test(phoneNumber);
  }

  validateRegistration(): boolean {
    if (this.addAccountForm.controls['username'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Tên đăng nhập');
      return false;
    }

    if (this.addAccountForm.controls['password'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Mật Khẩu');
      return false;
    }

    if (this.addAccountForm.controls['checkPass'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Xác nhận mật khẩu');
      return false;
    }

    if (this.addAccountForm.controls['email'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Email');
      return false;
    }

    if (this.addAccountForm.controls['phoneNumber'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Số điện thoại');
      return false;
    }

    if (this.addAccountForm.controls['address'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Địa chỉ cụ thể');
      return false;
    }

    if (this.addAccountForm.controls['fullname'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Họ Và Tên');
      return false;
    }

    // const positionValue = this.addAccountForm.controls['position'].value;
    // if (!positionValue || positionValue.toString().trim() === "") {
    //   this.toastr.error('Không được bỏ trống trường Vai Trò');
    //   return false;
    // }

    // const roleValue = this.addAccountForm.controls['role'].value;
    // if (!roleValue || roleValue.toString().trim() === "") {
    //   this.toastr.error('Không được bỏ trống trường Vị Trí');
    //   return false;
    // }

    // const BankNameValue = this.addAccountForm.controls['bank_name'].value;
    // if (!BankNameValue || BankNameValue.toString().trim() === "") {
    //   this.toastr.error('Không được bỏ trống trường Tên Ngân Hàng');
    //   return false;
    // }

    const CityValue = this.addAccountForm.controls['city'].value;
    if (!CityValue || CityValue.toString().trim() === "") {
      this.toastr.error('Không được bỏ trống trường Tỉnh / Thành Phố');
      return false;
    }
    const District = this.addAccountForm.controls['district'].value;
    if (!District || District.toString().trim() === "") {
      this.toastr.error('Không được bỏ trống trường Quận / Huyện ');
      return false;
    }
    const Ward = this.addAccountForm.controls['wards'].value;
    if (!Ward || Ward.toString().trim() === "") {
      this.toastr.error('Không được bỏ trống trường Phường / Xã  ');
      return false;
    }
    
    
    const email = this.addAccountForm.controls['email'].value;
    const phoneNumber = this.addAccountForm.controls['phoneNumber'].value;
    const username = this.addAccountForm.controls['username'].value;
    if (this.addAccountForm.controls['password'].value !== this.addAccountForm.controls['checkPass'].value) {
      this.toastr.error('Mật khẩu xác nhận không khớp.', 'Lỗi');
      return false;
    }
    if (!this.validateUsername(username)) {
      this.toastr.error('Tên đăng nhập không hợp lệ.', 'Lỗi');
      return false;
    }
    if (!this.validateEmail(email)) {
      this.toastr.error('Email không hợp lệ.', 'Lỗi');
      return false;
    }

    if (!this.validatePhoneNumber(phoneNumber)) {
      this.toastr.error('Số điện thoại không hợp lệ.', 'Lỗi');
      return false;
    }
    return true;
  }
  validateEditUser(): boolean {
    if (this.editUserForm.controls['username'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Tên đăng nhập');
      return false;
    }

    if (this.editUserForm.controls['email'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Email');
      return false;
    }

    if (this.editUserForm.controls['phoneNumber'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Số điện thoại');
      return false;
    }

    if (this.editUserForm.controls['address'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Địa chỉ cụ thể');
      return false;
    }

    if (this.editUserForm.controls['fullname'].value.trim() === "") {
      this.toastr.error('Không được bỏ trống trường Họ Và Tên');
      return false;
    }

    // const positionValue = this.editUserForm.controls['position_id'].value;
    // if (!positionValue || positionValue.toString().trim() === "") {
    //   this.toastr.error('Không được bỏ trống trường Vai Trò');
    //   return false;
    // }

    // const roleValue = this.editUserForm.controls['role_id'].value;
    // if (!roleValue || roleValue.toString().trim() === "") {
    //   this.toastr.error('Không được bỏ trống trường Vị Trí');
    //   return false;
    // }

    // const BankNameValue = this.editUserForm.controls['bank_name'].value;
    // if (!BankNameValue || BankNameValue.toString().trim() === "") {
    //   this.toastr.error('Không được bỏ trống trường Tên Ngân Hàng');
    //   return false;
    // }

    const CityValue = this.editUserForm.controls['city_province'].value;
    if (!CityValue || CityValue.toString().trim() === "") {
      this.toastr.error('Không được bỏ trống trường Tỉnh / Thành Phố');
      return false;
    }
    const District = this.editUserForm.controls['district'].value;
    if (!District || District.toString().trim() === "") {
      this.toastr.error('Không được bỏ trống trường Quận / Huyện ');
      return false;
    }
    const Ward = this.editUserForm.controls['wards'].value;
    if (!Ward || Ward.toString().trim() === "") {
      this.toastr.error('Không được bỏ trống trường Phường / Xã  ');
      return false;
    }
    
    
    const email = this.editUserForm.controls['email'].value;
    const phoneNumber = this.editUserForm.controls['phoneNumber'].value;
    const username = this.editUserForm.controls['username'].value;
   
    if (!this.validateUsername(username)) {
      this.toastr.error('Tên đăng nhập không hợp lệ.', 'Lỗi');
      return false;
    }
    if (!this.validateEmail(email)) {
      this.toastr.error('Email không hợp lệ.', 'Lỗi');
      return false;
    }

    if (!this.validatePhoneNumber(phoneNumber)) {
      this.toastr.error('Số điện thoại không hợp lệ.', 'Lỗi');
      return false;
    }
    return true;
  }

  AddNewAccount(): void {
    if (!this.validateRegistration()) {
      return;
    }
    const addNewAccountRequest: AddNewAccount = this.addAccountForm.value;
    console.log('Request Data:', addNewAccountRequest);

    this.authenListService.AddNewAccountForAdmin(addNewAccountRequest).subscribe(
      () => {
        this.toastr.success('Thêm tài khoản người dùng thành công.');
        this.addAccountForm.reset(); // Reset the form after successful addition
        setTimeout(() => {
          window.location.reload();
        }, 1000); // Delay 1 second before reload
      },
      (error: any) => {
        console.error('Lỗi khi đăng nhập', error);
        if ( error.error.code === 1019) {
          this.toastr.error('Tên đăng nhập đã tồn tại',);
        }
        else if ( error.error.code === 1001) {
          this.toastr.error('Email đã tồn tại',);
        }
      }
    );
  }
  EditUser(): void {
    if (!this.validateEditUser()) {
      return;
    }
    const editUserRequest: EditUserRequest = this.editUserForm.value;
    const userId = this.userData.userId; // Lấy userId từ userData
    console.log("Data: ", editUserRequest)
    this.authenListService.editUserById(userId, editUserRequest).subscribe(
      () => {

        this.toastr.success('Thay đổi thông tin thành công.');
        this.ngAfterViewInit();
        setTimeout(() => {
          window.location.reload();
        }, 2000); // Delay 1 second before reload
      },
      (error: any) => {
        
        // if ( error.error.code === 1033) {
        //   this.toastr.error('Không thể thay đổi quyền của nhân viên này vì họ đang đảm nhận công việc ở vị trí của họ',);
        // }
       
      }
    );
  }
  
  SearchUserByNameorAddress(): void {
    console.log("Thực hiện tìm kiếm sản phẩm: ", this.searchKey);

    if (this.searchKey.trim() !== "") {
      this.authenListService.findSearchUserByNameorAddress(this.searchKey)
        .subscribe(
          (data) => {
            if (data.code === 1000) {
              this.user = data.result;
              console.log('Tìm kiếm thành công:', this.user);
              this.toastr.success('Tìm kiếm sản phẩm thành công!', 'Thành công');
            } else if (data.code === 1015) {
              this.user = [];
              console.error('Tìm kiếm không thành công:', data);
              this.toastr.error('Không tìm thấy sản phẩm!', 'Tìm kiếm thất bại');
            } 
          }
        );
    } else{
      
      this.loadAllUsers();
    }
  }

}