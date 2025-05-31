import { Component } from '@angular/core';
import { HeroesComponent } from "./heroes/heroes.component";
import { HeroesPropertyBindingComponent } from "./heroes-property-binding/heroes-property-binding.component";
import { HeroesTwoWayBindingComponent } from "./heroes-two-way-binding/heroes-two-way-binding.component";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [HeroesComponent, HeroesPropertyBindingComponent, HeroesTwoWayBindingComponent]
})
export class AppComponent {
  title = 'pruebas';
}
