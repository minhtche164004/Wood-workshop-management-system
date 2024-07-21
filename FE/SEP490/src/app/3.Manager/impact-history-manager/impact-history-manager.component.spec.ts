import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImpactHistoryManagerComponent } from './impact-history-manager.component';

describe('ImpactHistoryManagerComponent', () => {
  let component: ImpactHistoryManagerComponent;
  let fixture: ComponentFixture<ImpactHistoryManagerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImpactHistoryManagerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ImpactHistoryManagerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
