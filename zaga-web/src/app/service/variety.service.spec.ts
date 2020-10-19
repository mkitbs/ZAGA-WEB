import { TestBed } from '@angular/core/testing';

import { VarietyService } from './variety.service';

describe('VarietyService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: VarietyService = TestBed.get(VarietyService);
    expect(service).toBeTruthy();
  });
});
