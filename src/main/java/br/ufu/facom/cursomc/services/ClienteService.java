package br.ufu.facom.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.dto.ClienteDTO;
import br.ufu.facom.cursomc.repositories.ClienteRepository;
import br.ufu.facom.cursomc.services.exceptions.DataIntegrityException;
import br.ufu.facom.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired // Isso faz com que a dependencia seja automaticamente instanciada pelo String
	private ClienteRepository repo; 
		
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		// TRATAMENTO DE ERRO PARA CASO NAO EXISTA OBJETO
		return obj.orElseThrow( () -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente update (Cliente obj) {
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
	
	public List<Cliente> findAll(){
		// Esse metodo retorna todas as categorias quando digitar o URL "/categoria"
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		// Metodo auxiliar que instancia um objeto do tipo Cliente a partir de um objeto do tipo ClienteDTO
		//return new Cliente(objDTO.getId(),objDTO.getNome());
		throw new UnsupportedOperationException(); // Solucao temporaria, tenho que implementar aqui depois
	}
}
