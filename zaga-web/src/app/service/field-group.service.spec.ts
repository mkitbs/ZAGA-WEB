import { TestBed } from '@angular/core/testing';

import { FieldGroupService } from './field-group.service';

describe('FieldGroupService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FieldGroupService = TestBed.get(FieldGroupService);
    expect(service).toBeTruthy();
  });
});
