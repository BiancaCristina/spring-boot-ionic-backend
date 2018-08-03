package br.ufu.facom.cursomc.domain.enums;

public enum Perfil {
	ADMIN(1,"ROLE_ADMIN"), // "ROLE_ADMIN" eh exigencia do framework
	CLIENTE(2,"ROLE_CLIENTE");
	
	private int codigo;
	private String descricao;
	
	private Perfil(int cod, String descricao) {
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
	
	public static Perfil toEnum(Integer cod) {
		// Eh static porque esa operacao vai ter que ser possivel mesmo sem instanciar objetos
		if (cod == null) return null;
		
		for(Perfil i: Perfil.values()) // Percorre todo o TipoCliente
		{		
			if (cod.equals(i.getCodigo())) return i;			
		}
		
		// CASO O RETURN NAO ACONTECA DENTRO DO FOR, O ID EH INVALIDO, ENTAO:
		throw new IllegalArgumentException("Id invalido: " + cod);
	}
}
