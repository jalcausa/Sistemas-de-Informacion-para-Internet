package es.uma.informatica.sii.practica2;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.Date;
import java.util.List;

import es.uma.informatica.sii.practica2.entidades.Proyecto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import es.uma.informatica.sii.practica2.repositorios.ProyectoRepo;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de planificación de proyectos")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class Practica2ApplicationTests {
	@Autowired
	private TestRestTemplate restTemplate;

	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private ProyectoRepo proyectoRepository;

	@BeforeEach
	public void initializeDatabase() {
		proyectoRepository.deleteAll();
	}

	private URI uri(String scheme, String host, int port, String ...paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path: paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}

	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.get(uri)
				.accept(MediaType.APPLICATION_JSON)
				.build();
		return peticion;
	}

	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.delete(uri)
				.build();
		return peticion;
	}

	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.post(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.body(object);
		return peticion;
	}

	private void compruebaCampos(Proyecto expected, Proyecto actual) {
		assertThat(actual.getFechaInicio()).isEqualTo(expected.getFechaInicio());
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
		assertThat(actual.getDuracion()).isEqualTo(expected.getDuracion());
	}


	@Nested
	@DisplayName("cuando la lista de proyectos está vacía")
	public class ListaVacia {

		@Test
		@DisplayName("devuelve la lista de proyectos vacía")
		public void devuelveLista() {

			var peticion = get("http", "localhost",port, "/api/planificador/proyectos");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Proyecto>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}

		@Nested
		@DisplayName("inserta un proyecto")
		public class InsertaProyectos {
			@Test
			@DisplayName("sin ID")
			public void sinID() {
				Proyecto proyecto = new Proyecto(null,
						"SSI",
						new Date("10/01/2025"),
						10);
				var peticion = post("http", "localhost", port, "/api/planificador/proyectos", proyecto);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuesta(proyecto, respuesta);
			}

			private void compruebaRespuesta(Proyecto proyecto, ResponseEntity<Void> respuesta) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
				assertThat(respuesta.getHeaders().get("Location").get(0))
						.startsWith("http://localhost:"+port+"/api/planificador/proyectos");

				List<Proyecto> proyectos = proyectoRepository.findAll();
				assertThat(proyectos).hasSize(1);
				assertThat(respuesta.getHeaders().get("Location").get(0))
						.endsWith("/"+ proyectos.get(0).getId());
				compruebaCampos(proyecto, proyectos.get(0));
			}

			@Test
			@DisplayName("a pesar de que tenga ID")
			public void conID() {
				Proyecto proyecto = new Proyecto(
						3L,
						"SSI",
						new Date("10/01/2025"),
						10);
				var peticion = post("http", "localhost", port, "/api/planificador/proyectos", proyecto);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuesta(proyecto, respuesta);
			}
		}

		@Test
		@DisplayName("devuelve error cuando se pide un proyecto concreto")
		public void devuelveErrorAlConsultarProyecto() {
			var peticion = get("http", "localhost",port, "/api/planificador/proyectos/1");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Proyecto>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
			assertThat(respuesta.hasBody()).isEqualTo(false);
		}

		@Test
		@DisplayName("devuelve error cuando se modifica un proyecto concreto")
		public void devuelveErrorAlModificarProyecto() {
			Proyecto proyecto = new Proyecto(
					3L,
					"SSI",
					new Date("10/01/2025"),
					10);
			var peticion = put("http", "localhost",port, "/api/planificador/proyectos/1", proyecto);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}

		@Test
		@DisplayName("devuelve error cuando se elimina un proyecto concreto")
		public void devuelveErrorAlEliminarProyecto() {
			var peticion = delete("http", "localhost",port, "/api/planificador/proyectos/1");

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
	}

	@Nested
	@DisplayName("cuando hay proyectos")
	public class ListaConDatos {
		@BeforeEach
		public void introduceDatos() {
			proyectoRepository.save(new Proyecto(null,"SSI",
					new Date("10/01/2025"),
					10));
			proyectoRepository.save(new Proyecto(null,"Amazon",
					new Date("25/02/2025"),
					90));
		}

		@Test
		@DisplayName("devuelve la lista de proyectos correctamente")
		public void devuelveLista() {
			var peticion = get("http", "localhost",port, "/api/planificador/proyectos");

			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<Proyecto>>() {});

			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(2);
		}

		@Nested
		@DisplayName("inserta un proyecto")
		public class InsertaProyectos {
			@Test
			@DisplayName("sin ID")
			public void sinID() {
				Proyecto proyecto = new Proyecto(null,
						"Google",
						new Date("06/06/2025"),
						120);
				var peticion = post("http", "localhost", port, "/api/planificador/proyectos", proyecto);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuesta(proyecto, respuesta);
			}
			@Test
			@DisplayName("a pesar de que tenga ID")
			public void conIDNoExistente() {
				Proyecto proyecto = new Proyecto(
						28L,
						"Google",
						new Date("06/06/2025"),
						120);
				var peticion = post("http", "localhost", port, "/api/planificador/proyectos", proyecto);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuesta(proyecto, respuesta);
			}
			@Test
			@DisplayName("a pesar de que el ID coincida con uno existente")
			public void conIDExistente() {
				Proyecto proyecto = new Proyecto(
						1L,
						"Google",
						new Date("06/06/2025"),
						120);
				var peticion = post("http", "localhost", port, "/api/planificador/proyectos", proyecto);

				var respuesta = restTemplate.exchange(peticion, Void.class);

				compruebaRespuesta(proyecto, respuesta);
			}

			private void compruebaRespuesta(Proyecto proyecto, ResponseEntity<Void> respuesta) {
				assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
				assertThat(respuesta.getHeaders().get("Location").get(0))
						.startsWith("http://localhost:"+port+"/api/planificador/proyectos");

				List<Proyecto> proyectos = proyectoRepository.findAll();
				assertThat(proyectos).hasSize(3);

				Proyecto sonia = proyectos.stream()
						.filter(c->c.getNombre().equals("Google"))
						.findAny()
						.get();

				assertThat(respuesta.getHeaders().get("Location").get(0))
						.endsWith("/"+sonia.getId());
				compruebaCampos(proyecto, sonia);
			}
		}

		@Nested
		@DisplayName("al consultar un proyecto concreto")
		public class ObtenerProyectos {
			@Test
			@DisplayName("lo devuelve cuando existe")
			public void devuelveProyecto() {
				var peticion = get("http", "localhost",port, "/api/planificador/proyectos/1");

				var respuesta = restTemplate.exchange(peticion, Proyecto.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				assertThat(respuesta.hasBody()).isEqualTo(true);
				assertThat(respuesta.getBody()).isNotNull();
			}

			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoProyectoNoExiste() {
				var peticion = get("http", "localhost",port, "/api/planificador/proyectos/28");

				var respuesta = restTemplate.exchange(peticion,
						new ParameterizedTypeReference<List<Proyecto>>() {});

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}

		@Nested
		@DisplayName("al modificar un proyecto")
		public class ModificarProyectos {
			@Test
			@DisplayName("lo modifica correctamente cuando existe")
			@DirtiesContext
			public void modificaCorrectamente() {
				Proyecto proyecto = new Proyecto(
						null,
						"Google",
						new Date("06/06/2025"),
						120);
				var peticion = put("http", "localhost",port, "/api/planificador/proyectos/1", proyecto);

				var respuesta = restTemplate.exchange(peticion,Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				Proyecto proyectoBD = proyectoRepository.findById(1L).get();
				compruebaCampos(proyecto, proyectoBD);
			}
			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoNoExiste() {
				Proyecto proyecto = new Proyecto(
						null,
						"Google",
						new Date("06/06/2025"),
						120);
				var peticion = put("http", "localhost",port, "/api/planificador/proyectos/28", proyecto);

				var respuesta = restTemplate.exchange(peticion,Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}

		@Nested
		@DisplayName("al eliminar un proyecto")
		public class EliminarProyectos {
			@Test
			@DisplayName("lo elimina cuando existe")
			public void eliminaCorrectamente() {
				List<Proyecto> proyectosantes = proyectoRepository.findAll();
				proyectosantes.forEach(c->System.out.println(c));
				var peticion = delete("http", "localhost",port, "/api/planificador/proyectos/1");

				var respuesta = restTemplate.exchange(peticion,Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
				List<Proyecto> proyectos = proyectoRepository.findAll();
				assertThat(proyectos).hasSize(1);
				assertThat(proyectos).allMatch(c->c.getId()!=1);
			}

			@Test
			@DisplayName("da error cuando no existe")
			public void errorCuandoNoExiste() {
				var peticion = delete("http", "localhost",port, "/api/planificador/proyectos/28");

				var respuesta = restTemplate.exchange(peticion,Void.class);

				assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
				assertThat(respuesta.hasBody()).isEqualTo(false);
			}
		}
	}
}