package es.uma.informatica.sii.practica2.servicios;

import es.uma.informatica.sii.practica2.entidades.Proyecto;
import es.uma.informatica.sii.practica2.servicios.excepciones.ProyectoNoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uma.informatica.sii.practica2.repositorios.ProyectoRepo;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LogicaProyectos {
	
	private ProyectoRepo repo;
	
	@Autowired
	public LogicaProyectos(ProyectoRepo repo) {
		this.repo=repo;
	}
	
	//TODO Método getTodosProyectos
	public List<Proyecto> getTodosProyectos() {
		return repo.findAll();
	}

	//TODO Método anadirProyecto
	public Proyecto anadirProyecto(Proyecto proyecto){
		proyecto.setId(null);
		return repo.save(proyecto);
	}


	//TODO Método getProyectoPorId
	public Optional<Proyecto> getProyectoPorId(Long id) {
		return repo.findById(id);
	}

	public void modificarProyecto(Proyecto proyecto) {
		//TODO
		//Debe lanzar throw new ProyectoNoEncontrado(); si el proyecto no existe
		if (repo.existsById(proyecto.getId())) {
			repo.save(proyecto);
		} else {
			throw new ProyectoNoEncontrado();
		}
	}
	public void eliminarProyecto(Long id) {
		//TODO
		//Debe lanzar throw new ProyectoNoEncontrado(); si el proyecto no existe
		if (repo.existsById(id)) {
			repo.deleteById(id);
		} else {
			throw new ProyectoNoEncontrado();
		}
	}
}
