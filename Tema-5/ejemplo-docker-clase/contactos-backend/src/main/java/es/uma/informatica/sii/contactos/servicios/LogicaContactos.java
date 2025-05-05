package es.uma.informatica.sii.contactos.servicios;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uma.informatica.sii.contactos.entidades.Contacto;
import es.uma.informatica.sii.contactos.repositorios.ContactoRepo;
import es.uma.informatica.sii.contactos.servicios.excepciones.ContactoNoEncontrado;

@Service
@Transactional
public class LogicaContactos {
	private Logger logger = LoggerFactory.getLogger(LogicaContactos.class);
	private ContactoRepo repo;
	
	@Autowired
	public LogicaContactos(ContactoRepo repo) {
		this.repo=repo;
	}
	
	public List<Contacto> getTodosContactos() {
		logger.info("Pedidos todos los contactos");
		return repo.findAll();
	}
	
	public Contacto aniadirContacto(Contacto contacto) {
		logger.info("Añadido contacto");
		contacto.setId(null);
		return repo.save(contacto);
	}
	
	public Optional<Contacto> getContactoPorId(Long id) {
		logger.info("Pedido un contacto");
		return repo.findById(id);
	}
	
	public Contacto modificarContacto(Contacto contacto) {
		logger.info("Modificando contacto");
		if (repo.existsById(contacto.getId())) {
			return repo.save(contacto);
		} else {
			throw new ContactoNoEncontrado();
		}
	}
	
	public void eliminarContacto(Long id) {
		logger.info("Eliminando contacto");
		if (repo.existsById(id)) {
			repo.deleteById(id);
		} else {
			throw new ContactoNoEncontrado();
		}
	}
	
}
