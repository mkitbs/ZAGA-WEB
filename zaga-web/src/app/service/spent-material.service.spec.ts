import { TestBed } from '@angular/core/testing';

import { SpentMaterialService } from './spent-material.service';

describe('SpentMaterialService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SpentMaterialService = TestBed.get(SpentMaterialService);
    expect(service).toBeTruthy();
  });
});
