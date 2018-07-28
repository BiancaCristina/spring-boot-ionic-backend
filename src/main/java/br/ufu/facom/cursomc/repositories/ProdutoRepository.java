package br.ufu.facom.cursomc.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.ufu.facom.cursomc.domain.Categoria;
import br.ufu.facom.cursomc.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
	// Integer = corresponde ao tipo da minha chave principal (id)
	// Os objetos dessa interface sao capazes de realizar operacoes de acesso a dados, tais como busca,salvar,alterar,deletar,etc referentes ao objeto Categoria
		// Os objetos Categoria estao mapeados na tabela Categoria do banco de dados
	
	// A anotacao @Query eh usada como substituicao de ter que criar uma classe que implementa essa interface pra poder escrever esse metodo "search" (uma vez que uma interface eh apenas assinatura de metodos e nao sua implementacao)
	// Logo, o que esta dentro do (" ") indica a busca que eu quero fazer em JPQL
	// @Param("nome") serve para que o parametro "String nome" seja capturado e substitua na busca JPQL
	// @Param("categorias") faz a mesma coisa que o @Param("nome")
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(@Param("nome") String nome, @Param("categorias")List<Categoria> categorias, Pageable pageRequest);
}
