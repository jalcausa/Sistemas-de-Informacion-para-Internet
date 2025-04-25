package es.uma.informatica.sii.eventosasistentes.controllers;

import es.uma.informatica.sii.eventosasistentes.dtos.AsistenteDTO;
import es.uma.informatica.sii.eventosasistentes.dtos.EventoDTO;
import es.uma.informatica.sii.eventosasistentes.entities.Asistente;
import es.uma.informatica.sii.eventosasistentes.entities.Evento;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Mapper {

    public static EventoDTO toDTO(Evento evento) {
        return EventoDTO.builder()
            .id(evento.getId())
            .nombre(evento.getNombre())
            .inicio(evento.getInicio())
            .fin(evento.getFin())
            .asistentes(mapNullable(evento.getAsistentes(), Mapper::toDTO))
            .build();
    }

    public static Evento toEntity(EventoDTO eventoDTO) {
        return Evento.builder()
            .id(eventoDTO.getId())
            .nombre(eventoDTO.getNombre())
            .inicio(eventoDTO.getInicio())
            .fin(eventoDTO.getFin())
            .build();
    }

    public static AsistenteDTO toDTO(Asistente asistente) {
        return AsistenteDTO.builder()
            .id(asistente.getId())
            .dni(asistente.getDni())
            .nombre(asistente.getNombre())
            .apellido1(asistente.getApellido1())
            .apellido2(asistente.getApellido2())
            .build();
    }

    public static Asistente toEntity(AsistenteDTO asistenteDTO) {
        return Asistente.builder()
            .id(asistenteDTO.getId())
            .dni(asistenteDTO.getDni())
            .nombre(asistenteDTO.getNombre())
            .apellido1(asistenteDTO.getApellido1())
            .apellido2(asistenteDTO.getApellido2())
            .build();
    }

    public static <U, V> List<V> mapNullable(Collection<U> original, Function<U,V> mapping) {
        if (original == null) {
            return null;
        }
        return original.stream().map(mapping).collect(Collectors.toList());
    }

}
