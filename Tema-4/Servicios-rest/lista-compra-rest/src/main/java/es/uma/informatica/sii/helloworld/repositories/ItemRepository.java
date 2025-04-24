package es.uma.informatica.sii.helloworld.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.uma.informatica.sii.helloworld.entities.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

}
