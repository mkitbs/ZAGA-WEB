import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportMaterialComponent } from './report-material.component';

describe('ReportMaterialComponent', () => {
  let component: ReportMaterialComponent;
  let fixture: ComponentFixture<ReportMaterialComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReportMaterialComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportMaterialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
