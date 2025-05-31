import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeroesPropertyBindingComponent } from './heroes-property-binding.component';

describe('HeroesPropertyBindingComponent', () => {
  let component: HeroesPropertyBindingComponent;
  let fixture: ComponentFixture<HeroesPropertyBindingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeroesPropertyBindingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeroesPropertyBindingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
