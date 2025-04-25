package es.uma.informatica.sii.eventosasistentes.services;

import es.uma.informatica.sii.eventosasistentes.entities.Asistente;
import es.uma.informatica.sii.eventosasistentes.entities.Evento;
import es.uma.informatica.sii.eventosasistentes.exceptions.ElementoNoExisteException;
import es.uma.informatica.sii.eventosasistentes.exceptions.ElementoYaExistenteException;
import es.uma.informatica.sii.eventosasistentes.repositories.AsistenteRepo;
import es.uma.informatica.sii.eventosasistentes.repositories.EventoRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LogicaEventos {
    private EventoRepo eventoRepo;
    private AsistenteRepo asistenteRepo;

    public LogicaEventos(EventoRepo eventoRepo, AsistenteRepo asistenteRepo) {
        this.eventoRepo = eventoRepo;
        this.asistenteRepo = asistenteRepo;
    }

    public List<Evento> getEventos() {
        return eventoRepo.findAll();
    }

    public Evento addEvento(Evento evento) {
        evento.setId(null);
        evento.setAsistentes(Collections.EMPTY_SET);
        eventoRepo.findByNombre(evento.getNombre()).ifPresent(n -> {
            throw new ElementoYaExistenteException("Ya existe un evento con el mismo nommbre");
        });
        return eventoRepo.save(evento);
    }

    public Evento getEvento(Long id) {
        var evento = eventoRepo.findById(id);
        if (evento.isEmpty()) {
            throw new ElementoNoExisteException("Evento no encontrado");
        } else {
            return evento.get();
        }
    }

    public Evento updateEvento(Evento evento) {
        if (eventoRepo.existsById(evento.getId())) {
            var opEvento = eventoRepo.findByNombre(evento.getNombre());
            if (opEvento.isPresent() && opEvento.get().getId() != evento.getId()) {
                throw new ElementoYaExistenteException("Ya existe un evento con el mismo nombre");
            }
            opEvento = eventoRepo.findById(evento.getId());
            opEvento.ifPresent(n -> {
                n.setNombre(evento.getNombre());
                n.setInicio(evento.getInicio());
            });
            return eventoRepo.save(opEvento.get());
        } else {
            throw new ElementoNoExisteException("Evento no encontrado");
        }
    }

    public void deleteEvento(Long id) {
        var evento = eventoRepo.findById(id);
        if (evento.isPresent()) {
           evento.get().getAsistentes().forEach(asistente -> {
                var aEliminar = asistente.getEventos().stream()
                    .filter(e -> e.getId().equals(id)).collect(Collectors.toSet());
                asistente.getEventos().removeAll(aEliminar);
            });
            eventoRepo.deleteById(id);
        } else {
            throw new ElementoNoExisteException("Evento no encontrado");
        }
    }

    public Asistente addAsistente(Asistente asistente) {
        asistente.setId(null);
        asistente.setEventos(Collections.EMPTY_SET);
        asistenteRepo.findByDni(asistente.getDni()).ifPresent(n -> {
            throw new ElementoYaExistenteException("Ya hay un asistente con el mismo DNI");
        });

        return asistenteRepo.save(asistente);
    }

    public void deleteAsistente(Long idAsistente) {
        if (asistenteRepo.existsById(idAsistente)) {
            asistenteRepo.deleteById(idAsistente);
        } else {
            throw new ElementoNoExisteException("Asistente no encontrado");
        }
    }

    public Asistente updateAsistente(Asistente asistente) {
        if (asistenteRepo.existsById(asistente.getId())) {
            var opAsistente = asistenteRepo.findByDni(asistente.getDni());
            if (opAsistente.isPresent() && opAsistente.get().getId() != asistente.getId()) {
                throw new ElementoYaExistenteException("Ya hay un asistente con el mismo DNI");
            }
            opAsistente = asistenteRepo.findById(asistente.getId());
            opAsistente.ifPresent(n -> {
                n.setDni(asistente.getDni());
                n.setApellido1(asistente.getApellido1());
                n.setApellido2(asistente.getApellido2());
                n.setNombre(asistente.getNombre());
            });
            return asistenteRepo.save(opAsistente.get());
        } else {
            throw new ElementoNoExisteException("Asistente no encontrado");
        }
    }

    public List<Asistente> getAsistente() {
        return asistenteRepo.findAll();
    }

    public Asistente getAsistente(Long idAsistente) {
        var asistente = asistenteRepo.findById(idAsistente);
        if (asistente.isEmpty()) {
            throw new ElementoNoExisteException("Asistente no encontrado");
        }
        return asistente.get();
    }

    public void asociarEventoAsistente(Long idEvento, Long idAsistente){
        var asistente = getAsistente(idAsistente);
        var evento = getEvento(idEvento);
        asistente.getEventos().add(evento);
    }

    public void desAsociarEventoAsistente(Long idEvento, Long idAsistente){
        var asistente = getAsistente(idAsistente);
        var evento = getEvento(idEvento);
        asistente.getEventos().remove(evento);
    }
}
