import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportMachineComponent } from './report-machine.component';

describe('ReportMachineComponent', () => {
  let component: ReportMachineComponent;
  let fixture: ComponentFixture<ReportMachineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportMachineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportMachineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
