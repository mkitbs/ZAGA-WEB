import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkOrderTractorDriverComponent } from './work-order-tractor-driver.component';

describe('WorkOrderTractorDriverComponent', () => {
  let component: WorkOrderTractorDriverComponent;
  let fixture: ComponentFixture<WorkOrderTractorDriverComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkOrderTractorDriverComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkOrderTractorDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
