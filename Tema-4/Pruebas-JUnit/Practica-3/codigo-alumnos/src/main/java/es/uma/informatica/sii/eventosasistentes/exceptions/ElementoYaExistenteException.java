package es.uma.informatica.sii.eventosasistentes.exceptions;

public class ElementoYaExistenteException extends RuntimeException {
    public ElementoYaExistenteException(String message) {
        super(message);
    }

    public ElementoYaExistenteException() {
        super();
    }
}
