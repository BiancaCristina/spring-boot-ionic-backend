package br.ufu.facom.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufu.facom.cursomc.domain.Pagamento;

@Repository // Nao precisa criar Repository das subclasses
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
	// Integer = corresponde ao tipo da minha chave principal (id)
	// Os objetos dessa interface sao capazes de realizar operacoes de acesso a dados, tais como busca,salvar,alterar,deletar,etc referentes ao objeto Pagamento
		// Os objetos Pagamento estao mapeados na tabela Pagamento do banco de dados
	
	
}
