import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeroChildComponent } from '../hero-child/hero-child.component';
import { Hero } from '../hero';

@Component({
  selector: 'app-hero-parent',
  imports: [CommonModule, HeroChildComponent],
  templateUrl: './hero-parent.component.html',
  styleUrl: './hero-parent.component.css',
})
export class HeroParentComponent {
  master = 'Master jalcausa';

  heroes: Hero[] = [
    { id: 1, name: 'Superman' },
    { id: 2, name: 'Batman' },
    { id: 3, name: 'Wonder Woman' },
  ];

  lastMessage = '';
  lastMessageTime = new Date();
  heroStatuses: { hero: string; status: string; timestamp: Date }[] = [];

  onChildMessage(message: string) {
    this.lastMessage = message;
    this.lastMessageTime = new Date();
    console.log('Message received from child:', message);
  }

  onStatusUpdate(statusData: { hero: string; status: string }) {
    const statusEntry = {
      ...statusData,
      timestamp: new Date(),
    };

    this.heroStatuses.unshift(statusEntry);

    if (this.heroStatuses.length > 5) {
      this.heroStatuses = this.heroStatuses.slice(0, 5);
    }

    console.log('Status update received:', statusEntry);
  }
}
