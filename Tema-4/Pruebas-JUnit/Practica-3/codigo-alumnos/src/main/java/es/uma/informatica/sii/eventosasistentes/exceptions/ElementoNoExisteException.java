package es.uma.informatica.sii.eventosasistentes.exceptions;

public class ElementoNoExisteException extends RuntimeException {
    public ElementoNoExisteException(String message) {
        super(message);
    }

    public ElementoNoExisteException() {
        super();
    }
}
