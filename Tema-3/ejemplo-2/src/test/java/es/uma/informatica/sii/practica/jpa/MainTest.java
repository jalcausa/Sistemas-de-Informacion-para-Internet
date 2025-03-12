package es.uma.informatica.sii.practica.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

/**
 * A simple unit test
 */
public class MainTest 
{
    private AccesoDatos ad;

    @BeforeEach
    public void setup() {
        ad = new AccesoDatos();
    }

    @AfterEach
    public void teardown() {
        ad.close();
    }

    @Test
    public void testNada() {
        // Nada que hacer, está simplemente para abrir y cerrar el contexto de persistencia
    }
}
