import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvancedBillManagementComponent } from './advanced-bill-management.component';

describe('AdvancedBillManagementComponent', () => {
  let component: AdvancedBillManagementComponent;
  let fixture: ComponentFixture<AdvancedBillManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdvancedBillManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdvancedBillManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
