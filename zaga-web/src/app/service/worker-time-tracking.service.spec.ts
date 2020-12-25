import { TestBed } from '@angular/core/testing';

import { WorkerTimeTrackingService } from './worker-time-tracking.service';

describe('WorkerTimeTrackingService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WorkerTimeTrackingService = TestBed.get(WorkerTimeTrackingService);
    expect(service).toBeTruthy();
  });
});
