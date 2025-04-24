package es.uma.informatica.sii.helloworld.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import es.uma.informatica.sii.helloworld.entities.Item;
import es.uma.informatica.sii.helloworld.entities.ListaCompra;
import es.uma.informatica.sii.helloworld.service.ListaCompraDBService;
import es.uma.informatica.sii.helloworld.service.NoEncontradoException;

@RestController
@RequestMapping(path = "/listas")
public class ListaCompraController {
	public static final String LISTA_COMPRA_PATH="/listas"; 
	public static final String ITEM_PATH="/item";
	
	private ListaCompraDBService service;
	
	public ListaCompraController(ListaCompraDBService service) {
		this.service = service;
	}
	
	@GetMapping
	public List<Long> obtieneListas() {
		return service.todasListascompras();
	}
	
	@PostMapping
	public ResponseEntity<?> aniadeLista(@RequestBody ListaCompra nuevaLista, UriComponentsBuilder builder) {
		Long id = service.aniadirListaCompra(nuevaLista);
		URI uri = builder.path("/listas")
						.path(String.format("/%d", id))
						.build()
						.toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ListaCompra> obtenerLista(@PathVariable Long id) {
		Optional<ListaCompra> listaCompraById = service.listaCompraById(id);
		return ResponseEntity.of(listaCompraById);
	}
	
	@PutMapping("{id}")
	public ResponseEntity<?> modificaLista(@PathVariable Long id, @RequestBody ListaCompra lista) {
		lista.setId(id);
		service.modificarListaCompra(lista);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void eliminarLista(@PathVariable Long id) {
		service.eliminarListaCompra(id);
	}
	
	@PostMapping("{id}")
	public ResponseEntity<?> aniadeItem(@PathVariable(name="id") Long idLista, @RequestBody Item item, UriComponentsBuilder uriBuilder) {
		Item itemEntity = item;
		itemEntity.setId(null);
		Long idItem = service.aniadirItem(idLista, itemEntity);
		URI uri = uriBuilder
				.path("/listas").path(String.format("/%d", idLista))
				.path("/item").path(String.format("/%d", idItem))
				.build()
				.toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("{idLista}/item/{idItem}")
	public ResponseEntity<Item> obtenerItem(@PathVariable Long idLista, @PathVariable Long idItem) {
		Optional<Item> item = service.getItem(idLista, idItem);
		return ResponseEntity.of(item);
	}
	
	
	@PutMapping("{idLista}/item/{idItem}")
	@ResponseStatus(code = HttpStatus.OK)
	public void modificarItem(@PathVariable Long idLista, @PathVariable Long idItem, @RequestBody Item item) {
		item.setId(idItem);
		service.modificarItem(idLista, item);
	}
	
	@DeleteMapping("{idLista}/item/{idItem}")
	public ResponseEntity<?> eliminarItem(@PathVariable Long idLista, @PathVariable Long idItem) {
		service.eliminarItem(idLista, idItem);
		return ResponseEntity.ok().build();
	}
	
	@ExceptionHandler(NoEncontradoException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public void noEncontrado() {}

}
