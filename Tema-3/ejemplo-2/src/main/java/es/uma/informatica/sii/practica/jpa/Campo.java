package es.uma.informatica.sii.practica.jpa;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Campo {
    @Id
    private Long id;
    @Column(name ="nombre_campo")
    private String nombre;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "formulario")
    private Formulario formulario;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campo campo = (Campo) o;
        return Objects.equals(id, campo.id) && Objects.equals(nombre, campo.nombre) && tipo == campo.tipo && Objects.equals(descripcion, campo.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, tipo, descripcion);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Formulario getFormulario() {
        return formulario;
    }

    public void setFormulario(Formulario formulario) {
        this.formulario = formulario;
    }
}
