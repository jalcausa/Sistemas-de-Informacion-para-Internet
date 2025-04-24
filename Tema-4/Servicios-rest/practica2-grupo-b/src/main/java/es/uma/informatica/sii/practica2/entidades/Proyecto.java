package es.uma.informatica.sii.practica2.entidades;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class Proyecto {
	
	@Id @GeneratedValue
	private Long id;
	private String nombre;
	@Temporal(TemporalType.DATE)
	private Date fechaInicio;
	private Integer duracion;
	
	public Proyecto() {}
	
	public Proyecto(Long id, String nombre, Date fechaInicio, Integer duracion) {
		this();
		this.id = id;
		this.nombre = nombre;
		this.fechaInicio = fechaInicio;
		this.duracion = duracion;
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
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Integer getDuracion() {
		return duracion;
	}
	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Proyecto other = (Proyecto) obj;
		return Objects.equals(id, other.id);
	}
	@Override
	public String toString() {
		return "Proyecto [id=" + id + ", nombre=" + nombre + ", fechaInicio=" + fechaInicio + ", duracion=" + duracion + "]";
	}
}
