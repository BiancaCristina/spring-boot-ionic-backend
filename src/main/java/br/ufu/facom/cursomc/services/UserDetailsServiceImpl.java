package br.ufu.facom.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.repositories.ClienteRepository;
import br.ufu.facom.cursomc.security.UserSS;

public class UserDetailsServiceImpl implements UserDetailsService {
	// Essa classe faz a busca do usuario
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// Permite buscar um usuario pelo seu email
		Cliente cli = repo.findByEmail(email);
		
		if (cli == null) {
			// Se o cliente for null, eh porque esse email nao esta cadastrado
			// Caso entre nesse if, lanca uma excecao informando que o username(email) nao foi encontrado
			throw new UsernameNotFoundException(email); 
		}
		
		return new UserSS(cli.getId(),cli.getEmail(),cli.getSenha(),cli.getPerfis());
	}
	
}
