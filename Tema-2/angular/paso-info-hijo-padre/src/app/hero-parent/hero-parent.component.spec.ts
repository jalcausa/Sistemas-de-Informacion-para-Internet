import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeroParentComponent } from './hero-parent.component';

describe('HeroParentComponent', () => {
  let component: HeroParentComponent;
  let fixture: ComponentFixture<HeroParentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeroParentComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeroParentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
