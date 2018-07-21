package br.ufu.facom.cursomc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CursomcApplication {
	// Para alterar porta, vai no resources e coloca: server.port=${port:8081}, 8081 eh a porta nova!
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}
}
