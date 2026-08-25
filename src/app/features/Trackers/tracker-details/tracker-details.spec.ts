import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackerDetails } from './tracker-details';

describe('TrackerDetails', () => {
  let component: TrackerDetails;
  let fixture: ComponentFixture<TrackerDetails>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackerDetails],
    }).compileComponents();

    fixture = TestBed.createComponent(TrackerDetails);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
