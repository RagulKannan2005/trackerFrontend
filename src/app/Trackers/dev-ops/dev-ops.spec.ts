import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DevOps } from './dev-ops';

describe('DevOps', () => {
  let component: DevOps;
  let fixture: ComponentFixture<DevOps>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DevOps],
    }).compileComponents();

    fixture = TestBed.createComponent(DevOps);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
