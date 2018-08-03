package br.ufu.facom.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment env; 
	
	// Essa variavel define quais caminhos nao precisam estar bloqueados pro publico
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**",
	};
	
	// Essa variavel define quais caminhos nao precisam estar bloqueados pro publico (SOMENTE PRA LEITURA)
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		// O comando abaixo pega os profiles ativos do meu programa e verifica se eu estou no profile de test
		// Se eu estiver, eu quero acessar o H2 e preciso fazer o comando "http.headers().frameOptions().disable();"
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable(); // Libera o acesso ao H2
        }
		
		// O comando abaixo ativa o metodo "corsConfigurationSource" e desabilita a protecao a CSRF 
		http.cors().and().csrf().disable();
		
		// O comando abaixo diz que eh pra permitir todos os caminhos do vetor "PUBLIC_MATCHERS"
		// Alem disso, esse comando determina que todos os outros precisam de permissao
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll() // Isso faz com que os caminhos listados em PUBLIC_MATCHERS_GET sao apenas pra leitura, o user nao pode inserir/deletar nada!
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			.anyRequest()
			.authenticated();
		
		// O comando abaixo impede que o sistema crie sessoes de usuario
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		// Esse metodo permite o acesso aos meus endpoints por multiplas fontes com as configs basicas
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}
