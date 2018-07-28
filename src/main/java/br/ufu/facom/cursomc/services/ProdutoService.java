package br.ufu.facom.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.ufu.facom.cursomc.domain.Categoria;
import br.ufu.facom.cursomc.domain.Produto;
import br.ufu.facom.cursomc.repositories.CategoriaRepository;
import br.ufu.facom.cursomc.repositories.ProdutoRepository;
import br.ufu.facom.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired // Isso faz com que a dependencia seja automaticamente instanciada pelo String
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	

	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		// TRATAMENTO DE ERRO PARA CASO NAO EXISTA OBJETO
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	public Page<Produto> search(String nome, List<Integer> ids,Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),orderBy);
		
		List<Categoria> categorias = categoriaRepository.findAllById(ids); // Gera a lista de categorias a partir da lista de IDS (ids)
		
		return repo.search(nome, categorias, pageRequest);
		
	}
}
