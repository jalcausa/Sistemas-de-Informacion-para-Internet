package es.uma.informatica.sii.practica.jpa;

import java.io.Closeable;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Esta clase servirá para centralizar todas las operaciones relacionadas con la 
 * gestión de los datos.
 * Recuerde que las operaciones que modifiquen la base de datos deben ejecutarse dentro
 * de una transacción.
 * @author francis
 *
 */
public class AccesoDatos implements Closeable {
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	/**
	 * Constructor por defecto. Crea un contexto de persistencia.
	 */
	public AccesoDatos() {
		emf = Persistence.createEntityManagerFactory("p3a-jpa");
		em = emf.createEntityManager();
	}
	
	/**
	 * Cierra el contexto de persistencia.
	 */
	@Override
	public void close() {
		em.close();
		emf.close();
	}	
}
