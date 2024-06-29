import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeJobComponent } from './employee-job.component';

describe('EmployeeJobComponent', () => {
  let component: EmployeeJobComponent;
  let fixture: ComponentFixture<EmployeeJobComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmployeeJobComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmployeeJobComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
