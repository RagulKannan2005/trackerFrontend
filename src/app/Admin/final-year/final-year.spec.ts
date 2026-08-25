import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalYear } from './final-year';

describe('FinalYear', () => {
  let component: FinalYear;
  let fixture: ComponentFixture<FinalYear>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinalYear],
    }).compileComponents();

    fixture = TestBed.createComponent(FinalYear);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
