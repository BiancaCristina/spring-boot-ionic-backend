package br.ufu.facom.cursomc.domain;

import java.util.Date;

import javax.persistence.Entity;

import br.ufu.facom.cursomc.domain.enums.EstadoPagamento;

@Entity // Nao precisa colocar a parte do strategy porque eh herdado! 
public class PagamentoComBoleto extends Pagamento{
	//Nao precisa colocar o implements Serializable, mas precisa definir o serialVersionUID
	private static final long serialVersionUID = 1L;
	
	private Date dataVencimento;
	private Date dataPagamento;
	
	public PagamentoComBoleto() {}

	public PagamentoComBoleto(Integer id, EstadoPagamento estado, Pedido pedido, Date dataVencimento, Date dataPagamento) {
		super(id, estado, pedido);
		this.dataPagamento = dataPagamento;
		this.dataVencimento = dataVencimento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
}
