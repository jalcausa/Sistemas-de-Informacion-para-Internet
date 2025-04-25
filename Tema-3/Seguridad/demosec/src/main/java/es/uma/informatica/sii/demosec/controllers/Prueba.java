package es.uma.informatica.sii.demosec.controllers;

import es.uma.informatica.sii.demosec.security.SecurityConfguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class Prueba {

    @GetMapping("/prueba")
    public ResponseEntity<String> prueba() {
        var usuario = SecurityConfguration.getAuthenticatedUser()
            .map(user-> String.format("Conectado el usuario %s", user.getUsername()))
            .orElse("No hay usuario conectado");
        return ResponseEntity.ok(String.format("Prueba con éxito: %s", usuario));
    }

    @GetMapping("/sin-auth")
    public ResponseEntity<String> noAuth() {
        return ResponseEntity.ok("No se requiere autenticación para esta ruta");
    }
}
