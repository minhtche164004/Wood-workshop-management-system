import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentSalaryComponent } from './payment-salary.component';

describe('PaymentSalaryComponent', () => {
  let component: PaymentSalaryComponent;
  let fixture: ComponentFixture<PaymentSalaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentSalaryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaymentSalaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
