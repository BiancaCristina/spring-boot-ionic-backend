package br.ufu.facom.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufu.facom.cursomc.domain.Categoria;
import br.ufu.facom.cursomc.repositories.CategoriaRepository;
import br.ufu.facom.cursomc.services.exceptions.*;

@Service
public class CategoriaService {
	
	@Autowired // Isso faz com que a dependencia seja automaticamente instanciada pelo String
	private CategoriaRepository repo; 
		
	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		// TRATAMENTO DE ERRO PARA CASO NAO EXISTA OBJETO
		return obj.orElseThrow( () -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
}
