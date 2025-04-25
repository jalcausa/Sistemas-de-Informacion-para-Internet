package es.uma.informatica.sii.spring.jpa.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.uma.informatica.sii.spring.jpa.demo.entities.Book;
import es.uma.informatica.sii.spring.jpa.demo.repositories.BookRepository;

@Component
public class LineaComandos implements CommandLineRunner {
	private BookRepository repository;
	public LineaComandos(BookRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {

		for (String s: args) {
			System.out.println(s);
		}

		if (args.length > 0) {
			for (Book b: repository.findByNombre(args[0])) {
				System.out.println(b);
			}
		}
	}

}
