package es.uma.informatica.sii.practica.jpa;

import jakarta.persistence.*;

import java.lang.annotation.ElementType;
import java.util.Objects;

@Entity
public class Plaza {
    @Id
    private Long id;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    private Integer fila;
    private Character letra;
    @ManyToOne
    @JoinColumn(name="vuelo")
    private Vuelo vuelo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Integer getFila() {
        return fila;
    }

    public void setFila(Integer fila) {
        this.fila = fila;
    }

    public Character getLetra() {
        return letra;
    }

    public void setLetra(Character letra) {
        this.letra = letra;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plaza plaza = (Plaza) o;
        return Objects.equals(id, plaza.id) && tipo == plaza.tipo && Objects.equals(fila, plaza.fila) && Objects.equals(letra, plaza.letra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipo, fila, letra);
    }
}
