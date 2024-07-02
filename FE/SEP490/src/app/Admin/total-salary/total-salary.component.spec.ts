import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TotalSalaryComponent } from './total-salary.component';

describe('TotalSalaryComponent', () => {
  let component: TotalSalaryComponent;
  let fixture: ComponentFixture<TotalSalaryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TotalSalaryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TotalSalaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
