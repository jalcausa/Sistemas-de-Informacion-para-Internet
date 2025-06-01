import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Hero } from '../hero';

@Component({
  selector: 'app-hero-child',
  templateUrl: './hero-child.component.html',
  styleUrl: './hero-child.component.css',
})
export class HeroChildComponent {
  @Input() hero!: Hero;
  @Input('master') masterName = '';

  @Output() messageToParent = new EventEmitter<string>();
  @Output() statusUpdate = new EventEmitter<{ hero: string; status: string }>();

  sendGreeting() {
    const message = `Hello from ${this.hero.name}! Ready for action!`;
    this.messageToParent.emit(message);
  }

  sendStatus() {
    const status = {
      hero: this.hero.name,
      status: 'Ready for duty',
    };
    this.statusUpdate.emit(status);
  }

  sendMission() {
    const message = `${this.hero.name} requests a new mission from ${this.masterName}`;
    this.messageToParent.emit(message);
  }
}
