package es.uma.informatica.sii.contactos.controladores;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import es.uma.informatica.sii.contactos.entidades.Contacto;
import es.uma.informatica.sii.contactos.servicios.LogicaContactos;
import es.uma.informatica.sii.contactos.servicios.excepciones.ContactoNoEncontrado;

@RestController
@CrossOrigin
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
	public ResponseEntity<Contacto> aniadirContacto(@RequestBody Contacto contacto, UriComponentsBuilder builder) {
		contacto = servicio.aniadirContacto(contacto);
		URI uri = builder
				.path("/api")
				.path("/agenda")
				.path("/contactos")
				.path(String.format("/%d",contacto.getId()))
				.build()
				.toUri();
		return ResponseEntity.created(uri).body(contacto);
	}

	@GetMapping("{id}")
	public ResponseEntity<Contacto> getContacto(@PathVariable Long id) {
		return ResponseEntity.of(servicio.getContactoPorId(id));
	}

	@PutMapping("{id}")
	public ResponseEntity<Contacto> modificarContacto(@PathVariable Long id, @RequestBody Contacto contacto) {
		contacto.setId(id);
		try {
			contacto = servicio.modificarContacto(contacto);
			return ResponseEntity.ok(contacto);
		} catch (ContactoNoEncontrado e) {
			return ResponseEntity.notFound().build();
		}
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




}
