package es.uma.informatica.sii.helloworld.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class ListaCompra {
	
	@Id @GeneratedValue
	private Long id;
	@OneToMany (fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn
	private List<Item> items;
	private String nombre;
	
	public ListaCompra() {
		items = new ArrayList<>();
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Item> getItems() {
		return items;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
