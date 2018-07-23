package br.ufu.facom.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import br.ufu.facom.cursomc.domain.Categoria;

public class CategoriaDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Essa classe define quais dados eu quero trafegar quando faco operacoes basicas de Categoria
	// Funciona QUASE como uma "subclasse" de Categoria, a qual instancia apenas os parametros necessarios que eu quero transferir
	// A criacao de DTO "otimiza" o sistema porque, por exemplo, CategoriaDTO eh uma classe bem menos complexa do que Categoria, logo, quando uso esse objeto, preciso usar uma operacao bem menos complexa
	private Integer id;
	
	@NotEmpty(message="Preenchimento obrigatorio") // Validar o nome, definindo que nao pode ser vazio
	@Length(min=5,max=80,message="O tamanho deve ser entre 5 e 80 caracteres") // Define tamanho do nome (entre 5 e 80 caracteres)
	private String nome;
	
	public CategoriaDTO() {}
	
	public CategoriaDTO (Categoria obj) {
		id = obj.getId();
		nome = obj.getNome();
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
}
