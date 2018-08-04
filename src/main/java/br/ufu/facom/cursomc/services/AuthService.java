package br.ufu.facom.cursomc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.repositories.ClienteRepository;
import br.ufu.facom.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random();
	
	public void sendNewPassword(String email) {
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if (cliente == null) {
			// Cliente nao existe
			throw new ObjectNotFoundException("Email nao encontrado");
		}
		
		String newPass = newPassword(); // Nova senha gerada pelo sistema
		cliente.setSenha(pe.encode(newPass));
		
		clienteRepository.save(cliente);
		emailService. sendNewPasswordEmail(cliente, newPass);

	}

	private String newPassword() {
		char[] vet = new char[10];
		
		for(int i=0;i<10;i++) {
			// Gera senha aleatoria de 10 caracteres
			vet[i] = randomChar();
		}
		
		return new String(vet); // Gera uma string a partir do vetor "vet[10]"
	}

	private char randomChar() {
		// Esse metodo gera os caracteres aleatorios
		// Tabela unicode = tabela que diz codigos dos char
		
		int opcao = rand.nextInt(3); // Gera numero inteiro de 0 ate 2
		if (opcao == 0) {
			// Se opcao == 0, gera um digito
			return (char) (rand.nextInt(10) + 48); // Como o 0 = 48, entao esse comando vai gerar um inteiro de 0 a 9
		}
		
		else if (opcao == 1) {
			// Se opcao == 1, gera letra maiscula
			return (char) (rand.nextInt(26) + 65); // Gera de 'A' ate 'Z'
		}
		
		else {
			// No caso, a opcao == 2 e gera letra minuscula
			return (char) (rand.nextInt(26) + 97);  // Gera de 'a' ate 'z'
		}
	}
}
