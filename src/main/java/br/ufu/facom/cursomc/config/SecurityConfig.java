package br.ufu.facom.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.ufu.facom.cursomc.security.JWTAuthenticationFilter;
import br.ufu.facom.cursomc.security.JWTAuthorizationFilter;
import br.ufu.facom.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment env; 
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	// Essa variavel define quais caminhos nao precisam estar bloqueados pro publico
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**",
	};
	
	// Essa variavel define quais caminhos nao precisam estar bloqueados pro publico (SOMENTE PRA LEITURA)
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**"
	};
	
	// Essa variavel define quais endpoints permitirei apenas no POST
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/clientes/**",
			"/auth/forgot/**"
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
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() // Permite o POST no vetor PUBLIC_MATCHERS_POST
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll() // Isso faz com que os caminhos listados em PUBLIC_MATCHERS_GET sao apenas pra leitura, o user nao pode inserir/deletar nada!
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			.anyRequest()
			.authenticated();
		
		// O comando abaixo registra o meu JWTAuthenticationFilter (Filtro de Autenticacao)
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		// Fim do comando
		
		// O comando abaixo registra meu JWTAuthorizationFilter (Filtro de Autorizacao)
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		// Fim do comando
		
		// O comando abaixo impede que o sistema crie sessoes de usuario
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Fim do comando
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Define os mecanismos de autenticacao
		// Define quem eh o UserDetailsService e tambem como a senha eh criptografada
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		// Esse metodo permite o acesso aos meus endpoints por multiplas fontes com as configs basicas
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		// Instancializa um BCryptPasswordEncoder que eh gerado pra senhas
		return new BCryptPasswordEncoder();
	}
}
