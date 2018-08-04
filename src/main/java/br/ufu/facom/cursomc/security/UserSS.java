package br.ufu.facom.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.ufu.facom.cursomc.domain.enums.Perfil;

public class UserSS implements UserDetails{
	// Essa classe eh pra definir quais atributos de um usuario do Spring Security(UserSS)
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities; // Perfis de autorizacao (Cliente, ADMIN)
	
	public UserSS() {}
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		
		// O comando abaixo converte o Set<Perfil> em Collection<? extends GrantedAuthority>
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}

	public Integer getId() {
		return id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Esse metodo retorna quais as autorizacoes do usuario
		return authorities; 
	}

	@Override
	public String getPassword() {
		// Esse metodo devolve a senha do usuario
		return senha; 
	}

	@Override
	public String getUsername() {
		// Esse metodo retorna o username do usuario, o qual eh o email (por definicao do projeto)
		return email; 
	}

	@Override
	public boolean isAccountNonExpired() {
		// Por padrao, as contas nunca vao expirar, entao sempre retorna true
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// Por padrao, as contas nunca estarao bloqueadas, entao retorna sempre true
		// Esse metodo serve para caso queira implementar a ferramenta de bloqueio de conta
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// Por padrao, as credenciais nunca estarao expiradas, entao retorna true sempre
		return true;
	}

	@Override
	public boolean isEnabled() {
		// Por padrao, o usuario sempre estara ativo
		return true;
	}
	// Classe de um usuario que atende o Spring Security -> UserSS
	
	public boolean hasRole(Perfil perfil) {
		// Verifica se meu objeto do tipo UserSS possui o perfil passado como parametro
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}
	
}
