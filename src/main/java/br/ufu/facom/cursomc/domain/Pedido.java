package br.ufu.facom.cursomc.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Pedido implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@JsonFormat(pattern="dd/MM/yyyy HH:mm") // Usa isso aqui pra formatar a parte "instante"
	private Date instante; // Instante que o pedido foi feito
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="pedido") 
		// Essa parte do cascade eh uma notacao peculiar do JPA
		// O mappedBy realiza o mapeamento 1:1 entre Pedido-Pagamento e garante que a ID do pedido seja igual a ID do pagamento
	private Pagamento pagamento;
	
	@ManyToOne
	@JoinColumn(name="cliente_id")
	private Cliente cliente;
	
	@ManyToOne
	@JoinColumn(name="endereco_de_entrega_id") // So precisa fazer aqui no pedido porque eh uma relacao unidimensional
	private Endereco enderecoDeEntrega;
	
	// Como em ItemPedido a serializacao eh ignorada, entao, automaticamente, Pedido serializa ItemPedido
	@OneToMany(mappedBy="id.pedido") // Eh id.pedido porque foi mapeado na variavel pedidos de ItemPedido
	private Set<ItemPedido> itens = new HashSet<>();
	
	public Pedido() {}

	public Pedido(Integer id, Date instante, Cliente cliente, Endereco enderecoDeEntrega) {
		super();
		this.id = id;
		this.instante = instante;
		// Apaguei pagamento para deixar essa classe independente do pagamento na INICIALIZACAO apenas
		this.cliente = cliente;
		this.enderecoDeEntrega = enderecoDeEntrega;
	}
	
	public double getValorTotal() {
		double soma = 0;
		
		for(ItemPedido ip: itens) {
			// Para cada objeto do tipo ItemPedido da minha lista "itens", faco a soma de seus subTotal e acrescento na variavel soma
			soma = soma + ip.getSubTotal();		
		}
		
		return soma;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getInstante() {
		return instante;
	}

	public void setInstante(Date instante) {
		this.instante = instante;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Endereco getEnderecoDeEntrega() {
		return enderecoDeEntrega;
	}

	public void setEnderecoDeEntrega(Endereco enderecoDeEntrega) {
		this.enderecoDeEntrega = enderecoDeEntrega;
	}
	
	public Set<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(Set<ItemPedido> itens) {
		this.itens = itens;
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
		Pedido other = (Pedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")); // Formar o preco e subtotal em forma de dinheiro na localidade BR
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); // Usado para formatar o instante
		
		StringBuilder builder = new StringBuilder();
		builder.append("Pedido numero: ");
		builder.append(getId());
		builder.append(", Instante: ");
		builder.append(sdf.format(getInstante()));
		builder.append(",Cliente: ");
		builder.append(getCliente().getNome());
		builder.append(", Situacao do pagamento: ");
		builder.append(getPagamento().getEstado().getDescricao());
		builder.append("\nDetalhes:\n");
		
		for (ItemPedido ip: getItens()) {
			builder.append(ip.toString());
			
		}
		
		builder.append("Valor Total: ");
		builder.append(nf.format(getValorTotal()));
		
		return builder.toString();
	}		
	
	
}
