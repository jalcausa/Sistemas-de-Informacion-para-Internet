package es.uma.informatica.sii.pr2025rest.repositorios;

import es.uma.informatica.sii.pr2025rest.entidades.Equipo;
import es.uma.informatica.sii.pr2025rest.entidades.Expedicion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpedicionRepository extends JpaRepository<Expedicion, Long> {
}