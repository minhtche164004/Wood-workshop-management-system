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
 
}
}