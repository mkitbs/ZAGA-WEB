import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateworkOrderComponent } from './creatework-order.component';

describe('CreateworkOrderComponent', () => {
  let component: CreateworkOrderComponent;
  let fixture: ComponentFixture<CreateworkOrderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateworkOrderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateworkOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
