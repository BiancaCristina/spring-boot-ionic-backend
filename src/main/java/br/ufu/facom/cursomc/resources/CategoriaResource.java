package br.ufu.facom.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.ufu.facom.cursomc.domain.Categoria;
import br.ufu.facom.cursomc.dto.CategoriaDTO;
import br.ufu.facom.cursomc.services.CategoriaService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	// A anotacao @ApiOperation = define o nome da operacao na documentacao Swagger
	// A anotacao @ApiResponses = define uma mensagem personalizada de um endpoint em uma operacao especifica
		// Ou seja, aqui no metodo "delete", tem uma mensagem diferente da padrao pros status 404 e 400, mas em outros lugares da API que esse erro pode estourar a mensagem eh a padrao
	@Autowired
	private CategoriaService service;
	
	@ApiOperation(value="Busca por id")
	@RequestMapping(value="/{id}",method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		// @PathVariable = faz com que {id} corresponda ao id passado como parametro (caminho da URL)
		// ResponseEntity = armazena varias info de uma resposta HTTP para um servico REST			
		
		Categoria obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value="Insere categoria")
	@PreAuthorize("hasAnyRole('ADMIN')") // So admin acessa
	@RequestMapping(method=RequestMethod.POST) // Faz com que seja um metodo POST
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDTO) {
		//@RequestBody = faz o Json ser convertido automaticamente para o objeto java
		Categoria obj = service.fromDTO(objDTO); // Converte CategoriaDTO em Categoria
		obj = service.insert(obj); // Chama o metodo "insert" do objeto "service" que eh do tipo CategoriaService
		// Http status code = mostra os codigos http padrao 
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		// Esse uri serve pra me dar a url da nova categoria que inseri
		
		return ResponseEntity.created(uri).build();		
	}
	
	@ApiOperation(value="Atualiza categoria")
	@PreAuthorize("hasAnyRole('ADMIN')") // So admin acessa
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody @Valid CategoriaDTO objDTO, @PathVariable Integer id) {
		Categoria obj = service.fromDTO(objDTO);
		
		obj.setId(id); // Comando feito pra garantir que o objeto que eu to passando tenha ID que eu quero atualizar
		obj = service.update(obj);
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Não é possível excluir uma categoria que possui produtos"),
			@ApiResponse(code = 404, message = "Código inexistente") })
	@ApiOperation(value="Remove categoria")
	@PreAuthorize("hasAnyRole('ADMIN')") // So admin acessa
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE) // REQUISICAO BASICA = GET
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);		
		
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value="Retorna todas categorias")
	@RequestMapping(method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<List<CategoriaDTO>> findAll() {	
		List <Categoria> list = service.findAll();
		
		// O codigo abaixo converte cada objeto da lista de Categoria em um objeto de CategoriaDTO e forma uma lista de CategoriaDTO
		// CategoriaDTO mostrara apenas os dados que me interessam quando estou imprimindo a lista de Categorias(id,nome)
		List <CategoriaDTO> listDTO = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	@ApiOperation(value="Retorna todas categorias com paginação")
	@RequestMapping(value="/page",method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(name="page",defaultValue="0") Integer page, 
			@RequestParam(name="linesPerPage",defaultValue="24") Integer linesPerPage, 
			@RequestParam(name="orderBy",defaultValue="nome") String orderBy, 
			@RequestParam(name="direction",defaultValue="ASC") String direction) // FIM DOS PARAMETROS DO METODO
	{	
		// defaultValue = valor padrao -> se nao for informado nenhum valor, assume o valor padrao
		// defaultValue do direction pode ser ASC(ascendente) ou DESC(descendente)
		// defaultValue do orderBy = por qual parametro devo ordenar as paginas
		// defaultValue do linesPerPage = numero de Categorias informadas por pagina 
		// Essa funcao faz a paginacao (divisao de Categoria por pagina)
		
		Page<Categoria> list = service.findPage(page,linesPerPage,orderBy,direction);
		
		// O codigo abaixo converte cada objeto da lista de Categoria em um objeto de CategoriaDTO e forma uma lista de CategoriaDTO
		// CategoriaDTO mostrara apenas os dados que me interessam quando estou imprimindo a lista de Categorias(id,nome)
		Page<CategoriaDTO> listDTO = list.map(obj -> new CategoriaDTO(obj)); // Retirei "stream" e "collect"
		return ResponseEntity.ok().body(listDTO);
	}
}
