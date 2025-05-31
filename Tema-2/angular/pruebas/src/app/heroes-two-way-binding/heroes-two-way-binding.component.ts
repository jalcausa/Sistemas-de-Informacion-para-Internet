import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Hero } from '../hero';

@Component({
  selector: 'app-heroes-two-way-binding',
  imports: [CommonModule, FormsModule],
  templateUrl: './heroes-two-way-binding.component.html',
  styleUrl: './heroes-two-way-binding.component.css',
})
export class HeroesTwoWayBindingComponent {
  hero: Hero = {
    id: 1,
    name: 'Juan Carlos',
  };
}
