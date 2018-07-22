package br.ufu.facom.cursomc.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.ufu.facom.cursomc.services.exceptions.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {
	// Cria-se essa classe como alternativa ao "try-catch" para capturar a excecao de quando o objeto nao existe
	
	@ExceptionHandler(ObjectNotFoundException.class)
	
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request){
		StandardError err = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(),System.currentTimeMillis());
		// HttpStatus.NOT_Found = error 404
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
}
