import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListRequestProductComponent } from './list-request-product.component';

describe('ListRequestProductComponent', () => {
  let component: ListRequestProductComponent;
  let fixture: ComponentFixture<ListRequestProductComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListRequestProductComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListRequestProductComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
