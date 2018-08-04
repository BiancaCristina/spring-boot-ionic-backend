package br.ufu.facom.cursomc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	// Integer = corresponde ao tipo da minha chave principal (id)
	// Os objetos dessa interface sao capazes de realizar operacoes de acesso a dados, tais como busca,salvar,alterar,deletar,etc referentes ao objeto Pedido
		// Os objetos Pedido estao mapeados na tabela Pedido do banco de dados
	
	@Transactional(readOnly=true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
}
