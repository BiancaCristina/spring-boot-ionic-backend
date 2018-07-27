package br.ufu.facom.cursomc.repositories;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufu.facom.cursomc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	// Integer = corresponde ao tipo da minha chave principal (id)
	// Os objetos dessa interface sao capazes de realizar operacoes de acesso a dados, tais como busca,salvar,alterar,deletar,etc referentes ao objeto Categoria
		// Os objetos Cliente estao mapeados na tabela Categoria do banco de dados
	
	@Transactional(readOnly=true) // ReadOnly=Nao precisa ser envolvida com uma transacao de BD
	Cliente findByEmail(String email); // Fazendo so isso aqui, o Repository ja busca um cliente passando o email como argumento 
}
