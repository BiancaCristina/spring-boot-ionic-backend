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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.dto.ClienteDTO;
import br.ufu.facom.cursomc.dto.ClienteNewDTO;
import br.ufu.facom.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@RequestMapping(value="/{id}",method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		// @PathVariable = faz com que {id} corresponda ao id passado como parametro
		// ResponseEntity = armazena varias info de uma resposta HTTP para um servico REST
			
		Cliente obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method=RequestMethod.POST) // Faz com que seja um metodo POST
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDTO) {
		//@RequestBody = faz o Json ser convertido automaticamente para o objeto java
		Cliente obj = service.fromDTO(objDTO); // Converte CategoriaDTO em Categoria
		obj = service.insert(obj); // Chama o metodo "insert" do objeto "service" que eh do tipo CategoriaService
		// Http status code = mostra os codigos http padrao 
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		// Esse uri serve pra me dar a url da nova categoria que inseri
		
		return ResponseEntity.created(uri).build();		
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody @Valid ClienteDTO objDTO, @PathVariable Integer id) {
		Cliente obj = service.fromDTO(objDTO);
		
		obj.setId(id); // Comando feito pra garantir que o objeto que eu to passando tenha ID que eu quero atualizar
		obj = service.update(obj);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')") // So admin acessa
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE) // REQUISICAO BASICA = GET
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);		
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')") // So admin acessa
	@RequestMapping(method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<List<ClienteDTO>> findAll() {	
		List <Cliente> list = service.findAll();
		
		// O codigo abaixo converte cada objeto da lista de Cliente em um objeto de ClienteDTO e forma uma lista de ClienteDTO
		// ClienteDTO mostrara apenas os dados que me interessam quando estou imprimindo a lista de Clientes(id,nome)
		List <ClienteDTO> listDTO = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')") // So admin acessa
	@RequestMapping(value="/page",method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(name="page",defaultValue="0") Integer page, 
			@RequestParam(name="linesPerPage",defaultValue="24") Integer linesPerPage, 
			@RequestParam(name="orderBy",defaultValue="nome") String orderBy, 
			@RequestParam(name="direction",defaultValue="ASC") String direction) // FIM DOS PARAMETROS DO METODO
	{	
		// defaultValue = valor padrao -> se nao for informado nenhum valor, assume o valor padrao
		// defaultValue do direction pode ser ASC(ascendente) ou DESC(descendente)
		// defaultValue do orderBy = por qual parametro devo ordenar as paginas
		// defaultValue do linesPerPage = numero de Clientes informadas por pagina 
		// Essa funcao faz a paginacao (divisao de Cliente por pagina)
		
		Page<Cliente> list = service.findPage(page,linesPerPage,orderBy,direction);
		
		// O codigo abaixo converte cada objeto da lista de Cliente em um objeto de ClienteDTO e forma uma lista de ClienteDTO
		// ClienteDTO mostrara apenas os dados que me interessam quando estou imprimindo a lista de Clientes(id,nome)
		Page<ClienteDTO> listDTO = list.map(obj -> new ClienteDTO(obj)); // Retirei "stream" e "collect"
		return ResponseEntity.ok().body(listDTO);
	}
	
	@RequestMapping(value="/picture",method=RequestMethod.POST) // Faz com que seja um metodo POST
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name="file") MultipartFile file) {
		URI uri = service.uploadProfilePicture(file); // Faz upload da imagem e tem como resposta a URI da imagem
	
		return ResponseEntity.created(uri).build();	// Retorna resposta HTTP 201 (recurso criado) e retorna a URI que esta no cabecalho
	}
}
