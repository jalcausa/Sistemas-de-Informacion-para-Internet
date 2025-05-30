import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {ContactosService } from './contactos.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

describe('El componente principal', () => {
  const mockService = {
    getContactos: () => [
      {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: 'Hombre'},
      {id: 2, nombre: 'Ana', apellidos: 'García', email: '', telefono: '', genero: 'Mujer'}
    ]
  };

  let fixture: ComponentFixture<AppComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent], // Standalone component
      providers: [
        { provide: ContactosService, useValue: mockService },
        NgbModal]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
  });

  it('debe mostrar a Juan en primer lugar en la lista de botones', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const buttons = compiled.querySelectorAll('button.list-group-item');
    expect(buttons[0].textContent).toContain("Juan");
  });

  it('debe mostrar a Ana en segundo lugar en la lista de botones', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const buttons = compiled.querySelectorAll('button.list-group-item');
    expect(buttons[1].textContent).toContain("Ana");
  });
});
