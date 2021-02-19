import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkOrderReportsComponent } from './work-order-reports.component';

describe('WorkOrderReportsComponent', () => {
  let component: WorkOrderReportsComponent;
  let fixture: ComponentFixture<WorkOrderReportsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorkOrderReportsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkOrderReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
