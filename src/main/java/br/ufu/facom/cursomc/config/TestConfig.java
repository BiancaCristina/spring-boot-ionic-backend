package br.ufu.facom.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.ufu.facom.cursomc.services.DBService;

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
}
