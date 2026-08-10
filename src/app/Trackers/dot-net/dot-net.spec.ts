import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DotNet } from './dot-net';

describe('DotNet', () => {
  let component: DotNet;
  let fixture: ComponentFixture<DotNet>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DotNet],
    }).compileComponents();

    fixture = TestBed.createComponent(DotNet);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
