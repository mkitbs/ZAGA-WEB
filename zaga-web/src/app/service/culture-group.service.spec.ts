import { TestBed } from '@angular/core/testing';

import { CultureGroupService } from './culture-group.service';

describe('CultureGroupService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CultureGroupService = TestBed.get(CultureGroupService);
    expect(service).toBeTruthy();
  });
});
