import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LeavePageConfirmationComponent } from './leave-page-confirmation.component';

describe('LeavePageConfirmationComponent', () => {
  let component: LeavePageConfirmationComponent;
  let fixture: ComponentFixture<LeavePageConfirmationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LeavePageConfirmationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeavePageConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
