package es.uma.informatica.sii.contactos.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uma.informatica.sii.contactos.entidades.Contacto;

@Repository
public interface ContactoRepo extends JpaRepository<Contacto, Long> {

}
