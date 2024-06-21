import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubMaterialManagementComponent } from './sub-material-management.component';

describe('SubMaterialManagementComponent', () => {
  let component: SubMaterialManagementComponent;
  let fixture: ComponentFixture<SubMaterialManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SubMaterialManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubMaterialManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
