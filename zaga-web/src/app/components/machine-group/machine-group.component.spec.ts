import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MachineGroupComponent } from './machine-group.component';

describe('MachineGroupComponent', () => {
  let component: MachineGroupComponent;
  let fixture: ComponentFixture<MachineGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MachineGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MachineGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
