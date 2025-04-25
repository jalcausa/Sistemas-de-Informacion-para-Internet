package es.uma.informatica.sii.eventosasistentes.repositories;

import es.uma.informatica.sii.eventosasistentes.entities.Asistente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsistenteRepo extends JpaRepository<Asistente, Long>{
    Optional<Asistente> findByDni(String dni);

}
