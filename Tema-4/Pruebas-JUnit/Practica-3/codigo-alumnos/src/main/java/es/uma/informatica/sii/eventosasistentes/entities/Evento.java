package es.uma.informatica.sii.eventosasistentes.entities;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EqualsAndHashCode
public class Evento {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String nombre;
    private Timestamp inicio;
    private Timestamp fin;
    @ManyToMany(mappedBy = "eventos")
    @EqualsAndHashCode.Exclude
    private Set<Asistente> asistentes;
}
