import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Courseinfo } from './courseinfo';

describe('Courseinfo', () => {
  let component: Courseinfo;
  let fixture: ComponentFixture<Courseinfo>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Courseinfo],
    }).compileComponents();

    fixture = TestBed.createComponent(Courseinfo);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
