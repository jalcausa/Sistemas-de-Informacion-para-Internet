import { Component } from '@angular/core';
import { BotonContadorComponent } from './hijo/hijo.component';

@Component({
  selector: 'app-root',
  imports: [BotonContadorComponent],
  template: `
    <div>
      <h1>Soy el componente padre</h1>
      <p>
        El hijo me ha dicho que ha sido clickeado: {{ mensajeDelHijo }} veces
      </p>

      <!-- ✅ El padre "escucha" los gritos del hijo -->
      <app-boton-contador (clickRealizado)="escucharAlHijo($event)">
      </app-boton-contador>
    </div>
  `,
})
export class AppComponent {
  mensajeDelHijo = 0;

  // ✅ Método que se ejecuta cuando el hijo "grita"
  escucharAlHijo(numeroDeClicks: number) {
    this.mensajeDelHijo = numeroDeClicks;
    console.log(`El hijo me dijo: "Me han clickeado ${numeroDeClicks} veces"`);
  }
}
