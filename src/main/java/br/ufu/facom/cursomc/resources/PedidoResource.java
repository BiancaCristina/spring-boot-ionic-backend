package br.ufu.facom.cursomc.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import br.ufu.facom.cursomc.domain.Pedido;
import br.ufu.facom.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	@RequestMapping(value="/{id}",method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		// @PathVariable = faz com que {id} corresponda ao id passado como parametro
		// ResponseEntity = armazena varias info de uma resposta HTTP para um servico REST
			
		Pedido obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	// Nao usarei um PedidoDTO porque tem muitos dados associados a um Pedido, entao usarei a Pedido mesmo
	@RequestMapping(method=RequestMethod.POST) // Faz com que seja um metodo POST
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj) {
		//@RequestBody = faz o Json ser convertido automaticamente para o objeto java
		obj = service.insert(obj); // Chama o metodo "insert" do objeto "service" que eh do tipo CategoriaService
		// Http status code = mostra os codigos http padrao 
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		// Esse uri serve pra me dar a url da nova categoria que inseri
		
		return ResponseEntity.created(uri).build();		
	}
	
	@RequestMapping(method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<Page<Pedido>> findPage(
			@RequestParam(name="page",defaultValue="0") Integer page, 
			@RequestParam(name="linesPerPage",defaultValue="24") Integer linesPerPage, 
			@RequestParam(name="orderBy",defaultValue="instante") String orderBy, 
			@RequestParam(name="direction",defaultValue="DESC") String direction) // FIM DOS PARAMETROS DO METODO
	{	
		// defaultValue = valor padrao -> se nao for informado nenhum valor, assume o valor padrao
		// defaultValue do direction pode ser ASC(ascendente) ou DESC(descendente)
		// defaultValue do orderBy = por qual parametro devo ordenar as paginas
		// defaultValue do linesPerPage = numero de Categorias informadas por pagina 
		// Essa funcao faz a paginacao (divisao de Categoria por pagina)
		
		Page<Pedido> list = service.findPage(page,linesPerPage,orderBy,direction);	
		return ResponseEntity.ok().body(list);
	}
}
