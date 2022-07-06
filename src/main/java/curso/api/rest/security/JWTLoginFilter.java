package curso.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import curso.api.rest.model.Usuario;

/*ESTABELECE O NOSSO GERENCIADOR DE TOKEN*/
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	
	/* CONFIGURAÇÃO O GERENCIADOR DE AUTENTICAÇÃO */
	public JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		
		/* SUPER A AUTENTICAR A URL */
		super(new AntPathRequestMatcher(url));
		
		/*GERENCIADOR DE AUTENTICAÇÃO*/
		setAuthenticationManager(authenticationManager);
	}

	/*RETORNA O USUÁRIO AO PROCESSAR A AUTENTICAÇÃO*/
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		/*ESTÁ PEGANDO O TOKEN PARA VALIDAR*/
		Usuario user = new ObjectMapper().
				readValue(request.getInputStream(),Usuario.class);
		
		/*RETORNA O USUÁRIO LOGIN E SENHA E ACESSOS*/
		return getAuthenticationManager().
				authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
	}
	
	/*USUÁRIO VALIDO*/
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
	}

	
}
