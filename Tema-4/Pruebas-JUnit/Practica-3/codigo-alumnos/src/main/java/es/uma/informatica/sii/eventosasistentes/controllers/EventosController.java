package es.uma.informatica.sii.eventosasistentes.controllers;

import es.uma.informatica.sii.eventosasistentes.dtos.AsistenteDTO;
import es.uma.informatica.sii.eventosasistentes.dtos.EventoDTO;
import es.uma.informatica.sii.eventosasistentes.entities.Evento;
import es.uma.informatica.sii.eventosasistentes.exceptions.ElementoNoExisteException;
import es.uma.informatica.sii.eventosasistentes.exceptions.ElementoYaExistenteException;
import es.uma.informatica.sii.eventosasistentes.repositories.EventoRepo;
import es.uma.informatica.sii.eventosasistentes.services.LogicaEventos;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/eventos")
public class EventosController {

    private final EventoRepo eventoRepo;
    private LogicaEventos logicaEventos;

    public EventosController(LogicaEventos logicaEventos, EventoRepo eventoRepo) {
        this.logicaEventos = logicaEventos;
        this.eventoRepo = eventoRepo;
    }

    @GetMapping
    public List<EventoDTO> getEventos() {
        return logicaEventos.getEventos().stream()
            .map(Mapper::toDTO)
            .collect(Collectors.toList());
    }

    @PostMapping()
    public ResponseEntity<EventoDTO> addEvento(@RequestBody EventoDTO evento, UriComponentsBuilder uriBuilder) {
        var eventoEntity = Mapper.toEntity(evento);
        eventoEntity.setId(null);
        eventoEntity = logicaEventos.addEvento(eventoEntity);
        return ResponseEntity.created(uriBuilder
                .path("/eventos/{id}")
                .buildAndExpand(eventoEntity.getId())
                .toUri())
            .body(Mapper.toDTO(eventoEntity));
    }

    @GetMapping("/{id}")
    public EventoDTO getEvento(@PathVariable Long id) {
        return Mapper.toDTO(logicaEventos.getEvento(id));
    }

    @PutMapping("/{id}")
    public Evento updateEvento(@PathVariable Long id, @RequestBody EventoDTO evento) {
        var eventoEntity = Mapper.toEntity(evento);
        eventoEntity.setId(id);
        return logicaEventos.updateEvento(eventoEntity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvento(@PathVariable Long id) {
        logicaEventos.deleteEvento(id);
    }

    @PutMapping("/{idEvento}/asistentes")
    public List<AsistenteDTO> updateEvento(@PathVariable Long idEvento, @RequestParam List<Long> idAsistentes) {
        idAsistentes.stream()
            .forEach(idAsistente -> logicaEventos.asociarEventoAsistente(idEvento, idAsistente));
        return Mapper.mapNullable(logicaEventos.getEvento(idEvento).getAsistentes(), Mapper::toDTO);
    }

    @DeleteMapping("/{idEvento}/asistentes")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvento(@PathVariable Long idEvento, @RequestParam List<Long> idAsistentes) {
        idAsistentes.stream()
            .forEach(idAsistente -> logicaEventos.desAsociarEventoAsistente(idEvento, idAsistente));
    }


    @ExceptionHandler(ElementoNoExisteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleElementoNoExisteException() {
    }

    @ExceptionHandler({ElementoYaExistenteException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleElementoYaExistenteException() {
    }







}
