import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenListService } from 'src/app/service/authen.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  formGroup: FormGroup;
  errorMessage: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private authenListService: AuthenListService,
    private router: Router,
    private toastr: ToastrService // Inject ToastrService
  ) {
    this.formGroup = this.formBuilder.group({
      old_pass: ['', Validators.required],
      new_pass: ['', Validators.required],
      check_pass: ['', Validators.required]
    });
  }

  ngOnInit(): void {}

  onSubmit(): void {
    if (this.formGroup.invalid) {
      this.errorMessage = 'Vui lòng điền đầy đủ thông tin.';
      return;
    }

    if (this.formGroup.value.new_pass !== this.formGroup.value.check_pass) {
      // Use ToastrService to show error message
      this.toastr.error('Mật khẩu mới và xác nhận mật khẩu không khớp.', 'Lỗi xác thực');
      return;
    }

    const oldPassword = this.formGroup.value.old_pass;
    const newPassword = this.formGroup.value.new_pass;


    this.authenListService.getChangePass(oldPassword, newPassword).subscribe({
      next: (response) => {
        console.log('Đổi mật khẩu thành công', response);
        this.toastr.success('Đổi mật khẩu thành công', 'Thành công');
        this.router.navigate(['/homepage']);
      },
      error: (error) => {
        console.error('Lỗi khi đổi mật khẩu', error);
        if (error.error.code === 1006) {
          this.toastr.error('Mật khẩu cũ không chính xác. Vui lòng thử lại.', 'Lỗi');
        } else {
          this.toastr.error('Lỗi khi đổi mật khẩu. Vui lòng thử lại.', 'Lỗi');
        }
      },
     
    });
  }
}
