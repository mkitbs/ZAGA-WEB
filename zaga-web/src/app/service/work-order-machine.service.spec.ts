import { TestBed } from '@angular/core/testing';

import { WorkOrderMachineService } from './work-order-machine.service';

describe('WorkOrderMachineService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WorkOrderMachineService = TestBed.get(WorkOrderMachineService);
    expect(service).toBeTruthy();
  });
});
