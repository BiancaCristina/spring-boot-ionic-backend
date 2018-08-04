package br.ufu.facom.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component // Isso faz com que essa classe seja injetada em outras como componente
public class JWTUtil {
	
	@Value("${jwt.secret}")
	private String secret; // Armazena a string que corresponde ao jwt.secret
	
	@Value("${jwt.expiration}")
	private Long expiration; // Armazena o tempo que o token deve expirar pegando o valor do jwt.expiration
	
	public String generateToken(String username) {
		// Metodo que vai gerar o token
		return Jwts.builder()
			   .setSubject(username)
			   .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Define quando o token vai expirar baseado no tempo do sistema + tempo de expiracao
			   .signWith(SignatureAlgorithm.HS512, secret.getBytes())
			   .compact();
	}
	
	public boolean tokenValido(String token) {
		// Claim armazena as reinvidicacoes do token
		Claims claims = getClaims(token);
		
		if (claims != null) {
			// Se claims for diferente de null
			String username = claims.getSubject(); // Retorna username
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis()); // Pega a data atual
			
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				// now.before = meu instante atual eh anterior a minha data de expiracao
				return true;
			}
		}
		
		return false;
	}
	
	public String getUsername(String token) {
		// Claim armazena as reinvidicacoes do token
		Claims claims = getClaims(token);
		
		if (claims != null) {
			// Se claims for diferente de null
			return claims.getSubject(); // Retorna username
		}
		
		return null;
	}

	private Claims getClaims(String token) {
		// Metodo que pega os claims a partir de um token
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
		}
		
		catch (Exception e) {
			// Se o token era invalido ou deu algum problema, retorno null
			return null;
		}
	}
}
