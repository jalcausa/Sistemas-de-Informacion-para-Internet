import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeroesTwoWayBindingComponent } from './heroes-two-way-binding.component';

describe('HeroesTwoWayBindingComponent', () => {
  let component: HeroesTwoWayBindingComponent;
  let fixture: ComponentFixture<HeroesTwoWayBindingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HeroesTwoWayBindingComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeroesTwoWayBindingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
