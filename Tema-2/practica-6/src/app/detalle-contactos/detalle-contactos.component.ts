import { Component, Input, Output, EventEmitter } from '@angular/core';
import {Contacto } from '../contacto';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioContactosComponent} from '../formulario-contactos/formulario-contactos.component'
import { ContactosService } from '../contactos.service';

@Component({
  standalone: true,
  selector: 'app-detalle-contactos',
  templateUrl: './detalle-contactos.component.html',
  styleUrls: ['./detalle-contactos.component.css']
})
export class DetalleContactosComponent {
  @Input() contacto?: Contacto;
  @Output() contactoEditado = new EventEmitter<Contacto>();
  @Output() contactoEliminado = new EventEmitter<number>();

  constructor(private contactosService: ContactosService, private modalService: NgbModal) { }

  editarContacto(): void {
    let ref = this.modalService.open(FormularioContactosComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.contacto = {...this.contacto};
    ref.result.then((contacto: Contacto) => {
      this.contactoEditado.emit(contacto);
    }, (reason) => {});
  }

  eliminarContacto(): void {
    this.contactoEliminado.emit(this.contacto?.id);
  }
}
