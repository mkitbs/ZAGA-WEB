import { TestBed } from '@angular/core/testing';

import { OperationGroupService } from './operation-group.service';

describe('OperationGroupService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: OperationGroupService = TestBed.get(OperationGroupService);
    expect(service).toBeTruthy();
  });
});
