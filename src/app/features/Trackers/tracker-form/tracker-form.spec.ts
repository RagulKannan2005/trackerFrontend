import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TrackerForm } from './tracker-form';

describe('TrackerForm', () => {
  let component: TrackerForm;
  let fixture: ComponentFixture<TrackerForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TrackerForm],
    }).compileComponents();

    fixture = TestBed.createComponent(TrackerForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
