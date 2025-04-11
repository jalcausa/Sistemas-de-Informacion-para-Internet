package es.uma.informatica.sii.practica2.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import es.uma.informatica.sii.practica2.entidades.Contacto;
import es.uma.informatica.sii.practica2.repositorios.ContactoRepo;
import es.uma.informatica.sii.practica2.servicios.excepciones.ContactoNoEncontrado;
import io.swagger.v3.oas.models.info.Contact;

@Service
@Transactional
public class LogicaContactos {
	
	private ContactoRepo repo;
	
	@Autowired
	public LogicaContactos(ContactoRepo repo) {
		this.repo=repo;
	}
	
	public List<Contacto> getTodosContactos() {
		return repo.findAll();
	}

	public Contacto insertarContacto(Contacto contacto) {
		contacto.setId(null); // De esta forma nos aseguramos de que inserta el contacto con un nuevo ID correcto
		return repo.save(contacto);
	}

	public Optional<Contacto> obtenerContacto(Long id) {
		// Opción 1 (si devolvemos Contacto)
	/* 	Optional<Contacto> contacto = repo.findById(id);
		if (contacto.isPresent())
			return contacto.get();
		else
			throw new ContactoNoEncontrado(); */
		
		// Opción 2 (si devolvemos Optional)
		return repo.findById(id);
	}

	public void eliminarContacto(Long id) {
		if (repo.existsById(id))
			repo.deleteById(id);
		else
			throw new ContactoNoEncontrado();
	}

	@PutMapping
	public Contacto actualizarContacto(Contacto contacto) {
		if (repo.existsById(contacto.getId()))
			return repo.save(contacto);
		else 
			throw new ContactoNoEncontrado();
	}
	
}
