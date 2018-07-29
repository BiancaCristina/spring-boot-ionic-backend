package br.ufu.facom.cursomc.domain;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ItemPedido implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// Chave composta entre Produto e Pedido = ItemPedidoPL
	private Double desconto;
	private Integer quantidade;
	private Double preco;
	
	@JsonIgnore // Essa parte sera ignorada, ou seja,
				// ItemPedido nao serializa Pedido e;
				// ItemPedido nao serializa Produto.
	@EmbeddedId // Isso significa que "id" eh um ID embutido em uma classe auxiliar
	private ItemPedidoPK id = new ItemPedidoPK();
	// Esse ID eh composto
	
	public ItemPedido() {}
	
	public ItemPedido(Double desconto, Integer quantidade, Double preco, Pedido pedido, Produto produto) {
		// Nao se passa ItemPedidoPK como parametro porque nao faz sentido pra quem vai usar o app
		super();
		this.desconto = desconto;
		this.quantidade = quantidade;
		this.preco = preco;	
		
		// Usa o Pedido e Produto para setar e inicializar o ID
		id.setPedido(pedido);
		id.setProduto(produto);				
	}
	
	public double getSubTotal() {
		// Eu uso o "get" aqui para que o Json serialize esse metodo e o subTotal apareca no POSTMAN
		return (preco - desconto) * quantidade;
	}
	
	// Assim como ocorreu em "Produto", os metodos get estao sendo serializados, entao precisa retirar isso	
	@JsonIgnore
	public Pedido getPedido() {
		return id.getPedido();
	}
	
	public void setPedido(Pedido pedido) {
		this.id.setPedido(pedido);
	}
	
	// Aqui nao precisa ignorar o Produto para que ele apareca
	public Produto getProduto() {
		return id.getProduto();
	}
	
	public void setProduto(Produto produto) {
		this.id.setProduto(produto);
	}
	
	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public ItemPedidoPK getId() {
		return id;
	}

	public void setId(ItemPedidoPK id) {
		this.id = id;
	}

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
		ItemPedido other = (ItemPedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
