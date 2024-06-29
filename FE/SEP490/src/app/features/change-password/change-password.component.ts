import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {
  formGroup: FormGroup = new FormGroup({}); // Khởi tạo một FormGroup trống

  constructor(private formBuilder: FormBuilder) {}

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      input1: '',
      input2: ''
    });
  
    // Đồng bộ giá trị của input 1 và input 2
    this.formGroup.get('input1')!.valueChanges.subscribe(value => {
      this.formGroup.patchValue({
        input2: value
      });
    });
  
    this.formGroup.get('input2')!.valueChanges.subscribe(value => {
      this.formGroup.patchValue({
        input1: value
      });
    });
}
}