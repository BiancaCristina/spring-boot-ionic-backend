package br.ufu.facom.cursomc.resources.exceptions;

import java.io.Serializable;

public class StandardError implements Serializable {
	// StandardError = erro padrao
	// Cria-se essa classe para retornar o erro com info completas!
	private static final long serialVersionUID = 1L;
	
	private Long timeStamp; // instante que ocorreu o erro
	private Integer status; // status HTTP do erro
	private String error; // indica o erro HTTP
	private String message; // Mensagem de erro
	private String path; // indica o caminho do erro
	
	public StandardError(Long timeStamp, Integer status, String error, String message, String path) {
		super();
		this.timeStamp = timeStamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	

}
