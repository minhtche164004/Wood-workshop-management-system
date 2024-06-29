import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubSubmaterialProductComponent } from './sub-submaterial-product.component';

describe('SubSubmaterialProductComponent', () => {
  let component: SubSubmaterialProductComponent;
  let fixture: ComponentFixture<SubSubmaterialProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubSubmaterialProductComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubSubmaterialProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
