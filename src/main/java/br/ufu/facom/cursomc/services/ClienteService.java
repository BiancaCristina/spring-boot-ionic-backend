package br.ufu.facom.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.repositories.ClienteRepository;
import br.ufu.facom.cursomc.services.exceptions.*;

@Service
public class ClienteService {
	
	@Autowired // Isso faz com que a dependencia seja automaticamente instanciada pelo String
	private ClienteRepository repo; 
		
	public Cliente buscar(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		// TRATAMENTO DE ERRO PARA CASO NAO EXISTA OBJETO
		return obj.orElseThrow( () -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
}
