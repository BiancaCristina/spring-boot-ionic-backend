package br.ufu.facom.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailService extends AbstractEmailService{
	// Essa classe sera responsavel por enviar o email
	
	@Autowired
	private MailSender mailSender; // Instancia esse cara com os dados contidos em application-dev
	
	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class); 
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando e-mail...");
		mailSender.send(msg);
		LOG.info("Email enviado!");
	}

}
