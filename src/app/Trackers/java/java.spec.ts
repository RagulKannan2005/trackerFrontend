import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Java } from './java';

describe('Java', () => {
  let component: Java;
  let fixture: ComponentFixture<Java>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Java],
    }).compileComponents();

    fixture = TestBed.createComponent(Java);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
