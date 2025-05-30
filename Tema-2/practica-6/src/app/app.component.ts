import { Component, OnInit } from '@angular/core';
import {Contacto } from './contacto';
import {ContactosService } from './contactos.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioContactosComponent} from './formulario-contactos/formulario-contactos.component'
import {DetalleContactosComponent} from './detalle-contactos/detalle-contactos.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [DetalleContactosComponent, CommonModule],
  standalone: true
})
export class AppComponent implements OnInit {
  contactos: Contacto [] = [];
  contactoElegido?: Contacto;

  constructor(private contactosService: ContactosService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.contactos = this.contactosService.getContactos();
  }

  elegirContacto(contacto: Contacto): void {
    this.contactoElegido = contacto;
  }

  aniadirContacto(): void {
    let ref = this.modalService.open(FormularioContactosComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.contacto = {id: 0, nombre: '', apellidos: '', email: '', telefono: ''};
    ref.result.then((contacto: Contacto) => {
      this.contactosService.addContacto(contacto);
      this.contactos = this.contactosService.getContactos();
    }, (reason) => {});

  }
  contactoEditado(contacto: Contacto): void {
    this.contactosService.editarContacto(contacto);
    this.contactos = this.contactosService.getContactos();
    this.contactoElegido = this.contactos.find(c => c.id == contacto.id);
  }

  eliminarContacto(id: number): void {
    this.contactosService.eliminarContacto(id);
    this.contactos = this.contactosService.getContactos();
    this.contactoElegido = undefined;
  }
}
