package es.uma.informatica.sii.spring.jpa.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.uma.informatica.sii.spring.jpa.demo.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
	List<Book> findByNombre(String title);
	List<Book> findByIsbnAndNombreOrderByNombreAsc(String isbn, String nombre);
	
	@Query("select b from Book b where b.nombre = :nombre")
	List<Book> miConsultaCompleja(@Param("nombre") String nombre);
}
