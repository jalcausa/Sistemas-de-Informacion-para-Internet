import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-boton-contador',
  template: `
    <div>
      <h3>Soy el componente hijo</h3>
      <p>He sido clickeado {{ contador }} veces</p>
      <button (click)="hacerClick()">¡Haz click aquí!</button>
    </div>
  `,
})
export class BotonContadorComponent {
  contador = 0;

  // ✅ Esto es como darle un "megáfono" al hijo
  @Output() clickRealizado = new EventEmitter<number>();

  hacerClick() {
    this.contador++;

    // ✅ El hijo "grita" al padre: "¡Oye, me hicieron click!"
    this.clickRealizado.emit(this.contador);
  }
}
