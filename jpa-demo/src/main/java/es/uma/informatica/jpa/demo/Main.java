package es.uma.informatica.jpa.demo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

// Para compilar desde línea de comandos:
// mvn exec:java -Dexec.mainClass="es.uma.informatica.jpa.demo.Main"
public class Main {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa.demo");
		EntityManager em = emf.createEntityManager();
		
		em.close();
		emf.close();

	}

}
