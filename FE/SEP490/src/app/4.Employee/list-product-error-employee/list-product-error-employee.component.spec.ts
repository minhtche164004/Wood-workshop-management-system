import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListProductErrorEmployeeComponent } from './list-product-error-employee.component';

describe('ListProductErrorEmployeeComponent', () => {
  let component: ListProductErrorEmployeeComponent;
  let fixture: ComponentFixture<ListProductErrorEmployeeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListProductErrorEmployeeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListProductErrorEmployeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
