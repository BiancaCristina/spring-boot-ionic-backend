package br.ufu.facom.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.ufu.facom.cursomc.domain.Cidade;
import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.domain.Endereco;
import br.ufu.facom.cursomc.domain.enums.Perfil;
import br.ufu.facom.cursomc.domain.enums.TipoCliente;
import br.ufu.facom.cursomc.dto.ClienteDTO;
import br.ufu.facom.cursomc.dto.ClienteNewDTO;
import br.ufu.facom.cursomc.repositories.ClienteRepository;
import br.ufu.facom.cursomc.repositories.EnderecoRepository;
import br.ufu.facom.cursomc.security.UserSS;
import br.ufu.facom.cursomc.services.exceptions.AuthorizationException;
import br.ufu.facom.cursomc.services.exceptions.DataIntegrityException;
import br.ufu.facom.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired // Isso faz com que a dependencia seja automaticamente instanciada pelo String
	private ClienteRepository repo; 
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	// Eh preciso fazer um repositorio do endereco porque, diferentemente dos telefones, nao ha um "ElementCollection" que indique o salvamento automatico dos enderecos no BD
	// Logo, eh preciso usar o enderecoRepository e salvar o endereco criado para insercao desse cliente
	// @ElementCollection = serve para mapear uma coleção em memória (no caso a lista de telefones) para uma tabela no banco de dados relacional.Assim, se você coloca essa anotação sobre uma lista de strings (como foi o caso), será criada uma tabela contendo dois campos: a chave estrangeira e o valor do string.
	
	@Autowired
	private BCryptPasswordEncoder pe; // pe = passowrd encoder, vai codificar a senha
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix; // Essa variavel armazena aquele prefixo "cp" do application.propperties
	
	@Value("${img.profile.size}")
	private Integer size; // Essa variavel armazena aquele valor size do application.propperties
	
	public Cliente find(Integer id) {
		// Testando nivel de autorizacao
		UserSS user = UserService.authenticated(); // Pega o usuario logado
		
		if (user== null || ((!user.hasRole(Perfil.ADMIN)) && (!id.equals(user.getId()))) ) {
			// Se o user for null ou (nao for ADMIN e nao eh ID do usuario logado)
			throw new AuthorizationException("Acesso negado");
		}
		
		// Fim do teste de nivel de autorizacao 
		
		Optional<Cliente> obj = repo.findById(id);
		// TRATAMENTO DE ERRO PARA CASO NAO EXISTA OBJETO
		return obj.orElseThrow( () -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert (Cliente obj) {
		obj.setId(null);
		// Se esse obj tiver algum ID, o metodo save vai considerar que eh uma atualizacao e nao uma insercao
		// Logo, o comando acima garante que estou inserindo um objeto do tipo Categoria com ID nulo
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update (Cliente obj) {
		// A diferenca entre update e inser eh que em um eu preciso garantir que ID seja nulo (insert),
		// No outro, caso o ID nao seja nulo, entao irei fazer uma atualizacao em um ID que ja existe no meu BD
		
		// Verifica se o objeto existe
		Cliente newObj = this.find(obj.getId()); // Caso o objeto nao exista, esse metodo ja lanca a excecao!
		this.updateData(newObj,obj); // Salva os dados de newObj de acordo com os dados previos de obj
		return repo.save(newObj);
	}
	
	private void updateData(Cliente newObj,Cliente obj) {
		// Eh um metodo privado porque eh so um metodo auxiliar que vou usar exclusivamente na ClienteService
		// So preciso setar Nome e Email porque sao os atributos que eu nao coloquei em ClienteDTO
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public void delete(Integer id) {
		// So podemos excluir se o cliente nao possuir clientes
		// Caso exclua, deve apagar os enderecos associados ao cliente (porque um Endereco nao pode existir sem um Cliente)
		
		// A unica diferenca entre esse delete e o da Categoria eh que tive que adicionar o "cascade" na classe Cliente do dominio, ou seja,
		// Quando apagar um cliente, os enderecos serao apagados em cascata
		
		// Verifica se ID existe
		this.find(id);
		
		try 
		{			
			repo.deleteById(id);
		}
		
		catch (DataIntegrityViolationException e)
		{
			throw new DataIntegrityException("Nao eh possivel excluir um cliente que possui entidades relacionadas");
		}		
	}
	
	public List<Cliente> findAll(){
		// Esse metodo retorna todas as categorias quando digitar o URL "/categoria"
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		// Metodo auxiliar que instancia um objeto do tipo Cliente a partir de um objeto do tipo ClienteDTO
		return new Cliente(objDTO.getId(),objDTO.getNome(),objDTO.getEmail(),null,null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOUcnpj(), TipoCliente.toEnum(objDTO.getTipo()),pe.encode(objDTO.getSenha()));
		Cidade cid = new Cidade(objDTO.getCidadeId(),null,null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		
		// Adicionando os telefone2 e telefone3 (opcionais)
		if (objDTO.getTelefone2() != null) cli.getTelefones().add(objDTO.getTelefone2());
		if (objDTO.getTelefone3() != null) cli.getTelefones().add(objDTO.getTelefone3());
		
		return cli;
	}
	
	public URI uploadProfilePicture(MultipartFile multiPartFile) {
		UserSS user = UserService.authenticated(); // Pega o usuario logado
		
		if (user == null) {
			// Isso significa que ninguem esta logado, lanca excecao
			throw new AuthorizationException("Acesso negado");
		}	
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multiPartFile); // Pega a imagem do multiPartFile
		jpgImage = imageService.cropSquare(jpgImage); // Recorta a imagem
		jpgImage = imageService.resize(jpgImage, size);
		
		String fileName = prefix + user.getId() + ".jpg"; // Isso aqui padroniza os nomes dos arquivos, ex: cliente1-> cp1.jpg, cliente2->cp2.jpg
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");	
	}
}
