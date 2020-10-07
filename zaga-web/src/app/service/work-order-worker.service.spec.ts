import { TestBed } from '@angular/core/testing';

import { WorkOrderWorkerService } from './work-order-worker.service';

describe('WorkOrderWorkerService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WorkOrderWorkerService = TestBed.get(WorkOrderWorkerService);
    expect(service).toBeTruthy();
  });
});
