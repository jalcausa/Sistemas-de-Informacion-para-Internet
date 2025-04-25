package es.uma.informatica.sii.eventosasistentes.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AsistenteDTO {
    private Long id;
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;
}
