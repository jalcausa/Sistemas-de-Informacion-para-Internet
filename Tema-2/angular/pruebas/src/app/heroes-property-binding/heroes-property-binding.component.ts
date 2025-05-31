import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-heroes-property-binding',
  imports: [CommonModule],
  templateUrl: './heroes-property-binding.component.html',
  styleUrl: './heroes-property-binding.component.css',
})
export class HeroesPropertyBindingComponent implements OnInit{
  selected: boolean = true;
  canSave: boolean = true;
  isUnchanged: boolean = true;
  isSpecial: boolean = true;

  getClasses(): string {
    return 'btn btn-primary';
  }

  currentClasses: Record<string, boolean> = {};

  setCurrentClasses() {
    this.currentClasses = {
      saveable: this.canSave,
      modified: !this.isUnchanged,
      special: this.isSpecial
    };
  }

  ngOnInit(): void {
    this.setCurrentClasses()
  }
}
