package br.ufu.facom.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;
import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.dto.ClienteDTO;
import br.ufu.facom.cursomc.repositories.ClienteRepository;
import br.ufu.facom.cursomc.resources.exceptions.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	@Autowired
	private HttpServletRequest request; // Criado para achar o ID do obj (uma vez que um objeto do tipo ClienteDTO nao carrega ID)
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteUpdate ann) {}

	@Override
	public boolean isValid(ClienteDTO objDTO, ConstraintValidatorContext context) {
		
		@SuppressWarnings("unchecked")
		Map<String,String> map =(Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE); 
		// Esse map vai pegar o map de variaveis de URI que estao na requisicao
		
		Integer uriID = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> list = new ArrayList<>();
		
		Cliente aux = repo.findByEmail(objDTO.getEmail());
		
		if (aux != null && (!aux.getId().equals(uriID)))
		{
			// Se aux != null e o ID do objeto que eu quero fazer update for diferente do que possui esse email, eu adiciono um erro
			list.add(new FieldMessage("email","Email ja existente"));
		}
		
		for (FieldMessage e : list) {
			// Isso aqui serve para converter meus FieldMessage(meus erros customizados) em erros do framework e adicionar na lista do framework
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		
		
		return list.isEmpty(); // Se a lista for vazia, significa que nao houve erros, logo eh valido.
	}
}