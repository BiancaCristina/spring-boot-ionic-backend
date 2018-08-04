package br.ufu.facom.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import br.ufu.facom.cursomc.domain.Cliente;
import br.ufu.facom.cursomc.domain.Pedido;

public interface EmailService {
	
	// Texto plano (simples)
	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);
	public void sendNewPasswordEmail(Cliente cliente, String newPass);

}
