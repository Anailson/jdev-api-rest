package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {
	
	/*TEMPO DE VALIDADE DE TOKEN EM 2 DIAS - A CADA 2 DIAS UMA NOVA AUTENTICAÇÃO */
	private static final long EXPIRATION_TIME = 17280000;
	
	/*UMA SENHA ÚNICA */
	private static final String SECRET = "SenhaExtreamenteSecreta";
	
	/*PREFIXO PADRÃO DO TOKEN*/
	private static final String TOKEN_PREFIX  = "Bearer";
	
	private static final String HEADER_STRING = "Authorization";
	
	/*GERANDO TOKEN DE AUTENTICAÇÃO E ADICIONANDO AO CABEÇALHO E RESPOSTA HTTP */
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {
		
		/*MONTAGEM DO TOKEN*/
		String JWT = Jwts.builder()/*CHAMAR O GERADOR DE TOKEN*/
				.setSubject(username)/*ADICIONAR O USUÁRIO*/
				.setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))/*TEMPO DE EXPIRAÇÃO*/
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();/*COMPACTAÇÃO E ALGORITMO DE GERAÇÃO DE SENHA*/
		
		/*JUNTA O TOKEN COM O PREFIXO*/
		String token = TOKEN_PREFIX + " " + JWT; /*bearer - ex: FORMANDO UM NOVO TOKEN : ewsfefeferc*/
		
		/*ADICIONAR NO CABEÇALHO HTTP*/
		response.addHeader(HEADER_STRING, token); /*Authorization: Bearer ffreferergffd*/
		
		/*CHAMANDO A INSTRUÇÃO NO REPOSITORY PARA ATUALIZAÇÃO DO TOKEN NA BASE*/
		ApplicationContextLoad.getApplicationContext()
				.getBean(UsuarioRepository.class)
				.atualizaTokenUser(JWT, username);  /*METODO NO REPOSITORY CRIADO*/
	
		/*LIBERANDO RESPOSTA PARA AS PORTAS DIFERENTES QUE USAM A API OU CASO OS CLIENTES WEB EX: PORTA 8080 E PORTA 4900*/
		liberacaoCors(response);
		
		//response.addHeader("Access-Control-Allow-Origin", "*");
		
		
		/*ESCREVE TOKEN COM A RESPOSTA NO CORPO DO HTTP*/
		response.getWriter().write("{\"Authorization\": \"" +token+"\"}");
		
	}
	
	/*RETORNANDO O USUÁRIO VALIDADO COM TOKEN OU CASO NÃO SEJA VALIDO RETORNA NULL*/
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {
		
		/*PEGA O TOKEN ENVIADO NO CABEÇALHO HTTP*/
		
		String token = request.getHeader(HEADER_STRING);
		
		try { //CAPTURANDO EXCEÇÃO DE TOKEN EXPIRADO
			
		if (token != null) {
			
			/*TE UM TOKEN LIMO SEM ESPAÇOS exemplo: Bearer fsfdgdfgd*/
			String tokenLimpo = token.replace(TOKEN_PREFIX,"").trim();//TRIM LIMPAR OS ESPAÇOS
			
			/*FAZ A VALIDAÇÃO DO TOKEN DO USUÁRIO NA REQUISIÇÃO*/
			String user = Jwts.parser().setSigningKey(SECRET) /*Bearer frwqrwqerwe*/
					.parseClaimsJws(tokenLimpo)/*faeewfeferfer*/
					.getBody().getSubject(); /*exemplo: usuario teste*/
			
		
			if(user != null) {
				
				Usuario usuario = ApplicationContextLoad.getApplicationContext()
						.getBean(UsuarioRepository.class).findUserByLogin(user);
				
				if(usuario != null) {
					
					/*SER O TOKEN FOI IGUAL DO BANCO É VALIDADO - PASSA A VALIDAÇÃO E O ACESSO É LIBERADO*/
					if (tokenLimpo.equals(usuario.getToken())) {
							
					
					/*PADRÃO DA DOCUMENTAÇÃO SPRING*/
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(),
							usuario.getSenha(),
							usuario.getAuthorities());
					}
				}
			}
		

		} //FIM DA CONDIÇÃO TOKEN
		}catch (io.jsonwebtoken.ExpiredJwtException e) {
			try {
				response.getOutputStream().println("Seu TOKEN está expirado, faça seu login ou informe um novo TOKEN para autenticação");
			}catch(IOException el) {}
		}
			
		/*LIBERAÇÃO CORS*/
		liberacaoCors(response);

		return null; /*NÃO AUTORZAÇÃO*/	
	}
 
	/*LIBERAÇÃO DO CORS MÉTODO CRIADO APÓS A liberacaoCors*/
	private void liberacaoCors(HttpServletResponse response) {
	
		
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin","*");
		}
		
		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers","*");
		}
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers","*");
		}
		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods","*");
		}
		
		
	}
	

}
