import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CultureGroupComponent } from './culture-group.component';

describe('CultureGroupComponent', () => {
  let component: CultureGroupComponent;
  let fixture: ComponentFixture<CultureGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CultureGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CultureGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
