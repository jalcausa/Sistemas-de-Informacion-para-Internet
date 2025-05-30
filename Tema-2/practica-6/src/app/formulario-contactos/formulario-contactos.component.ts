import { Component } from '@angular/core';
import  {Contacto} from '../contacto';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [FormsModule, CommonModule],
  selector: 'app-formulario-contacto',
  templateUrl: './formulario-contactos.component.html',
  styleUrls: ['./formulario-contactos.component.css']
})
export class FormularioContactosComponent {
  accion?: "Añadir" | "Editar";
  contacto: Contacto = {id: 0, nombre: '', apellidos: '', email: '', telefono: '', genero: ''};

  generos: String[] = [
	'Hombre', 
	'Mujer', 
	'Gay', 
	'Lesbiana', 
	'Transexual', 
	'Bisexual',
	'Intersexual', 
	'Queer', 
	'Asexual', 
	'Pansexual', 
	'Polisexual', 
	'Otro'
  ];

  constructor(public modal: NgbActiveModal) { }

  guardarContacto(): void {
    this.modal.close(this.contacto);
  }

}
