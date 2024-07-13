import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderFailComponent } from './order-fail.component';

describe('OrderFailComponent', () => {
  let component: OrderFailComponent;
  let fixture: ComponentFixture<OrderFailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderFailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderFailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
