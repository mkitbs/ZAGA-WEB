import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarTractorDriverComponent } from './navbar-tractor-driver.component';

describe('NavbarTractorDriverComponent', () => {
  let component: NavbarTractorDriverComponent;
  let fixture: ComponentFixture<NavbarTractorDriverComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavbarTractorDriverComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarTractorDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
