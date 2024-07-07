import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderRequiredComponent } from './order-required.component';

describe('OrderRequiredComponent', () => {
  let component: OrderRequiredComponent;
  let fixture: ComponentFixture<OrderRequiredComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderRequiredComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderRequiredComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
