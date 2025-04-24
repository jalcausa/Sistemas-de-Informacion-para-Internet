package es.uma.informatica.sii.helloworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uma.informatica.sii.helloworld.entities.ListaCompra;

@Repository
public interface xListaCompraRepository extends JpaRepository<ListaCompra, Long> {

}
