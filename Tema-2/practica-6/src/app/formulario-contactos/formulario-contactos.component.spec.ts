import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormularioContactosComponent } from './formulario-contactos.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';


describe('El formulario de contactos', () => {
  let component: FormularioContactosComponent;
  let fixture: ComponentFixture<FormularioContactosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsModule, FormularioContactosComponent ],
      providers: [NgbActiveModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormularioContactosComponent);
    component = fixture.componentInstance;

  });

  it('debe mostrar una lista de selección debajo del teléfono', () => {
    component.accion = "Añadir";
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#telefono + label')).not.toBeNull();
    expect(compiled.querySelector('#telefono + label')?.textContent).toContain('Género');
    expect(compiled.querySelector('#telefono ~ select')).not.toBeNull();
  });

  it('debe mostrar el seleciconado el género del contacto (probando Hombre)', (done: DoneFn) => {
    component.accion = "Añadir";
    component.contacto = {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: 'Hombre'};
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const lista = compiled.querySelector('#telefono ~ select') as HTMLInputElement;
    fixture.whenStable().then(() => {
      expect(lista.getAttribute('ng-reflect-model')).toBe('Hombre');
      done();
    });
  });

  it('debe mostrar el seleciconado el género del contacto (probando Mujer)', (done: DoneFn) => {
    component.accion = "Añadir";
    component.contacto = {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: 'Mujer'};
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const lista = compiled.querySelector('#telefono ~ select') as HTMLInputElement;
    fixture.whenStable().then(() => {
      expect(lista.getAttribute('ng-reflect-model')).toBe('Mujer');
      done();
    });
  });

  it('debe mostrar el seleciconado el género del contacto (pobando cadena vacía)', (done: DoneFn) => {
    component.accion = "Añadir";
    component.contacto = {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: ''};
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const lista = compiled.querySelector('#telefono ~ select') as HTMLInputElement;
    fixture.whenStable().then(() => {
      expect(lista.getAttribute('ng-reflect-model')).toBe('');
      done();
    });
  });


  it('debe cambiar el modelo cuando se cambia el género', (done: DoneFn) => {
    component.accion = "Añadir";
    component.contacto = {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: ''};
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const lista = compiled.querySelector('#telefono ~ select') as HTMLInputElement;
    const opcionAMarcar = compiled.querySelector('option:nth-child(3)') as HTMLInputElement;
    lista.value = opcionAMarcar.value;
    lista.dispatchEvent(new Event('change'));
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      expect(component.contacto.genero).toBe(opcionAMarcar.value);
      done();
    });
  });

  it('debe cambiar el modelo cuando se cambia el género', (done: DoneFn) => {
    component.accion = "Añadir";
    component.contacto = {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: ''};
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const lista = compiled.querySelector('#telefono ~ select') as HTMLInputElement;
    const opcionAMarcar = compiled.querySelector('option:nth-child(11)') as HTMLInputElement;
    lista.value = opcionAMarcar.value;
    lista.dispatchEvent(new Event('change'));
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      expect(component.contacto.genero).toBe(opcionAMarcar.value);
      done();
    });
  });
});

