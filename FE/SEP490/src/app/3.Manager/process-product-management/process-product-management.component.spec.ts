import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessProductManagementComponent } from './process-product-management.component';

describe('ProcessProductManagementComponent', () => {
  let component: ProcessProductManagementComponent;
  let fixture: ComponentFixture<ProcessProductManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProcessProductManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProcessProductManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
