import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeSubmaterialManagementComponent } from './employee-submaterial-management.component';

describe('EmployeeSubmaterialManagementComponent', () => {
  let component: EmployeeSubmaterialManagementComponent;
  let fixture: ComponentFixture<EmployeeSubmaterialManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeeSubmaterialManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeSubmaterialManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
