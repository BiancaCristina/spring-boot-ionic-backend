package br.ufu.facom.cursomc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.ufu.facom.cursomc.domain.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
	// Integer = corresponde ao tipo da minha chave principal (id)
	// Os objetos dessa interface sao capazes de realizar operacoes de acesso a dados, tais como busca,salvar,alterar,deletar,etc referentes ao objeto Categoria
		// Os objetos Categoria estao mapeados na tabela Categoria do banco de dados
	@Transactional(readOnly=true)
	@Query("SELECT obj FROM Cidade obj WHERE obj.estado.id = :estadoId ORDER BY obj.nome") // Devolve uma lista de cidades a partir do Estado escolhido
	// Esse @Param("estadoID" vincula o estado_id passado como parametro com "estadoiD" do @Query 
	public List<Cidade> findCidades(@Param("estadoId") Integer estado_id);
}
