package br.ufu.facom.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.ufu.facom.cursomc.domain.Categoria;
import br.ufu.facom.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@RequestMapping(value="/{id}",method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<?> find(@PathVariable Integer id) {
		// @PathVariable = faz com que {id} corresponda ao id passado como parametro
		// ResponseEntity = armazena varias info de uma resposta HTTP para um servico REST
			// O "?" significa que pode ser qualquer tipo, uma vez que a busca pode ser bem sucedida ou nao
		
		Categoria obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}
	
	
	@RequestMapping(method=RequestMethod.POST) // Faz com que seja um metodo POST
	public ResponseEntity<Void> insert(@RequestBody Categoria obj) {
		//@RequestBody = faz o Json ser convertido automaticamente para o objeto java
		obj = service.insert(obj); // Chama o metodo "insert" do objeto "service" que eh do tipo CategoriaService
		// Http status code = mostra os codigos http padrao 
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		// Esse uri serve pra me dar a url da nova categoria que inseri
		
		return ResponseEntity.created(uri).build();		
	}
}
