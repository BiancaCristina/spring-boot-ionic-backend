package br.ufu.facom.cursomc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import br.ufu.facom.cursomc.services.S3Service;

@SpringBootApplication
@EnableWebMvc
public class CursomcApplication implements CommandLineRunner{
	// Para alterar porta, vai no resources e coloca: server.port=${port:8081}, 8081 eh a porta nova!
    
	@Autowired
	private S3Service s3Service; // Variavel pra teste
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CursomcApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}


	
	@Override
	public void run(String... args) throws Exception {
		// Array as List = cria Lista automatica e posso colocar quantos elementos eu quiser dentro	
		s3Service.uploadFile("D:\\Users\\Bianca\\Desktop\\Facul\\Curso Udemy #1 (Spring Boot, Ionic, etc)\\Imagens-Teste\\lobo.jpg");
	}	
}
