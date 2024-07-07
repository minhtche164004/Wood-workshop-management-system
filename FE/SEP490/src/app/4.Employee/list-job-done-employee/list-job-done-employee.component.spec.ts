import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListJobDoneEmployeeComponent } from './list-job-done-employee.component';

describe('ListJobDoneEmployeeComponent', () => {
  let component: ListJobDoneEmployeeComponent;
  let fixture: ComponentFixture<ListJobDoneEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListJobDoneEmployeeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListJobDoneEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
