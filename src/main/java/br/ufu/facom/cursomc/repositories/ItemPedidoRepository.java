package br.ufu.facom.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufu.facom.cursomc.domain.ItemPedido;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {
	// Integer = corresponde ao tipo da minha chave principal (id)
	// Os objetos dessa interface sao capazes de realizar operacoes de acesso a dados, tais como busca,salvar,alterar,deletar,etc referentes ao objeto ItemPedido
		// Os objetos ItemPedido estao mapeados na tabela ItemPedido do banco de dados
	
	
}
