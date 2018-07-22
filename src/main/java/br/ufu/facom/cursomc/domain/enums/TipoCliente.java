package br.ufu.facom.cursomc.domain.enums;

public enum TipoCliente {
	// A implementacao do enum eh diferente das normais para garantir que ninguem faca besteira caso queira adicionar mais algum detalhe nesse enum
	PESSOAFISICA(1,"Pessoa Fisica"),
	PESSOAJURIDICA(2,"Pessoa Juridica");
	
	private int codigo;
	private String descricao;
	
	private TipoCliente(int cod, String descricao) {
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
	
	public static TipoCliente toEnum(Integer cod) {
		// Eh static porque esa operacao vai ter que ser possivel mesmo sem instanciar objetos
		if (cod == null) return null;
		
		for(TipoCliente i: TipoCliente.values()) // Percorre todo o TipoCliente
		{		
			if (cod.equals(i.getCodigo())) return i;			
		}
		
		// CASO O RETURN NAO ACONTECA DENTRO DO FOR, O ID EH INVALIDO, ENTAO:
		throw new IllegalArgumentException("Id invalido: " + cod);
	}
}
