package br.ufu.facom.cursomc.resources.exceptions;

import java.io.Serializable;

public class StandardError implements Serializable {
	// StandardError = erro padrao
	// Cria-se essa classe para retornar o erro com info completas!
	private static final long serialVersionUID = 1L;
	
	private Integer status; // status HTTP do erro
	private String msg; // Mensagem de erro
	private Long timeStamp; // instante que ocorreu o erro
	
	public StandardError(Integer status, String msg, Long timeStamp) {
		super();
		this.status = status;
		this.msg = msg;
		this.timeStamp = timeStamp;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	
	
}
