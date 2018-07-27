package br.ufu.facom.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import br.ufu.facom.cursomc.services.validation.ClienteInsert;

@ClienteInsert
public class ClienteNewDTO implements Serializable{
	// Essa classe difere da ClienteDTO porque eh utilizada nas insercoes apenas e nao usa ID como parametro (uma vez que quando insere nao precisa colocar o ID, isso eh feito automaticamente)
	private static final long serialVersionUID = 1L;
	
	// Dados vindos da classe Cliente
	@NotEmpty(message= "Preenchimento obrigatorio")
	@Length(min=5,max=120,message="O tamanho deve ser entre 5 e 120 caracteres")
	private String nome;
	
	@NotEmpty(message= "Preenchimento obrigatorio")
	@Length(min=5,max=120,message="O tamanho deve ser entre 5 e 120 caracteres")
	private String email;
	
	@NotEmpty(message= "Preenchimento obrigatorio")
	private String cpfOUcnpj;
	
	private Integer tipo;
	
	// Dados vindo da classe Endereco
	@NotEmpty(message= "Preenchimento obrigatorio")
	private String logradouro;
	
	@NotEmpty(message= "Preenchimento obrigatorio")
	private String numero;
	
	private String complemento;
	
	private String bairro;
	
	@NotEmpty(message= "Preenchimento obrigatorio")
	private String cep;
	
	// Um endereco precisa da cidade, entao eu pego o ID da cidade correspondente
	private Integer cidadeId;
	
	// Telefones (o 2 e 3 serao opcionais)
	@NotEmpty(message= "Preenchimento obrigatorio")
	private String telefone1;
	
	private String telefone2;
	private String telefone3;
	
	public ClienteNewDTO() {}

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

	public String getCpfOUcnpj() {
		return cpfOUcnpj;
	}

	public void setCpfOUcnpj(String cpfOUcnpj) {
		this.cpfOUcnpj = cpfOUcnpj;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Integer getCidadeId() {
		return cidadeId;
	}

	public void setCidadeId(Integer cidadeId) {
		this.cidadeId = cidadeId;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getTelefone3() {
		return telefone3;
	}

	public void setTelefone3(String telefone3) {
		this.telefone3 = telefone3;
	}
	
	
}
