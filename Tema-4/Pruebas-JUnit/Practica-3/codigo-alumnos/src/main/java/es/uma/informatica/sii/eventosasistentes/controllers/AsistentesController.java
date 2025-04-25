package es.uma.informatica.sii.eventosasistentes.controllers;

import es.uma.informatica.sii.eventosasistentes.dtos.AsistenteDTO;
import es.uma.informatica.sii.eventosasistentes.exceptions.ElementoNoExisteException;
import es.uma.informatica.sii.eventosasistentes.exceptions.ElementoYaExistenteException;
import es.uma.informatica.sii.eventosasistentes.services.LogicaEventos;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/asistentes")
public class AsistentesController {

    private LogicaEventos logicaEventos;

    public AsistentesController(LogicaEventos logicaEventos) {
        this.logicaEventos = logicaEventos;
    }

    @GetMapping
    public List<AsistenteDTO> getAsistentes() {
        return logicaEventos.getAsistente().stream()
            .map(Mapper::toDTO)
            .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<AsistenteDTO> addAsistente(@RequestBody AsistenteDTO asistente, UriComponentsBuilder uriBuilder) {
        var asistenteEntity = Mapper.toEntity(asistente);
        asistenteEntity = logicaEventos.addAsistente(asistenteEntity);
        return ResponseEntity.created(uriBuilder
                .path("/asistentes/{id}")
                .buildAndExpand(asistenteEntity.getId())
                .toUri())
            .body(Mapper.toDTO(asistenteEntity));
    }

    @GetMapping("/{id}")
    public AsistenteDTO getAsistente(@PathVariable Long id) {
        return Mapper.toDTO(logicaEventos.getAsistente(id));
    }


    @PutMapping("/{id}")
    public AsistenteDTO updateAsistente(@PathVariable Long id, @RequestBody AsistenteDTO asistente) {
        var asistenteEntity = Mapper.toEntity(asistente);
        asistenteEntity.setId(id);
        asistenteEntity = logicaEventos.updateAsistente(asistenteEntity);
        return Mapper.toDTO(asistenteEntity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAsistente(@PathVariable Long id) {
        logicaEventos.deleteAsistente(id);
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
