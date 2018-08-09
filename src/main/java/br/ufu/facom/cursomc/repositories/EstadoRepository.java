package br.ufu.facom.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.ufu.facom.cursomc.domain.Estado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	// Integer = corresponde ao tipo da minha chave principal (id)
	// Os objetos dessa interface sao capazes de realizar operacoes de acesso a dados, tais como busca,salvar,alterar,deletar,etc referentes ao objeto Categoria
		// Os objetos Categoria estao mapeados na tabela Categoria do banco de dados
	
	@Transactional(readOnly=true)
	public List<Estado> findAllByOrderByNome(); // Retorna os estados ordenado pelo nome (metodo que o framework vai implementar automaticamente)
}
