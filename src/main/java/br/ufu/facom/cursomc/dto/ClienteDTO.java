package br.ufu.facom.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.services.validation.ClienteUpdate;

@ClienteUpdate
public class ClienteDTO implements Serializable {
	// Esse DTO difere do ClienteNewDTO porque nao serve para insercoes
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	@NotEmpty(message= "Preenchimento obrigatorio")
	@Length(min=5,max=120,message="O tamanho deve ser entre 5 e 120 caracteres")
	private String nome;
	
	@NotEmpty(message= "Preenchimento obrigatorio")
	@Email(message="Email invalido")
	private String email;
	
	public ClienteDTO() {}
	
	public ClienteDTO (Cliente obj) {
		id = obj.getId();
		nome = obj.getNome();
		email = obj.getEmail();
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	// Nao inclui CPF (assume-se que isso nao pode ser mudado)
	// Nao inclui outros objetos relacionais alem de id, nome,email
	
	
}
