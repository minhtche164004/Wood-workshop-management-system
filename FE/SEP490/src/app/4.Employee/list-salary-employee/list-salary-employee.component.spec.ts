import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListSalaryEmployeeComponent } from './list-salary-employee.component';

describe('ListSalaryEmployeeComponent', () => {
  let component: ListSalaryEmployeeComponent;
  let fixture: ComponentFixture<ListSalaryEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListSalaryEmployeeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListSalaryEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
