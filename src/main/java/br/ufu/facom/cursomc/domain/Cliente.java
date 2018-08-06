package br.ufu.facom.cursomc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.ufu.facom.cursomc.domain.enums.Perfil;
import br.ufu.facom.cursomc.domain.enums.TipoCliente;

@Entity
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String nome;
	
	@Column(unique=true) // Alem disso aqui, preciso fazer uma validacao para poder lancar minha propria excecao
	private String email;
	
	private String cpfOUcnpj;
	private Integer tipo; // INTERNAMENTE EH UM INTEGER, EXTERNAMENTE = TIPOCLIENTE
	
	@JsonIgnore // Pra nao aparecer a senha na hora de recuperar os dados no sistema
	private String senha;
	
	@OneToMany(mappedBy="cliente",cascade=CascadeType.ALL)
	// O cascade (acao em cascata) faz com que toda operacao que modifica o cliente, reflete em cascata nos enderecos.
	// O cascade tambem impede que a delecao seja barrada por causa dos enderecos, ou seja, um cliente pode ser apagado mesmo tendo enderecos (so nao pode ser apagado caso tenha pedidos)
	private List<Endereco> enderecos = new ArrayList<>();
	
	// Como esse Set<String> nao vem de outra classe, o mapeamento ocorre de forma diferente
	@ElementCollection
	// @ElementCollection = serve para mapear uma coleção em memória (no caso a lista de telefones) para uma tabela no banco de dados relacional.Assim, se você coloca essa anotação sobre uma lista de strings (como foi o caso), será criada uma tabela contendo dois campos: a chave estrangeira e o valor do string.
	@CollectionTable(name="TELEFONE")
	private Set<String> telefones = new HashSet<>(); // Set = colecao(List) sem repeticao
	
	@ElementCollection(fetch=FetchType.EAGER) // Faz a busca de todos os perfis e traz eles juntos
	@CollectionTable(name="PERFIS")
	private Set<Integer> perfis = new HashSet<>(); // Eh Integer porque guarda apenas aquela ID do Enum "Perfil"
	
	@JsonIgnore // Cliente nao serializa pedidos!
	@OneToMany(mappedBy="cliente")
	private List<Pedido> pedidos = new ArrayList<>();
	
	public Cliente() {
		// Todo mundo eh CLIENTE, nem todo mundo eh ADMIN
		addPerfil(Perfil.CLIENTE); 
	}

	public Cliente(Integer id, String nome, String email, String cpfOUcnpj, TipoCliente tipo, String senha) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpfOUcnpj = cpfOUcnpj;
		this.tipo = (tipo==null) ? null : tipo.getCodigo(); // Significa: Se tipo==null, entao atribuo null, caso contrario pego o codigo
		this.senha = senha;
		
		// Todo mundo eh CLIENTE, nem todo mundo eh ADMIN
		addPerfil(Perfil.CLIENTE); 
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

	public String getCpfOUcnpj() {
		return cpfOUcnpj;
	}

	public void setCpfOUcnpj(String cpfOUcnpj) {
		this.cpfOUcnpj = cpfOUcnpj;
	}

	public TipoCliente getTipo() {
		// USA OPERACAO DO TIPOCLIENTE
		return TipoCliente.toEnum(tipo);
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCodigo();
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Set<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
	}
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Set<Perfil> getPerfis(){
		// Esse comando converte as IDs (Integer) do Set perfis em um Set do tipo Perfil
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCodigo()); // Armazena apenas ID do Perfil
	}
	
	// HASH CODE E EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
