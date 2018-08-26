package br.ufu.facom.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.domain.enums.TipoCliente;
import br.ufu.facom.cursomc.dto.ClienteNewDTO;
import br.ufu.facom.cursomc.repositories.ClienteRepository;
import br.ufu.facom.cursomc.resources.exceptions.FieldMessage;
import br.ufu.facom.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {}

	@Override
	public boolean isValid(ClienteNewDTO objDTO, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		if (objDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidCPF(objDTO.getCpfOuCnpj())) {
			// Se for uma pessoa fisica e o CPF nao for valido
			list.add(new FieldMessage("cpfOUcnpj", "CPF inválido"));
		}

		if (objDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidCNPJ(objDTO.getCpfOuCnpj())) {
			// Se for uma pessoa juridica e o CNPJ nao for valido
			list.add(new FieldMessage("cpfOUcnpj", "CNPJ inválido"));
		}

		Cliente aux = repo.findByEmail(objDTO.getEmail());
		
		if (aux != null)
		{
			// Se aux != null, entao alguem ja possui esse email
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