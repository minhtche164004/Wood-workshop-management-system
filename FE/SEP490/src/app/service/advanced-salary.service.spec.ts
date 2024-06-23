import { TestBed } from '@angular/core/testing';

import { AdvancedSalaryService } from './advanced-salary.service';

describe('AdvancedSalaryService', () => {
  let service: AdvancedSalaryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdvancedSalaryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
