import { TestBed, async, inject } from '@angular/core/testing';

import { LeavePageGuard } from './leave-page.guard';

describe('LeavePageGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LeavePageGuard]
    });
  });

  it('should ...', inject([LeavePageGuard], (guard: LeavePageGuard) => {
    expect(guard).toBeTruthy();
  }));
});
