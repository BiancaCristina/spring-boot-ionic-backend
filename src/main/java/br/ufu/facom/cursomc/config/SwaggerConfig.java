package br.ufu.facom.cursomc.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Header;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	// As variaveis abaixo personalizam as mensagens na documentacao
	private final ResponseMessage m201 = customMessage1();
	private final ResponseMessage m204put = simpleMessage(204, "Atualização ok");
	private final ResponseMessage m204del = simpleMessage(204, "Deleção ok");
	private final ResponseMessage m403 = simpleMessage(403, "Não autorizado");
	private final ResponseMessage m404 = simpleMessage(404, "Não encontrado");
	private final ResponseMessage m422 = simpleMessage(422, "Erro de validação");
	private final ResponseMessage m500 = simpleMessage(500, "Erro inesperado");
	// Fim das variaveis
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				
				.useDefaultResponseMessages(false) // Desativa as mensagens padroes
				// Os comandos abaixo definem qual operacao recebe qual tipo de mensagem
				.globalResponseMessage(RequestMethod.GET, Arrays.asList(m403, m404, m500))
				.globalResponseMessage(RequestMethod.POST, Arrays.asList(m201, m403, m422, m500))
				.globalResponseMessage(RequestMethod.PUT, Arrays.asList(m204put, m403, m404, m422, m500))
				.globalResponseMessage(RequestMethod.DELETE, Arrays.asList(m204del, m403, m404, m500))
				// Fim dos comandos
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.ufu.facom.cursomc.resources")) // Procura apenas no meu pacote Resources
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo()); // Mostra as infos especificadas no metodo "apiInfo()"
	}

	private ApiInfo apiInfo() {
		// Esse metodo detalha infos da documentacao
		return new ApiInfo("API Curso Spring Boot Ionic",
				"Esta API é utilizada no curso de Spring Boot ministrada pelo prof. Nelio Alves. API foi refeita por Bianca Cristina da Silva para fins educacionais", 
				"Versão 1.0",
				"https://www.udemy.com/terms",
				new Contact("Nelio Alves", "udemy.com/user/nelio-alves", "nelio.cursos@gmail.com"),
				"Permitido uso para estudantes", 
				"https://www.udemy.com/terms",
				Collections.emptyList() // Vendor																					// Extensions
		);
	}

	private ResponseMessage simpleMessage(int code, String msg) {
		// Esse metodo retorna uma mensagem simples no status (so nao esta configurado para o 201, que usa o metodo custom)
		return new ResponseMessageBuilder().code(code).message(msg).build();
	}
	
	private ResponseMessage customMessage1() {
		// Esse metodo avisa que ao realizar uma operacao que tem esse custom, ele vai retornar: header, location e string 
		// Eh mais utilizado para o POST (inserir algo novo na API) -> gera um 201 CREATED
		Map<String, Header> map = new HashMap<>();
		map.put("location", new Header("location", "URI do novo recurso", new ModelRef("string")));
	
		return new ResponseMessageBuilder()
		.code(201)
		.message("Recurso criado")
		.headersWithDescription(map)
		.build();
	}
}