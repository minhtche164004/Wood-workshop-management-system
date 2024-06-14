import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApiProvinceComponent } from './api-province.component';

describe('ApiProvinceComponent', () => {
  let component: ApiProvinceComponent;
  let fixture: ComponentFixture<ApiProvinceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApiProvinceComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApiProvinceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
