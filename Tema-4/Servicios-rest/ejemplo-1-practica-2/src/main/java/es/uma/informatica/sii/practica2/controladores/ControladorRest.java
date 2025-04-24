package es.uma.informatica.sii.practica2.controladores;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import es.uma.informatica.sii.practica2.entidades.Contacto;
import es.uma.informatica.sii.practica2.servicios.LogicaContactos;
import es.uma.informatica.sii.practica2.servicios.excepciones.ContactoNoEncontrado;

@RestController
@RequestMapping("/api/agenda/contactos")
public class ControladorRest {
	private LogicaContactos servicio;

	public ControladorRest(LogicaContactos servicioContactos) {
		servicio = servicioContactos;
	}

	@GetMapping
	public ResponseEntity<List<Contacto>> listaDeContactos() {
		return ResponseEntity.ok(servicio.getTodosContactos());
	}

	@PostMapping
	public ResponseEntity<?> aniadirContacto(@RequestBody Contacto contacto, UriComponentsBuilder builder) {
		// TODO
		contacto = servicio.insertarContacto(contacto);
		URI uri = builder
				.path("/api")
				.path("/agenda")
				.path("/contactos")
				.path(String.format("/%d",contacto.getId()))
				.build()
				.toUri();
		return ResponseEntity.created(uri).build();
	}

	@GetMapping("{id}")
	public ResponseEntity<Contacto> getContactoById(@PathVariable Long id) {
		return ResponseEntity.of(servicio.obtenerContacto(id));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> eliminarContacto(@PathVariable Long id) {
		try {
			servicio.eliminarContacto(id);
			return ResponseEntity.ok().build();
		} catch (ContactoNoEncontrado e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("{id}")
	public ResponseEntity<Contacto> actualizarContacto(@PathVariable Long id, @RequestBody Contacto contacto) {
        try {
            contacto.setId(id);
            contacto = servicio.actualizarContacto(contacto);
            return ResponseEntity.ok(contacto);
        } catch (ContactoNoEncontrado e) {
			return ResponseEntity.notFound().build();
        }
    }
}
