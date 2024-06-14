import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerifyOtpMailComponent } from './verify-otp-mail.component';

describe('VerifyOtpMailComponent', () => {
  let component: VerifyOtpMailComponent;
  let fixture: ComponentFixture<VerifyOtpMailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VerifyOtpMailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerifyOtpMailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
