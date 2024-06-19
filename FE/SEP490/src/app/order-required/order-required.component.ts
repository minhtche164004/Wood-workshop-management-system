import { Component, OnInit } from '@angular/core';
import { ProvincesService } from '../service/provinces.service'; 
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
  selector: 'app-order-required',
  templateUrl: './order-required.component.html',
  styleUrls: ['./order-required.component.scss']
})
export class OrderRequiredComponent implements OnInit {
  provinces: Province[] = [];
  districts: District[] = [];
  wards: Ward[] = [];
  provinceControl = new FormControl() ;
  districtControl = new FormControl();
  wardControl = new FormControl();
  selectedProvince: any;
  selectedDistrict: any;

  constructor(private provincesService: ProvincesService) { }
  onFileSelected(event: any) {
    const selectedFile = event.target.files[0];
    // Xử lý logic khi đã chọn file, ví dụ như upload lên server
    console.log('Selected File:', selectedFile);
  }
  ngOnInit() {
    this.provincesService.getProvinces().subscribe((data: Province[]) => {
      this.provinces = data;
      console.log(this.provinces);
    });

    this.provinceControl.valueChanges.subscribe(provinceCode => {
//      console.log('provinceCode:', provinceCode);
      this.selectedProvince = this.provinces.find(province => province.code == provinceCode);
//      console.log('selectedProvince:', this.selectedProvince);
      this.districts = this.selectedProvince ? this.selectedProvince.districts : [];
    });

    this.districtControl.valueChanges.subscribe(districtCode => {
//      console.log('districtCode:', districtCode);
      const selectedDistrict = this.districts.find(district => district.code == districtCode);
//      console.log('selectedDistrict:', this.selectedDistrict);
      this.wards = selectedDistrict ? selectedDistrict.wards : [];
      this.wardControl.reset();
    });
  }
}
