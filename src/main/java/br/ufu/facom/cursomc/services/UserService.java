package br.ufu.facom.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import br.ufu.facom.cursomc.security.UserSS;

public class UserService {
	
	public static UserSS authenticated() {
		try {
		// O comando abaixo retorna o usuario que estiver logado no sistema
		return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		
		catch (Exception e) {
			// Caso haja algum problema, retorna null 
			return null;
		}
	}
}
