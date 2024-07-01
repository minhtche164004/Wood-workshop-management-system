import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FeedbackDefectiveComponent } from './feedback-defective.component';

describe('FeedbackDefectiveComponent', () => {
  let component: FeedbackDefectiveComponent;
  let fixture: ComponentFixture<FeedbackDefectiveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FeedbackDefectiveComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FeedbackDefectiveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
