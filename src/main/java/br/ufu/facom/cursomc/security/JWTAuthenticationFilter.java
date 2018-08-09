package br.ufu.facom.cursomc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.ufu.facom.cursomc.dto.CredenciaisDTO;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	// Essa classe eh responsavel por filtrar a autenticacao e verificar se o username e senha fornecidos estao corretos

	private AuthenticationManager authenticationManager;

	private JWTUtil jwtUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		// Metodo da tentativa de login (autenticacao)

		try {
			// O comando abaixo tenta pegar os valores na requisicao(req) e converte em um CredenciaisDTO
			CredenciaisDTO creds = new ObjectMapper().readValue(req.getInputStream(), CredenciaisDTO.class);
			// Fim do comando

			// O comando abaixo cria um usuario de autenticacao a partir dos dados do objeto CredenciasDTO
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getEmail(),
					creds.getSenha(), new ArrayList<>());
			// Fim do comando

			// Esse comando verifica se o usuario e senha sao validos usando o objeto" authToken" e minha implementacao do UserDetailsService/UserSS
			Authentication auth = authenticationManager.authenticate(authToken);
			// Fim do comando

			// Retorna a autenticacao
			return auth;
		} catch (IOException e) {
			// Lanca excecao de tempo de tentativa de autenticacao
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		// GetPrincipal = pega o username
		String username = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username);
		res.addHeader("Authorization", "Bearer " + token);
		
		// Expoe meu header personalizado (isso evita problema de CORS)
		 res.addHeader("access-control-expose-headers", "Authorization");
	}
	
	// O codigo abaixo foi acrescentado depois para deixar adequado com a versao 2.0 do Spring Boot
	// Isso corrige o erro de dar o codigo de erro inadequado quando a autenticacao falha
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {
			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json());
		}

		private String json() {
			long date = new Date().getTime();
			return "{\"timestamp\": " + date + ", " + "\"status\": 401, " + "\"error\": \"Não autorizado\", "
					+ "\"message\": \"Email ou senha inválidos\", " + "\"path\": \"/login\"}";
		}
	}
}
