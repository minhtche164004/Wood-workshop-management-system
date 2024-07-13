import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MaterialManagementComponent } from './material-management.component';

describe('MaterialManagementComponent', () => {
  let component: MaterialManagementComponent;
  let fixture: ComponentFixture<MaterialManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MaterialManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MaterialManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
