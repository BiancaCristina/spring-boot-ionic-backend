package br.ufu.facom.cursomc.domain;

import br.ufu.facom.cursomc.domain.enums.EstadoPagamento;

public class PagamentoComCartao extends Pagamento{
	//Nao precisa colocar o implements Serializable, mas precisa definir o serialVersionUID
	private static final long serialVersionUID = 1L;
	
	private Integer numeroDeParcelas;
	
	public PagamentoComCartao() {}

	public PagamentoComCartao(Integer id, EstadoPagamento estado, Pedido pedido, Integer numeroDeParcelas) {
		super(id, estado, pedido);
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public Integer getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(Integer numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}
	
	
}
