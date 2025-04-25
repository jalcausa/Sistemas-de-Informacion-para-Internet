package es.uma.informatica.sii.eventosasistentes;


import es.uma.informatica.sii.eventosasistentes.dtos.AsistenteDTO;
import es.uma.informatica.sii.eventosasistentes.dtos.EventoDTO;
import es.uma.informatica.sii.eventosasistentes.entities.Asistente;
import es.uma.informatica.sii.eventosasistentes.entities.Evento;
import es.uma.informatica.sii.eventosasistentes.repositories.AsistenteRepo;
import es.uma.informatica.sii.eventosasistentes.repositories.EventoRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de eventos y asistentes")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EventosAsistentesApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private EventoRepo eventoRepository;

    @Autowired
    private AsistenteRepo asistenteRepository;

    @BeforeEach
    public void initializeDatabase() {
        eventoRepository.deleteAll();
        asistenteRepository.deleteAll();
    }

    private URI uri(String scheme, String host, int port, String... paths) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host).port(port);
        for (String path : paths) {
            ub = ub.path(path);
        }
        return ub.build();
    }

    private RequestEntity<Void> get(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .build();
        return peticion;
    }

    private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.delete(uri)
                .build();
        return peticion;
    }

    private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
        URI uri = uri(scheme, host, port, path);
        var peticion = RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .body(object);
        return peticion;
    }

    private URI uriWithQueryParams(String scheme, String host, int port, String path, String paramName, List<Long> paramValues) {
        UriBuilderFactory ubf = new DefaultUriBuilderFactory();
        UriBuilder ub = ubf.builder()
                .scheme(scheme)
                .host(host)
                .port(port)
                .path(path);

        if (paramValues != null && !paramValues.isEmpty()) {
            for (Long value : paramValues) {
                ub = ub.queryParam(paramName, value);
            }
        }

        return ub.build();
    }

    private RequestEntity<Void> putWithQueryParams(String scheme, String host, int port, String path, String paramName, List<Long> paramValues) {
        URI uri = uriWithQueryParams(scheme, host, port, path, paramName, paramValues);
        var peticion = RequestEntity.put(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
        return peticion;
    }

    private RequestEntity<Void> deleteWithQueryParams(String scheme, String host, int port, String path, String paramName, List<Long> paramValues) {
        URI uri = uriWithQueryParams(scheme, host, port, path, paramName, paramValues);
        var peticion = RequestEntity.delete(uri)
                .build();
        return peticion;
    }

    private EventoDTO crearEventoDTO(String nombre) {
        Timestamp ahora = Timestamp.from(Instant.now());
        Timestamp despues = Timestamp.from(Instant.now().plusSeconds(3600));
        return EventoDTO.builder()
                .nombre(nombre)
                .inicio(ahora)
                .fin(despues)
                .build();
    }

    private AsistenteDTO crearAsistenteDTO(String dni, String nombre) {
        return AsistenteDTO.builder()
                .dni(dni)
                .nombre(nombre)
                .apellido1("Apellido1")
                .apellido2("Apellido2")
                .build();
    }

    @Nested
    @DisplayName("cuando no hay eventos")
    public class EventosVacios {

        @Test
        @DisplayName("devuelve la lista de eventos vacía")
        public void devuelveEventos() {
            var peticion = get("http", "localhost", port, "/eventos");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<Set<Evento>>() {
                    });

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).isEmpty();
        }

        @Test
        @DisplayName("devuelve 404 cuando se solicita un evento que no existe")
        public void devuelve404CuandoEventoNoExiste() {
            var peticion = get("http", "localhost", port, "/eventos/1");

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("permite crear un nuevo evento")
        public void creaEvento() {
            EventoDTO evento = crearEventoDTO("Evento de Prueba");
            var peticion = post("http", "localhost", port, "/eventos", evento);

            var respuesta = restTemplate.exchange(peticion, EventoDTO.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
            assertThat(respuesta.getBody().getNombre()).isEqualTo("Evento de Prueba");
            assertThat(respuesta.getBody().getId()).isNotNull();
        }

        @Test
        @DisplayName("devuelve 404 cuando se intenta actualizar un evento que no existe")
        public void devuelve404CuandoActualizaEventoInexistente() {
            EventoDTO evento = crearEventoDTO("Evento Actualizado");
            var peticion = put("http", "localhost", port, "/eventos/1", evento);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve 404 cuando se intenta borrar un evento que no existe")
        public void devuelve404CuandoBorraEventoInexistente() {
            var peticion = delete("http", "localhost", port, "/eventos/1");

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }
    }

    @Nested
    @DisplayName("cuando no hay asistentes")
    public class AsistentesVacios {

        @Test
        @DisplayName("devuelve la lista de asistentes vacía")
        public void devuelveAsistentes() {
            var peticion = get("http", "localhost", port, "/asistentes");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<AsistenteDTO>>() {
                    });

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).isEmpty();
        }

        @Test
        @DisplayName("devuelve 404 cuando se solicita un asistente que no existe")
        public void devuelve404CuandoAsistenteNoExiste() {
            var peticion = get("http", "localhost", port, "/asistentes/1");

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("permite crear un nuevo asistente")
        public void creaAsistente() {
            AsistenteDTO asistente = crearAsistenteDTO("12345678A", "Asistente Prueba");
            var peticion = post("http", "localhost", port, "/asistentes", asistente);

            var respuesta = restTemplate.exchange(peticion, AsistenteDTO.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
            assertThat(respuesta.getBody().getDni()).isEqualTo("12345678A");
            assertThat(respuesta.getBody().getNombre()).isEqualTo("Asistente Prueba");
            assertThat(respuesta.getBody().getId()).isNotNull();
        }

        @Test
        @DisplayName("devuelve 404 cuando se intenta actualizar un asistente que no existe")
        public void devuelve404CuandoActualizaAsistenteInexistente() {
            AsistenteDTO asistente = crearAsistenteDTO("12345678A", "Asistente Actualizado");
            var peticion = put("http", "localhost", port, "/asistentes/1", asistente);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve 404 cuando se intenta borrar un asistente que no existe")
        public void devuelve404CuandoBorraAsistenteInexistente() {
            var peticion = delete("http", "localhost", port, "/asistentes/1");

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }
    }

    @Nested
    @DisplayName("con eventos existentes")
    public class ConEventos {

        private EventoDTO eventoCreado;

        @BeforeEach
        public void crearEvento() {
            EventoDTO evento = crearEventoDTO("Evento Existente");
            var peticion = post("http", "localhost", port, "/eventos", evento);
            eventoCreado = restTemplate.exchange(peticion, EventoDTO.class).getBody();
        }

        @Test
        @DisplayName("devuelve la lista de eventos con el evento creado")
        public void devuelveEventosConEvento() {
            var peticion = get("http", "localhost", port, "/eventos");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<EventoDTO>>() {
                    });

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(1);
            assertThat(respuesta.getBody().get(0).getNombre()).isEqualTo("Evento Existente");
        }

        @Test
        @DisplayName("devuelve un evento específico")
        public void devuelveEvento() {
            var peticion = get("http", "localhost", port, "/eventos/" + eventoCreado.getId());

            var respuesta = restTemplate.exchange(peticion, EventoDTO.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody().getNombre()).isEqualTo("Evento Existente");
            assertThat(respuesta.getBody().getId()).isEqualTo(eventoCreado.getId());
        }

        @Test
        @DisplayName("actualiza un evento existente")
        public void actualizaEvento() {
            EventoDTO eventoActualizado = crearEventoDTO("Evento Modificado");
            var peticion = put("http", "localhost", port, "/eventos/" + eventoCreado.getId(), eventoActualizado);

            var respuesta = restTemplate.exchange(peticion, Evento.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody().getNombre()).isEqualTo("Evento Modificado");
        }

        @Test
        @DisplayName("borra un evento existente")
        public void borraEvento() {
            var peticion = delete("http", "localhost", port, "/eventos/" + eventoCreado.getId());

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

            // Verificar que el evento ya no existe
            var peticionGet = get("http", "localhost", port, "/eventos/" + eventoCreado.getId());
            var respuestaGet = restTemplate.exchange(peticionGet, Void.class);
            assertThat(respuestaGet.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve error 403 al crear evento con nombre duplicado")
        public void errorCrearEventoDuplicado() {
            EventoDTO eventoDuplicado = crearEventoDTO("Evento Existente");
            var peticion = post("http", "localhost", port, "/eventos", eventoDuplicado);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
        }

        @Test
        @DisplayName("devuelve error 403 al actualizar evento con nombre duplicado")
        public void errorActualizarEventoNombreDuplicado() {
            // Crear otro evento primero
            EventoDTO otroEvento = crearEventoDTO("Otro Evento");
            var peticionPost = post("http", "localhost", port, "/eventos", otroEvento);
            EventoDTO otroEventoCreado = restTemplate.exchange(peticionPost, EventoDTO.class).getBody();

            // Intentar actualizar este segundo evento con el nombre del primero
            EventoDTO eventoActualizado = crearEventoDTO("Evento Existente");
            var peticionPut = put("http", "localhost", port, "/eventos/" + otroEventoCreado.getId(), eventoActualizado);

            var respuesta = restTemplate.exchange(peticionPut, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
        }
    }

    @Nested
    @DisplayName("con asistentes existentes")
    public class ConAsistentes {

        private AsistenteDTO asistenteCreado;

        @BeforeEach
        public void crearAsistente() {
            AsistenteDTO asistente = crearAsistenteDTO("12345678A", "Asistente Existente");
            var peticion = post("http", "localhost", port, "/asistentes", asistente);
            asistenteCreado = restTemplate.exchange(peticion, AsistenteDTO.class).getBody();
        }

        @Test
        @DisplayName("devuelve la lista de asistentes con el asistente creado")
        public void devuelveAsistentesConAsistente() {
            var peticion = get("http", "localhost", port, "/asistentes");

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<AsistenteDTO>>() {
                    });

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(1);
            assertThat(respuesta.getBody().get(0).getDni()).isEqualTo("12345678A");
        }

        @Test
        @DisplayName("devuelve un asistente específico")
        public void devuelveAsistente() {
            var peticion = get("http", "localhost", port, "/asistentes/" + asistenteCreado.getId());

            var respuesta = restTemplate.exchange(peticion, AsistenteDTO.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody().getDni()).isEqualTo("12345678A");
            assertThat(respuesta.getBody().getNombre()).isEqualTo("Asistente Existente");
        }

        @Test
        @DisplayName("actualiza un asistente existente")
        public void actualizaAsistente() {
            AsistenteDTO asistenteActualizado = crearAsistenteDTO("87654321B", "Asistente Modificado");
            var peticion = put("http", "localhost", port, "/asistentes/" + asistenteCreado.getId(), asistenteActualizado);

            var respuesta = restTemplate.exchange(peticion, AsistenteDTO.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody().getDni()).isEqualTo("87654321B");
            assertThat(respuesta.getBody().getNombre()).isEqualTo("Asistente Modificado");
        }

        @Test
        @DisplayName("borra un asistente existente")
        public void borraAsistente() {
            var peticion = delete("http", "localhost", port, "/asistentes/" + asistenteCreado.getId());

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);

            // Verificar que el asistente ya no existe
            var peticionGet = get("http", "localhost", port, "/asistentes/" + asistenteCreado.getId());
            var respuestaGet = restTemplate.exchange(peticionGet, Void.class);
            assertThat(respuestaGet.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve error 403 al crear asistente con DNI duplicado")
        public void errorCrearAsistenteDuplicado() {
            AsistenteDTO asistenteDuplicado = crearAsistenteDTO("12345678A", "Otro Asistente");
            var peticion = post("http", "localhost", port, "/asistentes", asistenteDuplicado);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
        }

        @Test
        @DisplayName("devuelve error 403 al actualizar asistente con DNI duplicado")
        public void errorActualizarAsistenteDniDuplicado() {
            // Crear otro asistente primero
            AsistenteDTO otroAsistente = crearAsistenteDTO("87654321B", "Otro Asistente");
            var peticionPost = post("http", "localhost", port, "/asistentes", otroAsistente);
            AsistenteDTO otroAsistenteCreado = restTemplate.exchange(peticionPost, AsistenteDTO.class).getBody();

            // Intentar actualizar este segundo asistente con el DNI del primero
            AsistenteDTO asistenteActualizado = crearAsistenteDTO("12345678A", "Asistente Modificado");
            var peticionPut = put("http", "localhost", port, "/asistentes/" + otroAsistenteCreado.getId(), asistenteActualizado);

            var respuesta = restTemplate.exchange(peticionPut, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(403);
        }
    }

    @Nested
    @DisplayName("con eventos y asistentes")
    public class ConEventosYAsistentes {

        private EventoDTO eventoCreado;
        private AsistenteDTO asistenteCreado1;
        private AsistenteDTO asistenteCreado2;

        @BeforeEach
        public void crearEventosYAsistentes() {
            // Crear evento
            EventoDTO evento = crearEventoDTO("Evento Con Asistentes");
            var peticionEvento = post("http", "localhost", port, "/eventos", evento);
            eventoCreado = restTemplate.exchange(peticionEvento, EventoDTO.class).getBody();

            // Crear asistentes
            AsistenteDTO asistente1 = crearAsistenteDTO("12345678A", "Asistente 1");
            var peticionAsistente1 = post("http", "localhost", port, "/asistentes", asistente1);
            asistenteCreado1 = restTemplate.exchange(peticionAsistente1, AsistenteDTO.class).getBody();

            AsistenteDTO asistente2 = crearAsistenteDTO("87654321B", "Asistente 2");
            var peticionAsistente2 = post("http", "localhost", port, "/asistentes", asistente2);
            asistenteCreado2 = restTemplate.exchange(peticionAsistente2, AsistenteDTO.class).getBody();
        }

        @Test
        @DisplayName("asocia asistentes a un evento")
        public void asociaAsistentesAEvento() {
            List<Long> idAsistentes = new ArrayList<>();
            idAsistentes.add(asistenteCreado1.getId());
            idAsistentes.add(asistenteCreado2.getId());

            var peticion = putWithQueryParams("http", "localhost", port,
                    "/eventos/" + eventoCreado.getId() + "/asistentes", "idAsistentes", idAsistentes);

            var respuesta = restTemplate.exchange(peticion,
                    new ParameterizedTypeReference<List<AsistenteDTO>>() {});

            assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
            assertThat(respuesta.getBody()).hasSize(2);

            // Verificar que los asistentes están asociados al evento
            var peticionEvento = get("http", "localhost", port, "/eventos/" + eventoCreado.getId());
            var respuestaEvento = restTemplate.exchange(peticionEvento, EventoDTO.class);
            assertThat(respuestaEvento.getBody().getAsistentes()).hasSize(2);
        }

        @Test
        @DisplayName("elimina asistentes de un evento")
        public void eliminaAsistentesDeEvento() {
            // Primero asocia asistentes
            List<Long> idAsistentes = new ArrayList<>();
            idAsistentes.add(asistenteCreado1.getId());
            idAsistentes.add(asistenteCreado2.getId());

            var peticionAsociar = putWithQueryParams("http", "localhost", port,
                    "/eventos/" + eventoCreado.getId() + "/asistentes", "idAsistentes", idAsistentes);
            restTemplate.exchange(peticionAsociar, Void.class);

            // Luego elimina un asistente
            List<Long> idAsistentesEliminar = new ArrayList<>();
            idAsistentesEliminar.add(asistenteCreado1.getId());

            var peticionEliminar = deleteWithQueryParams("http", "localhost", port,
                    "/eventos/" + eventoCreado.getId() + "/asistentes", "idAsistentes", idAsistentesEliminar);

            var respuestaEliminar = restTemplate.exchange(peticionEliminar, Void.class);

            assertThat(respuestaEliminar.getStatusCode().value()).isEqualTo(200);

            // Verificar que solo queda un asistente asociado
            var peticionEvento = get("http", "localhost", port, "/eventos/" + eventoCreado.getId());
            var respuestaEvento = restTemplate.exchange(peticionEvento, EventoDTO.class);
            assertThat(respuestaEvento.getBody().getAsistentes()).hasSize(1);
            assertThat(respuestaEvento.getBody().getAsistentes().get(0).getId()).isEqualTo(asistenteCreado2.getId());
        }

        @Test
        @DisplayName("devuelve 404 al asociar asistentes a un evento inexistente")
        public void error404AsociarAsistentesEventoInexistente() {
            List<Long> idAsistentes = new ArrayList<>();
            idAsistentes.add(asistenteCreado1.getId());

            var peticion = putWithQueryParams("http", "localhost", port,
                    "/eventos/999/asistentes", "idAsistentes", idAsistentes);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve 404 al asociar asistentes inexistentes a un evento")
        public void error404AsociarAsistentesInexistentes() {
            List<Long> idAsistentes = new ArrayList<>();
            idAsistentes.add(999L);

            var peticion = putWithQueryParams("http", "localhost", port,
                    "/eventos/" + eventoCreado.getId() + "/asistentes", "idAsistentes", idAsistentes);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve 404 al eliminar asistentes de un evento inexistente")
        public void error404EliminarAsistentesEventoInexistente() {
            List<Long> idAsistentes = new ArrayList<>();
            idAsistentes.add(asistenteCreado1.getId());

            var peticion = deleteWithQueryParams("http", "localhost", port,
                    "/eventos/999/asistentes", "idAsistentes", idAsistentes);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("devuelve 404 al eliminar asistentes inexistentes de un evento")
        public void error404EliminarAsistentesInexistentes() {
            List<Long> idAsistentes = new ArrayList<>();
            idAsistentes.add(999L);

            var peticion = deleteWithQueryParams("http", "localhost", port,
                    "/eventos/" + eventoCreado.getId() + "/asistentes", "idAsistentes", idAsistentes);

            var respuesta = restTemplate.exchange(peticion, Void.class);

            assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
        }

        @Test
        @DisplayName("al eliminar un evento se eliminan sus asociaciones con asistentes")
        public void eliminarEventoEliminaAsociaciones() {
            // Primero asocia asistentes al evento
            List<Long> idAsistentes = new ArrayList<>();
            idAsistentes.add(asistenteCreado1.getId());

            var peticionAsociar = putWithQueryParams("http", "localhost", port,
                    "/eventos/" + eventoCreado.getId() + "/asistentes", "idAsistentes", idAsistentes);
            restTemplate.exchange(peticionAsociar, Void.class);

            // Eliminar el evento
            var peticionEliminar = delete("http", "localhost", port, "/eventos/" + eventoCreado.getId());
            restTemplate.exchange(peticionEliminar, Void.class);

            // Verificar que el asistente sigue existiendo
            var peticionAsistente = get("http", "localhost", port, "/asistentes/" + asistenteCreado1.getId());
            var respuestaAsistente = restTemplate.exchange(peticionAsistente, AsistenteDTO.class);

            assertThat(respuestaAsistente.getStatusCode().value()).isEqualTo(200);
            assertThat(respuestaAsistente.getBody().getId()).isEqualTo(asistenteCreado1.getId());
        }
    }
}