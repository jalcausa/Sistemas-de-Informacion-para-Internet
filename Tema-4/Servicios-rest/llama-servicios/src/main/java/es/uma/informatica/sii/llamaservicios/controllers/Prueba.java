package es.uma.informatica.sii.llamaservicios.controllers;

import es.uma.informatica.sii.llamaservicios.dtos.UsuarioDTO;
import es.uma.informatica.sii.llamaservicios.security.SecurityConfguration;
import es.uma.informatica.sii.llamaservicios.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class Prueba {

    private UsuarioService usuarioService;

    public Prueba(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/info-usuario")
    public ResponseEntity<UsuarioDTO> infoUsuario(@RequestHeader("Authorization") String jwtToken) {
        return ResponseEntity.of(usuarioService.getUsuarioConectado(jwtToken));
    }

    @GetMapping("/info-usuario/{id}")
    public ResponseEntity<UsuarioDTO> infoCualquierUsuario(@PathVariable Long id,
                @RequestHeader("Authorization") String jwtToken) {
        return ResponseEntity.of(usuarioService.getUsuario(id, jwtToken));
    }
}
