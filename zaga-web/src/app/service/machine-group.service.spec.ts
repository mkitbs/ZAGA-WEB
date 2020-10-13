import { TestBed } from '@angular/core/testing';

import { MachineGroupService } from './machine-group.service';

describe('MachineGroupService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: MachineGroupService = TestBed.get(MachineGroupService);
    expect(service).toBeTruthy();
  });
});
