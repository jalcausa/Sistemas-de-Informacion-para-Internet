import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormularioContactoComponent } from './formulario-contacto.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

describe('FormularioContactoComponent', () => {
  let component: FormularioContactoComponent;
  let fixture: ComponentFixture<FormularioContactoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FormularioContactoComponent],
      imports: [FormsModule],
      providers: [NgbActiveModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormularioContactoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
