import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListJobDoneComponent } from './list-job-done.component';

describe('ListJobDoneComponent', () => {
  let component: ListJobDoneComponent;
  let fixture: ComponentFixture<ListJobDoneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListJobDoneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListJobDoneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
