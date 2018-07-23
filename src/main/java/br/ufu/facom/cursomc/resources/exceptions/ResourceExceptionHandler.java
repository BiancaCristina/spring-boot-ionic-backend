package br.ufu.facom.cursomc.resources.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.ufu.facom.cursomc.services.exceptions.DataIntegrityException;
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
	
	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request){
		StandardError err = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(),System.currentTimeMillis());
		// HttpStatus.BAD_REQUEST = error 400
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validatio(MethodArgumentNotValidException e, HttpServletRequest request){
		ValidationError err = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de validacao",System.currentTimeMillis());
		// HttpStatus.BAD_REQUEST = error 400
		
		// Percorre a lista de erros do "MethodArgumentNotValidException" e pega apenas a mensagem do erro
		for(FieldError x: e.getBindingResult().getFieldErrors()) // Para cada objeto x (do tipo Field Error) da lista "e", faco:
		{
			err.addError(x.getField(), x.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
}
