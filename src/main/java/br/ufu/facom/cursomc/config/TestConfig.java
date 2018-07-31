package br.ufu.facom.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.ufu.facom.cursomc.services.DBService;
import br.ufu.facom.cursomc.services.EmailService;
import br.ufu.facom.cursomc.services.MockEmailService;

// Classe que guarda as configs de teste

@Configuration
@Profile("test") // Identifica o profile test
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instanciateDataBase() throws ParseException {
		dbService.instantiateTestDataBase();
		
		return true; // So eh um metodo boolean porque nao pode ser void, dai sempre retorna true
	}
	
	@Bean // Metodos com a anotacao @Bean estao disponiveis como um componente do sistema
		  // Entao, se em outra classe eu faco uma dependencia, tal como a realizada em PedidoService com o EmaiLService, o Spring vai procurar por um componente que pode ser @Bean que retorna uma instancia
		  // No caso citado, a instancia devolvida seria do tipo EmailService
	public EmailService emailService() {
		return new MockEmailService();
	}
}
