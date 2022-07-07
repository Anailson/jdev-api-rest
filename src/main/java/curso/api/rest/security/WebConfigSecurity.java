package curso.api.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import curso.api.rest.service.ImplementacaoDetailsService;

/*MAPEIA URL, ENDEREÇO, AUTORIZA OU BLOQUEIA ACESSO A URL*/
@Configurable
@EnableWebSecurity
public class WebConfigSecurity  extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private ImplementacaoDetailsService implementacaoDetailsService;
	
	/*CONFIGURA AS SOLICITAÇÕES DE ACESSO POR HTTP*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
	   /*ATIVANDO A PROTEÇÃO CONTRA USUÁRIOS QUE NÃO ESTÃO VALIDADOS POR TOKEN*/
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		
		/*ATIVANDO A PERMISSÃO PARA ACESSO A PÁGINA INICIAL DO SISTEMA EX: SISTEMA.COM.BR/INDEX */
	   .disable().authorizeHttpRequests().antMatchers("/").permitAll()
	   .antMatchers("/index").permitAll() //PERMITE TODOS OS ACESSOS
	   
	   /*PERMITE VÁRIOS TIPOS DE USO NA HTTP COM AS PORTAS E SERVIDORES DIFERENTES*/
	   .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	   
	   /*URL DE LOGOUT - REDIRECIONAR APÓS O USER DESLOGAR DO SISTEMA */
	   .anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
	   
	   /*MAPEIA A URL DO LOGOUT E INVALIDA O USUÁRIO*/
	   .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		
		/*FILTRA REQUISIÇÕES DE LOGIN PARA AUTENTICAÇÃO*/
		.and().addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
			UsernamePasswordAuthenticationFilter.class)
					
		/*FILTRA DEMAIS REQUISIÇÕES PARA VERIFICAR A PRESENÇA DO TOKEN JWT NO HEADER HTTP*/
		.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
		
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		/*SERVICE QUE IRÁ CONSULTAR O USUÁRIO NO BANCO DE DADOS*/
		auth.userDetailsService(implementacaoDetailsService)

		/*PADRÃO DE CODIFICAÇÃO DE SENHA*/	
		.passwordEncoder(new BCryptPasswordEncoder());
	}

}
