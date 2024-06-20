import { Component, OnInit } from '@angular/core';
import { AuthenListService } from 'src/app/service/authen.service';
import { ProvincesService } from 'src/app/service/provinces.service';
import { FormControl } from '@angular/forms';

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
  selector: 'app-view-profile',
  templateUrl: './view-profile.component.html',
  styleUrls: ['./view-profile.component.scss']
})
export class ViewProfileComponent implements OnInit {
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  provinceControl = new FormControl();
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;

  userProfile: any = {};

  constructor(private provincesService: ProvincesService, private authenListService: AuthenListService) { }

  ngOnInit() {
    this.userProfile = {
      username: '',
      email: '',
      phoneNumber: '',
      address: '',
       fullname: '',
      bank_name: '',
      bank_number: '',
      city: '',
      district: '',
      wards: '',
     
      // Add more properties as needed
  };

  // Assuming you receive data from an API call
  this.authenListService.getUserProfile().subscribe((data) => {
      this.userProfile = data.result; // Assuming 'result' contains the profile data
      console.log(data);
  });

  this.provincesService.getProvinces().subscribe((data: Province[]) => {
    this.provinces = data;
    // Ví dụ lặp qua các tỉnh/thành phố để lấy ra các quận/huyện và phường/xã
    this.provinces.forEach(province => {
      this.districts.push(...province.districts);
      province.districts.forEach(district => {
        this.wards.push(...district.wards);
      });
    });
 
  });
  this.provinceControl.valueChanges.subscribe(provinceName => {
    this.onProvinceChange();
  });

  }
  onProvinceChange() {
    const selectedProvinceName = this.userProfile.city; // assuming 'city' is bound to ngModel of the province dropdown
    this.selectedProvince = this.provinces.find(province => province.name === selectedProvinceName);
    
    // Update districts based on the selected province
    this.districts = this.selectedProvince ? this.selectedProvince.districts : [];
    
    // Reset selected district and ward
    this.userProfile.district = ''; // reset selected district in the model
    this.userProfile.wards = ''; // reset selected ward in the model
  }

  onDistrictChange() {
    const selectedDistrictName = this.userProfile.district; // assuming 'city' is bound to ngModel of the province dropdown
    this.selectedDistrict = this.districts.find(district => district.name === selectedDistrictName);
    
    // Update districts based on the selected province
    this.wards = this.selectedDistrict ? this.selectedDistrict.wards : [];
    
    this.userProfile.wards = ''; // reset selected ward in the model
  }




  saveChanges() {
    console.log('Saving profile changes:', this.userProfile); // Log userProfile object for debugging
    
    this.authenListService.updateUserProfile(this.userProfile).subscribe(
      (response: any) => {
        console.log('Profile updated successfully:', response);
        // Optionally, you can notify the user or perform other actions upon successful update
      },
      (error: any) => {
        console.error('Error updating profile:', error);
        // Optionally, log specific error details or show user-friendly error messages
      }
    );
  }
  
  
}
