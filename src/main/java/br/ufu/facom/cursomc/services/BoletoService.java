package br.ufu.facom.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import br.ufu.facom.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		// Numa aplicacao real, o vencimento eh definido por um WebService que gera boleto
		// Logo, essa classe apenas serve como exemplo para ter um vencimento no app
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH,7); // Vencimento para 7 dias depois do instante do Pedido
		pagto.setDataPagamento(cal.getTime());
	}
}
