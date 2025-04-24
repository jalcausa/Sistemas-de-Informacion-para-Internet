package es.uma.informatica.sii.pr2025rest.controladores;

import es.uma.informatica.sii.pr2025rest.dtos.EquipoDTO;
import es.uma.informatica.sii.pr2025rest.entidades.Equipo;
import es.uma.informatica.sii.pr2025rest.excepciones.EntidadNoExisteException;
import es.uma.informatica.sii.pr2025rest.excepciones.ExpedicionConEquiposException;
import es.uma.informatica.sii.pr2025rest.servicios.EquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/equipos")
public class EquipoController {

    private final EquipoService equipoService;

    @Autowired
    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @GetMapping
    public List<EquipoDTO> obtenerTodos() {
        return equipoService.obtenerTodos().stream()
            .map(Mapper::dto)
            .collect(Collectors.toList());
    }
    // TODO

    @PostMapping
    public ResponseEntity<EquipoDTO> crearEquipo(@RequestBody EquipoDTO equipoDTO,
                                 UriComponentsBuilder builder) {
        Equipo equipo = Mapper.entidad(equipoDTO);
        equipo = equipoService.anadirEquipo(equipo);
        URI uri = builder
                .path("/equipos")
                .path(String.format("/%d",equipo.getId()))
                .build()
                .toUri();
        return ResponseEntity.created(uri).body((Mapper.dto(equipo)));
    }

    @GetMapping("{id}")
    public ResponseEntity<EquipoDTO> getEquipo(@PathVariable Long id) {
        Optional<Equipo> equipoOpt= equipoService.obtenerEquipo(id);
        return equipoOpt
                .map(equipo -> ResponseEntity.ok(Mapper.dto(equipo)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<EquipoDTO> changeEquipo(@RequestBody EquipoDTO equipoDTO, @PathVariable("id") Long id) {
        Equipo equipo = Mapper.entidad(equipoDTO);
        equipo.setId(id);
        try {
            equipo = equipoService.actualizarEquipo(equipo);
            return ResponseEntity.ok(Mapper.dto(equipo));
        } catch (EntidadNoExisteException e)  {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> eliminarEquipo(@PathVariable Long id) {
        try {
            equipoService.eliminarEquipo(id);
            return ResponseEntity.noContent().build();
        } catch (EntidadNoExisteException e) {
            return ResponseEntity.notFound().build();
        }
    }
}