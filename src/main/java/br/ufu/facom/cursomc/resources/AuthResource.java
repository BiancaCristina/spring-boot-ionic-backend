package br.ufu.facom.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.ufu.facom.cursomc.dto.EmailDTO;
import br.ufu.facom.cursomc.security.JWTUtil;
import br.ufu.facom.cursomc.security.UserSS;
import br.ufu.facom.cursomc.services.AuthService;
import br.ufu.facom.cursomc.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	// Essa classe eh responsavel por disponibilizar endpoints relacionados a autorizacao e autenticacao
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service; 
	
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		// Esse metodo renova o token do usuario LOGADO 
		UserSS user = UserService.authenticated(); // Pega usuario logado
		String token = jwtUtil.generateToken(user.getUsername()); // Gera novo token
		response.addHeader("Authorization", "Bearer " + token); // Adicionad token
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDTO) {
		service.sendNewPassword(objDTO.getEmail());
		return ResponseEntity.noContent().build();
	}
}
