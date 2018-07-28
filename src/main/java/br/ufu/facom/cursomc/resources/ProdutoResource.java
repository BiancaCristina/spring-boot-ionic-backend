package br.ufu.facom.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufu.facom.cursomc.domain.Produto;
import br.ufu.facom.cursomc.dto.ProdutoDTO;
import br.ufu.facom.cursomc.resources.utils.URL;
import br.ufu.facom.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;
	
	@RequestMapping(value="/{id}",method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		// @PathVariable = faz com que {id} corresponda ao id passado como parametro
		// ResponseEntity = armazena varias info de uma resposta HTTP para um servico REST
			
		Produto obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping(method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(name="nome",defaultValue="") String nome, 
			@RequestParam(name="categorias",defaultValue="") String categorias, 
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
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		Page<Produto> list = service.search(nomeDecoded,ids,page,linesPerPage,orderBy,direction);
		
		// O codigo abaixo converte cada objeto da lista de Produto em um objeto de ProdutoDTO e forma uma lista de ProdutoDTO
		// ProdutoDTO mostrara apenas os dados que me interessam quando estou imprimindo a lista de Produtos(id,nome)
		Page<ProdutoDTO> listDTO = list.map(obj -> new ProdutoDTO(obj)); // Retirei "stream" e "collect"
		return ResponseEntity.ok().body(listDTO);
	}
}
