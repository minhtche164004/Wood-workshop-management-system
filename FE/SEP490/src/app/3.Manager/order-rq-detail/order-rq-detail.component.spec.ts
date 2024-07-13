import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderRqDetailComponent } from './order-rq-detail.component';

describe('OrderRqDetailComponent', () => {
  let component: OrderRqDetailComponent;
  let fixture: ComponentFixture<OrderRqDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ OrderRqDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OrderRqDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
