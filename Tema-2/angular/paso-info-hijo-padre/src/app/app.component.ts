import { Component } from '@angular/core';
import { HeroParentComponent } from './hero-parent/hero-parent.component';

@Component({
  selector: 'app-root',
  imports: [ HeroParentComponent ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'paso-info-hijo-padre';
}
