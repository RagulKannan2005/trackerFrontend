import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SectionDeatils } from './section-deatils';

describe('SectionDeatils', () => {
  let component: SectionDeatils;
  let fixture: ComponentFixture<SectionDeatils>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SectionDeatils],
    }).compileComponents();

    fixture = TestBed.createComponent(SectionDeatils);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
