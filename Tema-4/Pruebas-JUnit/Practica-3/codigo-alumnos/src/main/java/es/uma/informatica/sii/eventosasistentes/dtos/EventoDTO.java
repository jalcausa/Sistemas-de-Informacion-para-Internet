package es.uma.informatica.sii.eventosasistentes.dtos;

import es.uma.informatica.sii.eventosasistentes.entities.Asistente;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoDTO {
    private Long id;
    private String nombre;
    private Timestamp inicio;
    private Timestamp fin;
    private List<AsistenteDTO> asistentes;

    public static EventoDTO of(String nombre, Timestamp inicio, Timestamp fin) {
        return EventoDTO.builder()
            .nombre(nombre)
            .inicio(inicio)
            .fin(fin)
            .build();
    }
}
