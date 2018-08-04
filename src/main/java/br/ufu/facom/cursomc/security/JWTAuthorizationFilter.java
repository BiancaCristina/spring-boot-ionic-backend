package br.ufu.facom.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	private JWTUtil jwtUtil;
	private UserDetailsService userDetailsService; // Usado para buscar o usuario quando quero checar o token
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
		// Metodo padrao que eh executado antes da requisicao continuar
		
		String header = request.getHeader("Authorization"); // Recebe o token
		
		if (header != null && header.startsWith("Bearer ")) {
			// Se o header nao for nulo e comecar com "Bearer "
			
			// O comando abaixo pega apenas o token de autorizacao sem o "Bearer "
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));
			// Fim do comando

			if (auth != null) {				
				// Caso o token seja invalido, entao auth sera null
				// Assim, caso auth nao seja null, liberamos o acesso com o comando abaixo:
				SecurityContextHolder.getContext().setAuthentication(auth); 
				// Fim do comando		
			}
		}
		
		// Esse comando continua fazendo a requisicao normalmente depois dos testes
		chain.doFilter(request, response);
		// Fim do comando
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if(jwtUtil.tokenValido(token)) {
			// Se o token for valido
			String username  = jwtUtil.getUsername(token);
			UserDetails user = userDetailsService.loadUserByUsername(username);
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
	
}
