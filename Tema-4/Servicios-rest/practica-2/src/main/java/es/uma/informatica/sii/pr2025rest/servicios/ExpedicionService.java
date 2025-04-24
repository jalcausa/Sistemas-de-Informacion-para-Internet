package es.uma.informatica.sii.pr2025rest.servicios;

import es.uma.informatica.sii.pr2025rest.dtos.ExpedicionDTO;
import es.uma.informatica.sii.pr2025rest.entidades.Equipo;
import es.uma.informatica.sii.pr2025rest.entidades.Expedicion;
import es.uma.informatica.sii.pr2025rest.excepciones.EntidadNoExisteException;
import es.uma.informatica.sii.pr2025rest.excepciones.ExpedicionConEquiposException;
import es.uma.informatica.sii.pr2025rest.repositorios.EquipoRepository;
import es.uma.informatica.sii.pr2025rest.repositorios.ExpedicionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExpedicionService {

    private final ExpedicionRepository expedicionRepository;
    private final EquipoRepository equipoRepository;

    public ExpedicionService(ExpedicionRepository expedicionRepository, EquipoRepository equipoRepository) {
        this.expedicionRepository = expedicionRepository;
        this.equipoRepository = equipoRepository;
    }

    // TODO
    public List<Expedicion> obtenerExpediciones(){
        return expedicionRepository.findAll();
    }

    /*
    * NO USAR ESTO:
    * equipoRepository.findAll().containsAll(expedicion.getEquipos())
    * Falla ya que compara referencias a objetos y pueden no coincidir
    * si son instancias nuevas (no persistidas) que ocurre en el DTO creo
    * */
    public Expedicion anadirExpedicion(Expedicion expedicion){
        expedicion.setId(null);
        if (expedicion.getEquipos() != null) {
            for (Equipo equipo : expedicion.getEquipos()) {
                if (!equipoRepository.existsById(equipo.getId())) {
                    throw new ExpedicionConEquiposException();
                }
            }
        }
        return expedicionRepository.save(expedicion);
    }

    public Optional<Expedicion> obtenerExpedicion(Long id){
        return expedicionRepository.findById(id);
    }

    public Expedicion actualizarExpedicion(Expedicion expedicion){
        if (!expedicionRepository.existsById(expedicion.getId()))
            throw new EntidadNoExisteException();
        if (expedicion.getEquipos() != null) {
            for (Equipo equipo : expedicion.getEquipos()) {
                if (!equipoRepository.existsById(equipo.getId())) {
                    throw new ExpedicionConEquiposException();
                }
            }
        }
        return expedicionRepository.save(expedicion);
    }

    public void eliminarExpedicion(Long id) {
        if (!expedicionRepository.existsById(id))
            throw new EntidadNoExisteException();
        Expedicion expedicion = expedicionRepository.findById(id).get();
        if (expedicion.getEquipos() != null && !expedicion.getEquipos().isEmpty())
            throw new ExpedicionConEquiposException();
        expedicionRepository.delete(expedicion);
    }
}