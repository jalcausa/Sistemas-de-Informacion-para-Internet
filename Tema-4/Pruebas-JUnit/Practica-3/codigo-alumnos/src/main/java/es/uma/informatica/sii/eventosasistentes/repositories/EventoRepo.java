package es.uma.informatica.sii.eventosasistentes.repositories;

import es.uma.informatica.sii.eventosasistentes.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventoRepo extends JpaRepository<Evento, Long> {
    Optional<Evento> findByNombre(String nombre);
}
