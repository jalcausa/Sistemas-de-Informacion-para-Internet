package es.uma.informatica.sii.pr2025rest.servicios;


import es.uma.informatica.sii.pr2025rest.entidades.Equipo;
import es.uma.informatica.sii.pr2025rest.entidades.Expedicion;
import es.uma.informatica.sii.pr2025rest.excepciones.EntidadNoExisteException;
import es.uma.informatica.sii.pr2025rest.repositorios.EquipoRepository;
import es.uma.informatica.sii.pr2025rest.repositorios.ExpedicionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EquipoService {
    private final EquipoRepository equipoRepository;
    private final ExpedicionRepository expedicionRepository;

    public EquipoService(EquipoRepository equipoRepository, ExpedicionRepository expedicionRepository) {
        this.equipoRepository = equipoRepository;
        this.expedicionRepository = expedicionRepository;
    }

    public List<Equipo> obtenerTodos() {
        return equipoRepository.findAll();
    }

    // TODO

    public Equipo anadirEquipo(Equipo equipo) {
        equipo.setId(null);
        return equipoRepository.save(equipo);
    }

    public Optional<Equipo> obtenerEquipo(Long id) {
        return equipoRepository.findById(id);
    }

    public Equipo actualizarEquipo(Equipo equipo) {
        if (!equipoRepository.existsById(equipo.getId()))
            throw new EntidadNoExisteException();
        return equipoRepository.save(equipo);
    }

    public void eliminarEquipo(Long id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(EntidadNoExisteException::new);

        List<Expedicion> todasExpediciones = expedicionRepository.findAll();
        List<Expedicion> expedicionesConEquipo = todasExpediciones.stream()
                .filter(e -> e.getEquipos().contains(equipo))
                .collect(Collectors.toList());

        expedicionesConEquipo.forEach(expedicion -> expedicion.getEquipos().remove(equipo));

        expedicionRepository.saveAll(expedicionesConEquipo);

        equipoRepository.delete(equipo);
    }
}