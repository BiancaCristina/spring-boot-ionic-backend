package br.ufu.facom.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
	
}
