package br.ufu.facom.cursomc.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class HeaderExposureFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Nao executarei nada quando o filtro for iniciado
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {	
		
		HttpServletResponse res = (HttpServletResponse) response; // "Converte" ServletResponse em HttpServletResponse
		res.addHeader("access-control-expose-headers", "Location");
		chain.doFilter(request, response); // Continua a requisicao depois de interceptar as requisicoes
	}

	@Override
	public void destroy() {
		// Nao executarei nada quando o filtro for destruido
		
	}
	
}
