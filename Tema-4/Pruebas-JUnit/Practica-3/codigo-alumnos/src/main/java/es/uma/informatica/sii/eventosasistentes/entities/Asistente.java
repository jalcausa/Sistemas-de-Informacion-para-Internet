package es.uma.informatica.sii.eventosasistentes.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Asistente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;
    @ManyToMany
    @EqualsAndHashCode.Exclude
    private Set<Evento> eventos;
}
