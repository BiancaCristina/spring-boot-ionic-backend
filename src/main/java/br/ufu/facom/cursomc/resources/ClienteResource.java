package br.ufu.facom.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.services.ClienteService;

@RestController
@RequestMapping(value= "/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@RequestMapping(value="/{id}",method= RequestMethod.GET) // REQUISICAO BASICA = GET
	public ResponseEntity<?> find(@PathVariable Integer id) {
		// @PathVariable = faz com que {id} corresponda ao id passado como parametro
		// ResponseEntity = armazena varias info de uma resposta HTTP para um servico REST
			// O "?" significa que pode ser qualquer tipo, uma vez que a busca pode ser bem sucedida ou nao
		
		Cliente obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}
}
