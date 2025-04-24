package es.uma.informatica.practica3;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

import es.uma.informatica.practica3.dtos.IngredienteDTO;
import es.uma.informatica.practica3.dtos.ProductoDTO;
import es.uma.informatica.practica3.entidades.Ingrediente;
import es.uma.informatica.practica3.entidades.Producto;
import es.uma.informatica.practica3.repositorios.IngredienteRepository;
import es.uma.informatica.practica3.repositorios.ProductoRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayName("En el servicio de productos")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class Practica3ApplicationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Value(value="${local.server.port}")
	private int port;
	
	@Autowired
	private IngredienteRepository ingredienteRepo;
	
	@Autowired
	private ProductoRepository productoRepo;
	
	
	private URI uri(String scheme, String host, int port, String ...paths) {
		UriBuilderFactory ubf = new DefaultUriBuilderFactory();
		UriBuilder ub = ubf.builder()
				.scheme(scheme)
				.host(host).port(port);
		for (String path: paths) {
			ub = ub.path(path);
		}
		return ub.build();
	}
	
	private RequestEntity<Void> get(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.get(uri)
			.accept(MediaType.APPLICATION_JSON)
			.build();
		return peticion;
	}
	
	private RequestEntity<Void> delete(String scheme, String host, int port, String path) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.delete(uri)
			.build();
		return peticion;
	}
	
	private <T> RequestEntity<T> post(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.post(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.body(object);
		return peticion;
	}
	
	private <T> RequestEntity<T> put(String scheme, String host, int port, String path, T object) {
		URI uri = uri(scheme, host,port, path);
		var peticion = RequestEntity.put(uri)
			.contentType(MediaType.APPLICATION_JSON)
			.body(object);
		return peticion;
	}
	
	private void compruebaCampos(Ingrediente expected, Ingrediente actual) {
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
	}
	
	private void compruebaCampos(Producto expected, Producto actual) {	
		assertThat(actual.getNombre()).isEqualTo(expected.getNombre());
		assertThat(actual.getDescripcion()).isEqualTo(expected.getDescripcion());
		assertThat(actual.getIngredientes()).isEqualTo(expected.getIngredientes());
	}
	
	@Nested
	@DisplayName("cuando la base de datos está vacía")
	public class BaseDatosVacia {

		@Test
		@DisplayName("devuelve error al acceder a un ingrediente concreto")
		public void errorConIngredienteConcreto() {
			var peticion = get("http", "localhost", port, "/ingredientes/1");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<IngredienteDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve error al acceder a un producto concreto")
		public void errorConProductoConcreto() {
			var peticion = get("http", "localhost", port, "/productos/1");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<ProductoDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve una lista vacía de ingredientes")
		public void devuelveListaVaciaIngredientes() {
			var peticion = get("http", "localhost", port, "/ingredientes");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<IngredienteDTO>>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}
		
		@Test
		@DisplayName("devuelve una lista vacía de productos")
		public void devuelveListaVaciaProductos() {
			var peticion = get("http", "localhost",port, "/productos");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<ProductoDTO>>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).isEmpty();
		}
		
		@Test
		@DisplayName("inserta correctamente un ingrediente")
		public void insertaIngrediente() {
			
			// Preparamos el ingrediente a insertar
			var ingrediente = IngredienteDTO.builder()
									.nombre("Chorizo")
									.build();
			// Preparamos la petición con el ingrediente dentro
			var peticion = post("http", "localhost",port, "/ingredientes", ingrediente);
			
			// Invocamos al servicio REST 
			var respuesta = restTemplate.exchange(peticion,Void.class);
			
			// Comprobamos el resultado
			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.startsWith("http://localhost:"+port+"/ingredientes");
		
			List<Ingrediente> ingredientesBD = ingredienteRepo.findAll();
			assertThat(ingredientesBD).hasSize(1);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.endsWith("/"+ingredientesBD.get(0).getId());
			compruebaCampos(ingrediente.ingrediente(), ingredientesBD.get(0));
		}
		
		@Test
		@DisplayName("devuelve error al intentar insertar un producto con ingredientes inexistentes")
		public void errorInsertarProductoConIngredientesInexistentes() {
			var ingredienteDTO = IngredienteDTO.builder()
					.id(1L)
					.nombre("Inexistente")
					.build();
			
			Set<IngredienteDTO> ingredientes = new HashSet<>();
			ingredientes.add(ingredienteDTO);
			
			var producto = ProductoDTO.builder()
					.nombre("Pizza")
					.descripcion("Pizza casera")
					.ingredientes(ingredientes)
					.build();
			
			var peticion = post("http", "localhost", port, "/productos", producto);
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
	}
	
	@Nested
	@DisplayName("cuando hay ingredientes en la base de datos")
	public class ConIngredientes {
		
		private Ingrediente prepararIngredienteEnBD(String nombre) {
			var ingrediente = new Ingrediente();
			ingrediente.setNombre(nombre);
			return ingredienteRepo.save(ingrediente);
		}
		
		@Test
		@DisplayName("obtiene un ingrediente por su id")
		public void obtieneIngredientePorId() {
			var ingrediente = prepararIngredienteEnBD("Tomate");
			
			var peticion = get("http", "localhost", port, "/ingredientes/" + ingrediente.getId());
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<IngredienteDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Tomate");
			assertThat(respuesta.getBody().getId()).isEqualTo(ingrediente.getId());
		}
		
		@Test
		@DisplayName("obtiene todos los ingredientes")
		public void obtieneTodosLosIngredientes() {
			prepararIngredienteEnBD("Tomate");
			prepararIngredienteEnBD("Queso");
			prepararIngredienteEnBD("Jamón");
			
			var peticion = get("http", "localhost", port, "/ingredientes");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<IngredienteDTO>>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(3);
		}
		
		@Test
		@DisplayName("actualiza un ingrediente")
		public void actualizaIngrediente() {
			var ingrediente = prepararIngredienteEnBD("Tomate");
			
			var ingredienteActualizado = IngredienteDTO.builder()
					.id(ingrediente.getId())
					.nombre("Tomate Cherry")
					.build();
			
			var peticion = put("http", "localhost", port, "/ingredientes/" + ingrediente.getId(), ingredienteActualizado);
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			
			var ingredienteBD = ingredienteRepo.findById(ingrediente.getId()).get();
			assertThat(ingredienteBD.getNombre()).isEqualTo("Tomate Cherry");
		}
		
		@Test
		@DisplayName("elimina un ingrediente")
		public void eliminaIngrediente() {
			var ingrediente = prepararIngredienteEnBD("Cebolla");
			
			var peticion = delete("http", "localhost", port, "/ingredientes/" + ingrediente.getId());
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(ingredienteRepo.existsById(ingrediente.getId())).isFalse();
		}
		
		@Test
		@DisplayName("devuelve error al actualizar un ingrediente inexistente")
		public void errorActualizarIngredienteInexistente() {
			var ingredienteDTO = IngredienteDTO.builder()
					.id(999L)
					.nombre("Inexistente")
					.build();
			
			var peticion = put("http", "localhost", port, "/ingredientes/999", ingredienteDTO);
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve error al eliminar un ingrediente inexistente")
		public void errorEliminarIngredienteInexistente() {
			var peticion = delete("http", "localhost", port, "/ingredientes/999");
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve error al insertar un ingrediente con nombre repetido")
		public void errorInsertarIngredienteConNombreRepetido() {
			prepararIngredienteEnBD("Cebolla");
			
			var ingredienteDTO = IngredienteDTO.builder()
					.nombre("Cebolla")
					.build();
			
			var peticion = post("http", "localhost", port, "/ingredientes", ingredienteDTO);
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(409); // Conflict
		}

		@Test
		@DisplayName("inserta correctamente un producto con ingredientes")
		public void insertaProductoConIngredientes() {
			var tomate = prepararIngredienteEnBD("Tomate");
			var queso = prepararIngredienteEnBD("Queso");

			var ingredienteDTO1 = IngredienteDTO.builder()
					.id(tomate.getId())
					.build();

			var ingredienteDTO2 = IngredienteDTO.builder()
					.id(queso.getId())
					.build();

			Set<IngredienteDTO> ingredientes = new HashSet<>();
			ingredientes.add(ingredienteDTO1);
			ingredientes.add(ingredienteDTO2);

			var producto = ProductoDTO.builder()
					.nombre("Pizza Margarita")
					.descripcion("Pizza con tomate y queso")
					.ingredientes(ingredientes)
					.build();

			var peticion = post("http", "localhost", port, "/productos", producto);

			var respuesta = restTemplate.exchange(peticion, Void.class);

			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
			assertThat(respuesta.getHeaders().get("Location").get(0))
				.startsWith("http://localhost:" + port + "/productos");

			List<Producto> productosBD = productoRepo.findAll();
			assertThat(productosBD).hasSize(1);
			assertThat(productosBD.get(0).getNombre()).isEqualTo("Pizza Margarita");
			assertThat(productosBD.get(0).getIngredientes()).hasSize(2);
		}

//		@Test
//		@DisplayName("inserta correctamente un producto con ingredientes por nombre")
//		public void insertaProductoConIngredientesPorNombre() {
//			// Preparar ingredientes en la base de datos
//			prepararIngredienteEnBD("Tomate");
//			prepararIngredienteEnBD("Queso");
//
//			// Crear ingredientes solo con nombre, sin ID
//			var ingredienteDTO1 = IngredienteDTO.builder()
//					.nombre("Tomate")
//					.build();
//
//			var ingredienteDTO2 = IngredienteDTO.builder()
//					.nombre("Queso")
//					.build();
//
//			Set<IngredienteDTO> ingredientes = new HashSet<>();
//			ingredientes.add(ingredienteDTO1);
//			ingredientes.add(ingredienteDTO2);
//
//			var producto = ProductoDTO.builder()
//					.nombre("Pizza Margarita")
//					.descripcion("Pizza con tomate y queso")
//					.ingredientes(ingredientes)
//					.build();
//
//			var peticion = post("http", "localhost", port, "/productos", producto);
//
//			var respuesta = restTemplate.exchange(peticion, Void.class);
//
//			assertThat(respuesta.getStatusCode().value()).isEqualTo(201);
//
//			// Obtener el producto de la base de datos
//			List<Producto> productosBD = productoRepo.findAll();
//			assertThat(productosBD).hasSize(1);
//			assertThat(productosBD.get(0).getNombre()).isEqualTo("Pizza Margarita");
//
//			// Verificar que el producto tiene los ingredientes correctos
//			Set<Ingrediente> ingredientesBD = productosBD.get(0).getIngredientes();
//			assertThat(ingredientesBD).hasSize(2);
//
//			// Verificar los nombres de los ingredientes
//			Set<String> nombresIngredientes = ingredientesBD.stream()
//					.map(Ingrediente::getNombre)
//					.collect(Collectors.toSet());
//
//			assertThat(nombresIngredientes).contains("Tomate", "Queso");
//		}
		
		@Test
		@DisplayName("devuelve error al crear un producto con un ingrediente inválido (sin ID ni nombre)")
		public void errorInsertarProductoConIngredienteInvalido() {
			var ingredienteInvalido = IngredienteDTO.builder().build(); // Sin ID ni nombre
			
			Set<IngredienteDTO> ingredientes = new HashSet<>();
			ingredientes.add(ingredienteInvalido);
			
			var producto = ProductoDTO.builder()
					.nombre("Pizza Margarita")
					.descripcion("Pizza con ingrediente inválido")
					.ingredientes(ingredientes)
					.build();
			
			var peticion = post("http", "localhost", port, "/productos", producto);
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404); // Not Found
		}
	}
	
	@Nested
	@DisplayName("cuando hay productos en la base de datos")
	public class ConProductos {
		
		private Ingrediente prepararIngredienteEnBD(String nombre) {
			var ingrediente = new Ingrediente();
			ingrediente.setNombre(nombre);
			return ingredienteRepo.save(ingrediente);
		}
		
		private Producto prepararProductoEnBD(String nombre, String descripcion, Set<Ingrediente> ingredientes) {
			var producto = new Producto();
			producto.setNombre(nombre);
			producto.setDescripcion(descripcion);
			producto.setIngredientes(ingredientes);
			return productoRepo.save(producto);
		}
		
		@Test
		@DisplayName("obtiene un producto por su id")
		public void obtieneProductoPorId() {
			var tomate = prepararIngredienteEnBD("Tomate");
			var queso = prepararIngredienteEnBD("Queso");
			
			Set<Ingrediente> ingredientes = new HashSet<>();
			ingredientes.add(tomate);
			ingredientes.add(queso);
			
			var producto = prepararProductoEnBD("Pizza Margarita", "Pizza básica", ingredientes);
			
			var peticion = get("http", "localhost", port, "/productos/" + producto.getId());
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<ProductoDTO>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody().getNombre()).isEqualTo("Pizza Margarita");
			assertThat(respuesta.getBody().getId()).isEqualTo(producto.getId());
			assertThat(respuesta.getBody().getIngredientes()).hasSize(2);
		}
		
		@Test
		@DisplayName("obtiene todos los productos")
		public void obtieneTodosLosProductos() {
			var tomate = prepararIngredienteEnBD("Tomate");
			var queso = prepararIngredienteEnBD("Queso");
			var jamon = prepararIngredienteEnBD("Jamón");
			
			Set<Ingrediente> ingredientes1 = new HashSet<>();
			ingredientes1.add(tomate);
			ingredientes1.add(queso);
			
			Set<Ingrediente> ingredientes2 = new HashSet<>();
			ingredientes2.add(tomate);
			ingredientes2.add(queso);
			ingredientes2.add(jamon);
			
			prepararProductoEnBD("Pizza Margarita", "Pizza básica", ingredientes1);
			prepararProductoEnBD("Pizza Jamón", "Pizza con jamón", ingredientes2);
			
			var peticion = get("http", "localhost", port, "/productos");
			
			var respuesta = restTemplate.exchange(peticion,
					new ParameterizedTypeReference<List<ProductoDTO>>() {});
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(respuesta.getBody()).hasSize(2);
		}
		
		@Test
		@DisplayName("actualiza un producto")
		public void actualizaProducto() {
			var tomate = prepararIngredienteEnBD("Tomate");
			var queso = prepararIngredienteEnBD("Queso");
			var jamon = prepararIngredienteEnBD("Jamón");
			
			Set<Ingrediente> ingredientes = new HashSet<>();
			ingredientes.add(tomate);
			ingredientes.add(queso);
			
			var producto = prepararProductoEnBD("Pizza Margarita", "Pizza básica", ingredientes);
			
			var ingredienteDTO1 = IngredienteDTO.builder()
					.id(tomate.getId())
					.build();
			
			var ingredienteDTO2 = IngredienteDTO.builder()
					.id(queso.getId())
					.build();
			
			var ingredienteDTO3 = IngredienteDTO.builder()
					.id(jamon.getId())
					.build();
			
			Set<IngredienteDTO> nuevosIngredientes = new HashSet<>();
			nuevosIngredientes.add(ingredienteDTO1);
			nuevosIngredientes.add(ingredienteDTO2);
			nuevosIngredientes.add(ingredienteDTO3);
			
			var productoActualizado = ProductoDTO.builder()
					.id(producto.getId())
					.nombre("Pizza Completa")
					.descripcion("Pizza con todos los ingredientes")
					.ingredientes(nuevosIngredientes)
					.build();
			
			var peticion = put("http", "localhost", port, "/productos/" + producto.getId(), productoActualizado);
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			
			var productoBD = productoRepo.findById(producto.getId()).get();
			assertThat(productoBD.getNombre()).isEqualTo("Pizza Completa");
			assertThat(productoBD.getDescripcion()).isEqualTo("Pizza con todos los ingredientes");
			assertThat(productoBD.getIngredientes()).hasSize(3);
		}
		
		@Test
		@DisplayName("elimina un producto")
		public void eliminaProducto() {
			var tomate = prepararIngredienteEnBD("Tomate");
			var queso = prepararIngredienteEnBD("Queso");
			
			Set<Ingrediente> ingredientes = new HashSet<>();
			ingredientes.add(tomate);
			ingredientes.add(queso);
			
			var producto = prepararProductoEnBD("Pizza Margarita", "Pizza básica", ingredientes);
			
			var peticion = delete("http", "localhost", port, "/productos/" + producto.getId());
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			assertThat(productoRepo.existsById(producto.getId())).isFalse();
			// Verificar que los ingredientes no se eliminan al eliminar el producto
			assertThat(ingredienteRepo.findAll()).hasSize(2);
		}
		
		@Test
		@DisplayName("devuelve error al actualizar un producto inexistente")
		public void errorActualizarProductoInexistente() {
			var productoDTO = ProductoDTO.builder()
					.id(999L)
					.nombre("Inexistente")
					.descripcion("No existe")
					.build();
			
			var peticion = put("http", "localhost", port, "/productos/999", productoDTO);
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve error al eliminar un producto inexistente")
		public void errorEliminarProductoInexistente() {
			var peticion = delete("http", "localhost", port, "/productos/999");
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(404);
		}
		
		@Test
		@DisplayName("devuelve error al insertar un producto con nombre repetido")
		public void errorInsertarProductoConNombreRepetido() {
			var tomate = prepararIngredienteEnBD("Tomate");
			
			Set<Ingrediente> ingredientes = new HashSet<>();
			ingredientes.add(tomate);
			
			prepararProductoEnBD("Pizza Margarita", "Pizza básica", ingredientes);
			
			var ingredienteDTO = IngredienteDTO.builder()
					.id(tomate.getId())
					.build();
			
			Set<IngredienteDTO> nuevosIngredientes = new HashSet<>();
			nuevosIngredientes.add(ingredienteDTO);
			
			var productoDTO = ProductoDTO.builder()
					.nombre("Pizza Margarita")
					.descripcion("Otra descripción")
					.ingredientes(nuevosIngredientes)
					.build();
			
			var peticion = post("http", "localhost", port, "/productos", productoDTO);
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(409); // Conflict
		}
		
		@Test
		@DisplayName("actualiza un producto utilizando mezcla de ingredientes (por ID y por nombre)")
		public void actualizaProductoConMezclaDeIngredientes() {
			var tomate = prepararIngredienteEnBD("Tomate");
			var queso = prepararIngredienteEnBD("Queso");
			prepararIngredienteEnBD("Jamón");
			
			Set<Ingrediente> ingredientes = new HashSet<>();
			ingredientes.add(tomate);
			
			var producto = prepararProductoEnBD("Pizza Simple", "Solo tomate", ingredientes);
			
			// Actualizar mezclando referencias por ID y por nombre
			var ingredienteDTO1 = IngredienteDTO.builder()
					.id(queso.getId()) // Referencia por ID
					.build();
			
			var ingredienteDTO2 = IngredienteDTO.builder()
					.nombre("Jamón") // Referencia por nombre
					.build();
			
			Set<IngredienteDTO> nuevosIngredientes = new HashSet<>();
			nuevosIngredientes.add(ingredienteDTO1);
			nuevosIngredientes.add(ingredienteDTO2);
			
			var productoActualizado = ProductoDTO.builder()
					.id(producto.getId())
					.nombre("Pizza Mixta")
					.descripcion("Con queso y jamón")
					.ingredientes(nuevosIngredientes)
					.build();
			
			var peticion = put("http", "localhost", port, "/productos/" + producto.getId(), productoActualizado);
			
			var respuesta = restTemplate.exchange(peticion, Void.class);
			
			assertThat(respuesta.getStatusCode().value()).isEqualTo(200);
			
			var productoBD = productoRepo.findById(producto.getId()).get();
			assertThat(productoBD.getNombre()).isEqualTo("Pizza Mixta");
			assertThat(productoBD.getIngredientes()).hasSize(2);
			
			// Verificar que contiene los ingredientes correctos
			boolean contieneQueso = productoBD.getIngredientes().stream()
					.anyMatch(ing -> ing.getNombre().equals("Queso"));
			boolean contieneJamon = productoBD.getIngredientes().stream()
					.anyMatch(ing -> ing.getNombre().equals("Jamón"));
			
			assertThat(contieneQueso).isTrue();
			assertThat(contieneJamon).isTrue();
		}
	}
}