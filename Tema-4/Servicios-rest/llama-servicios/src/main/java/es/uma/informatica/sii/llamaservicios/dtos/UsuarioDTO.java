package es.uma.informatica.sii.llamaservicios.dtos;

import es.uma.informatica.sii.llamaservicios.entities.Usuario;
import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String email;
    private Usuario.Rol role;
}
