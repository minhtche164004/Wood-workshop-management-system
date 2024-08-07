import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCateManagementComponent } from './product-cate-management.component';

describe('ProductCateManagementComponent', () => {
  let component: ProductCateManagementComponent;
  let fixture: ComponentFixture<ProductCateManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProductCateManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductCateManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
