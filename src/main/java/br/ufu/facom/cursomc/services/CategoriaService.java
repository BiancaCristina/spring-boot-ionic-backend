package br.ufu.facom.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.ufu.facom.cursomc.domain.Categoria;
import br.ufu.facom.cursomc.repositories.CategoriaRepository;
import br.ufu.facom.cursomc.services.exceptions.*;

@Service
public class CategoriaService {
	
	@Autowired // Isso faz com que a dependencia seja automaticamente instanciada pelo String
	private CategoriaRepository repo; 
		
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		// TRATAMENTO DE ERRO PARA CASO NAO EXISTA OBJETO
		return obj.orElseThrow( () -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert (Categoria obj) {
		obj.setId(null);
		// Se esse obj tiver algum ID, o metodo save vai considerar que eh uma atualizacao e nao uma insercao
		// Logo, o comando acima garante que estou inserindo um objeto do tipo Categoria com ID nulo
		return repo.save(obj);
	}
	
	public Categoria update (Categoria obj) {
		// A diferenca entre update e inser eh que em um eu preciso garantir que ID seja nulo (insert),
		// No outro, caso o ID nao seja nulo, entao irei fazer uma atualizacao em um ID que ja existe no meu BD
		
		// Verifica se o objeto existe
		this.find(obj.getId()); // Caso o objeto nao exista, esse metodo ja lanca a excecao!
		
		return repo.save(obj);
	}
	
	public void delete(Integer id) {
		// Verifica se ID existe
		this.find(id);
		
		try 
		{			
			repo.deleteById(id);
		}
		
		catch (DataIntegrityViolationException e)
		{
			throw new DataIntegrityException("Nao eh possivel excluir uma categoria que possui produtos");
		}
		
	}
}
