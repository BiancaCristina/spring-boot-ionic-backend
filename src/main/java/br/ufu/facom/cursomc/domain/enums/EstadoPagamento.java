package br.ufu.facom.cursomc.domain.enums;

public enum EstadoPagamento {
	PENDENTE(1,"Pagamento pendente"),
	QUITADO(2,"Pagamento quitado"),
	CANCELADO(3,"Pagamento cancelado");
	
	private int codigo;
	private String descricao;
	
	private EstadoPagamento(int cod, String descricao) {
		// Construtor de enum eh private
		this.codigo = cod;
		this.descricao = descricao;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static EstadoPagamento toEnum(Integer cod) {
		// Eh static porque esa operacao vai ter que ser possivel mesmo sem instanciar objetos
		if (cod == null) return null;
		
		for(EstadoPagamento i: EstadoPagamento.values()) // Percorre todo o TipoCliente
		{		
			if (cod.equals(i.getCodigo())) return i;			
		}
		
		// CASO O RETURN NAO ACONTECA DENTRO DO FOR, O ID EH INVALIDO, ENTAO:
		throw new IllegalArgumentException("Id invalido: " + cod);
	}
}
