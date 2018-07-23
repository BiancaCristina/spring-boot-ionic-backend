package br.ufu.facom.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufu.facom.cursomc.domain.Pedido;
import br.ufu.facom.cursomc.repositories.PedidoRepository;
import br.ufu.facom.cursomc.services.exceptions.*;

@Service
public class PedidoService {
	
	@Autowired // Isso faz com que a dependencia seja automaticamente instanciada pelo String
	private PedidoRepository repo; 
		
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		// TRATAMENTO DE ERRO PARA CASO NAO EXISTA OBJETO
		return obj.orElseThrow( () -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
}
