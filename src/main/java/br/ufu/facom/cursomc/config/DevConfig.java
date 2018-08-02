package br.ufu.facom.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.ufu.facom.cursomc.services.DBService;
import br.ufu.facom.cursomc.services.EmailService;
import br.ufu.facom.cursomc.services.SmtpEmailService;

// Classe que guarda as configs de teste

@Configuration
@Profile("dev") // Identifica o profile test
public class DevConfig {
	
	@Autowired
	private DBService dbService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")	
	// Essa anotacao faz com que a variavel "strategy" receba o valor dessa chave (a qual se encontra no arquivo aaplication-dev.properties)
	// Essa chave determina o que acontece quando abroo BD
	private String strategy;
	
	@Bean
	public boolean instanciateDataBase() throws ParseException {
		
		if (!"create".equals(strategy)) return false; // Se nao tiver que recriar o BD, retorna falso e nao reinicializa o BD
		
		// Caso seja == create, entao reinicializa o BD
		
		dbService.instantiateTestDataBase();
		
		return true; // So eh um metodo boolean porque nao pode ser void, dai sempre retorna true
	}
	
	@Bean // Instancia a classe Smtp
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
