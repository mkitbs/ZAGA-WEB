import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportAtmComponent } from './report-atm.component';

describe('ReportAtmComponent', () => {
  let component: ReportAtmComponent;
  let fixture: ComponentFixture<ReportAtmComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportAtmComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportAtmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
