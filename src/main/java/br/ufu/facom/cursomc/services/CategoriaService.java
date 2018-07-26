package br.ufu.facom.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.ufu.facom.cursomc.domain.Categoria;
import br.ufu.facom.cursomc.dto.CategoriaDTO;
import br.ufu.facom.cursomc.repositories.CategoriaRepository;
import br.ufu.facom.cursomc.services.exceptions.DataIntegrityException;
import br.ufu.facom.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired // Isso faz com que a dependencia seja automaticamente instanciada pelo String
	private CategoriaRepository repo; 
		
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		// TRATAMENTO DE ERRO PARA CASO NAO EXISTA OBJETO
		return obj.orElseThrow( () -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert (Categoria obj) {
		obj.setId(null);
		// Se esse obj tiver algum ID, o metodo save vai considerar que eh uma atualizacao e nao uma insercao
		// Logo, o comando acima garante que estou inserindo um objeto do tipo Categoria com ID nulo
		return repo.save(obj);
	}
	
	public Categoria update (Categoria obj) {
		// A diferenca entre update e inser eh que em um eu preciso garantir que ID seja nulo (insert),
		// No outro, caso o ID nao seja nulo, entao irei fazer uma atualizacao em um ID que ja existe no meu BD
		
		// Verifica se o objeto existe
		Categoria newObj = this.find(obj.getId()); // Caso o objeto nao exista, esse metodo ja lanca a excecao!
		this.updateData(newObj,obj); // Salva os dados de newObj de acordo com os dados previos de obj
		return repo.save(newObj);
	}
	
	private void updateData(Categoria newObj,Categoria obj) {
		// Eh um metodo privado porque eh so um metodo auxiliar que vou usar exclusivamente na CategoriaService
		// So preciso setar Nome e Email porque sao os atributos que eu nao coloquei em CategoriaDTO
		newObj.setNome(obj.getNome());
	}
	
	public void delete(Integer id) {
		// Verifica se ID existe
		this.find(id);
		
		try 
		{			
			repo.deleteById(id);
		}
		
		catch (DataIntegrityViolationException e)
		{
			throw new DataIntegrityException("Nao eh possivel excluir uma categoria que possui produtos");
		}		
	}
	
	public List<Categoria> findAll(){
		// Esse metodo retorna todas as categorias quando digitar o URL "/categoria"
		return repo.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		// Metodo auxiliar que instancia um objeto do tipo Categoria a partir de um objeto do tipo CategoriaDTO
		return new Categoria(objDTO.getId(),objDTO.getNome());
	}
}
