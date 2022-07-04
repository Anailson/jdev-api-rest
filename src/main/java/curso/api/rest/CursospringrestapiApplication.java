package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"curso.api.rest.model"})//LER O PACOTE E ASSIM CRIANDO TODAS AS TABELAS DE AUTOMATICAS
@ComponentScan(basePackages = {"curso.*"})//VER TUDO ESTÁ DENTRO DA PASTA CURSO
@EnableJpaRepositories(basePackages = {"curso.api.rest.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
public class CursospringrestapiApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CursospringrestapiApplication.class, args);
	}
	
	/*CROSS ORIGIN - MAPEAMENTO GLOBAL DA APLICAÇÃO*/
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		
		registry.addMapping("/usuario/***")//LIBEBRANDO AS AÇÕES PARA O addMapping
		.allowedOrigins("*")//LIBERANDO TODAS AS FUNÇÕES DA API
		.allowedOrigins("*");//LIBERANDO O MAPEAMENTO DE USUÁRIOS PARA TODAS AS ORIGENS
	
	}
	

}
