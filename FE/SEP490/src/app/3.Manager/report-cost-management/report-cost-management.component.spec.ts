import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportCostManagementComponent } from './report-cost-management.component';

describe('ReportCostManagementComponent', () => {
  let component: ReportCostManagementComponent;
  let fixture: ComponentFixture<ReportCostManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportCostManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportCostManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
