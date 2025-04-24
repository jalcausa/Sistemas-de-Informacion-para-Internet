package es.uma.informatica.sii.practica2.controladores;

import java.net.URI;
import java.util.List;

import es.uma.informatica.sii.practica2.entidades.Proyecto;
import es.uma.informatica.sii.practica2.servicios.excepciones.ProyectoNoEncontrado;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import es.uma.informatica.sii.practica2.servicios.LogicaProyectos;

@RestController
@RequestMapping("/api/planificador/proyectos")
public class ControladorRest {
	private LogicaProyectos servicio;

	public ControladorRest(LogicaProyectos servicioProyectos) {
		servicio = servicioProyectos;
	}

	@GetMapping
	public ResponseEntity<List<Proyecto>> listaDeProyectos() {
		return ResponseEntity.ok(servicio.getTodosProyectos());
	}

	@PostMapping
	public ResponseEntity<?> anadirProyecto(@RequestBody Proyecto proyecto,
											UriComponentsBuilder builder) {
		//TODO
		proyecto = servicio.anadirProyecto(proyecto);
		URI uri = builder
				.path("/api")
				.path("/planificador")
				.path("/proyectos")
				.path(String.format("/%d", proyecto.getId()))
				.build()
				.toUri();
		//TODO
		return ResponseEntity.created(uri).build();
	}

	@GetMapping("{id}")
	public ResponseEntity<Proyecto> getProyecto(@PathVariable Long id) {
		return ResponseEntity.of(servicio.getProyectoPorId(id));
	}

	@PutMapping("{id}")
	public ResponseEntity<?> modificarProyecto(@PathVariable Long id,
											   @RequestBody Proyecto proyecto
											   							) {
		//TODO return OK si se modifica
		//TODO return NOT_FOUND si no existe
		try {
			proyecto.setId(id);
			servicio.modificarProyecto(proyecto);
			return ResponseEntity.ok(proyecto);
		} catch (ProyectoNoEncontrado e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> eliminarProyecto(@PathVariable Long id) {
	  //TODO return OK si se elimina
	  //TODO return NOT_FOUND si no existe
		try {
			servicio.eliminarProyecto(id);
			return ResponseEntity.ok().build();
		} catch (ProyectoNoEncontrado e) {
			return ResponseEntity.notFound().build();
		}
	}
}