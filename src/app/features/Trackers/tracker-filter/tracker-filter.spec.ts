import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackerFilter } from './tracker-filter';

describe('TrackerFilter', () => {
  let component: TrackerFilter;
  let fixture: ComponentFixture<TrackerFilter>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackerFilter],
    }).compileComponents();

    fixture = TestBed.createComponent(TrackerFilter);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
