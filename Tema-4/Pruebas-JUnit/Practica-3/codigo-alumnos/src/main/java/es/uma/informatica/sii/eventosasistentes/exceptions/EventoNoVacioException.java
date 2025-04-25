package es.uma.informatica.sii.eventosasistentes.exceptions;

public class EventoNoVacioException extends RuntimeException {
    public EventoNoVacioException(String message) {
        super(message);
    }

    public EventoNoVacioException() {
        super();
    }
}
