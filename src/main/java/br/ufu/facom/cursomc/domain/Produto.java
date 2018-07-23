package br.ufu.facom.cursomc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Produto implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private double preco;
	
	@JsonIgnore // O "BackReference" faz assim: "Em Categoria eu ja busquei os objetos, aqui eu nao preciso buscar mais -> omite a lista de Categoria pra cada Produto" 
	@ManyToMany 
	@JoinTable(name = "PRODUTO_CATEGORIA",
		joinColumns = @JoinColumn(name="produto_id"), 
		inverseJoinColumns = @JoinColumn(name="categoria_id")
	) // JoinTable faz a terceira tabela do N:N
	private List<Categoria> categorias = new ArrayList<>();
	
	@JsonIgnore // Ignora a lista de itens porque o que eh importante eh quais Produtos eu obtenho a partir de ItemPedido e nao quais ItemPedido eu obtenho a partir do Produto
				// Logo, Produto nao serializa ItemPedido
	@OneToMany(mappedBy="id.produto") // Eh "id.produto" porque esta mapeado na variavel produtos de ItemProduto
	private Set<ItemPedido> itens = new HashSet<>();
	
	public Produto() {}

	public Produto(Integer id, String nome, double preco) {
		super();
		this.id = id;
		this.nome = nome;
		this.preco = preco;
	}
	
	// Tudo que comeca com get, eh automaticamente serializado, logo, caso nao modifique o Json aqui, isso geraria uma serializacao
	// Dessa forma, como o ItemPedido nao faz serializacao (e a ItemPedido eh responsavel pela comunicao entre Produto e Pedido), nao faz sentido que esse metodo serialize
	// Caso deixasse serializar, geraria uma referencia ciclica
	// Logo, eh preciso ignorar "getPedidos" na serializacao
	@JsonIgnore
	public List<Pedido> getPedidos(){
		List<Pedido> lista = new ArrayList<>();
		
		for(ItemPedido i: itens)
		{
			lista.add(i.getPedido());
		}
		
		return lista;
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

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	
	public Set<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(Set<ItemPedido> itens) {
		this.itens = itens;
	}
	
	// HASHCODE AND EQUALS
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
