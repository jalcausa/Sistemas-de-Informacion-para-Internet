package es.uma.informatica.sii.pr2025rest.controladores;

import es.uma.informatica.sii.pr2025rest.dtos.ExpedicionDTO;
import es.uma.informatica.sii.pr2025rest.dtos.ExpedicionEntradaDTO;
import es.uma.informatica.sii.pr2025rest.entidades.Expedicion;
import es.uma.informatica.sii.pr2025rest.excepciones.EntidadNoExisteException;
import es.uma.informatica.sii.pr2025rest.excepciones.ExpedicionConEquiposException;
import es.uma.informatica.sii.pr2025rest.servicios.ExpedicionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expediciones")
public class ExpedicionController {

    private final ExpedicionService expedicionService;

    @Autowired
    public ExpedicionController(ExpedicionService expedicionService) {
        this.expedicionService = expedicionService;
    }
    // TODO

    @GetMapping
    public List<ExpedicionDTO> getExpediciones() {
        return expedicionService.obtenerExpediciones().stream()
                .map(Mapper::dto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createExpedicion(@RequestBody ExpedicionEntradaDTO expedicionDTO,
                                              UriComponentsBuilder builder) {
        Expedicion expedicion = Mapper.entidad(expedicionDTO);
        try {
            expedicion = expedicionService.anadirExpedicion(expedicion);
            URI uri = builder
                    .path("/expediciones")
                    .path(String.format("/%d",expedicion.getId()))
                    .build()
                    .toUri();
            return ResponseEntity.created(uri).body((Mapper.dto(expedicion)));
        } catch (ExpedicionConEquiposException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ExpedicionDTO> getExpedicionById(@PathVariable("id") Long id) {
        Optional<Expedicion> expedicionOpt = expedicionService.obtenerExpedicion(id);

        // Si el Optional tiene valor, mapeamos a ExpedicionDTO y devolvemos 200 OK.
        // Si está vacío, devolvemos 404 Not Found.
        return expedicionOpt
                .map(expedicion -> ResponseEntity.ok(Mapper.dto(expedicion)))  // Si existe, devolvemos 200 OK con el DTO.
                .orElseGet(() -> ResponseEntity.notFound().build());  // Si no existe, devolvemos 404 Not Found.
    }

    @PutMapping("{id}")
    public ResponseEntity<?> changeExpedicion(@RequestBody ExpedicionEntradaDTO expedicionDTO, @PathVariable("id") Long id) {
        Expedicion expedicion = Mapper.entidad(expedicionDTO);
        expedicion.setId(id);
        try{
            expedicion = expedicionService.actualizarExpedicion(expedicion);
            return ResponseEntity.ok(Mapper.dto(expedicion));
        }catch (ExpedicionConEquiposException | EntidadNoExisteException e)  {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteExpedicion(@PathVariable("id") Long id) {
        try {
            expedicionService.eliminarExpedicion(id);
            return ResponseEntity.noContent().build();
        } catch (EntidadNoExisteException e) {
            return ResponseEntity.notFound().build();
        } catch (ExpedicionConEquiposException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}