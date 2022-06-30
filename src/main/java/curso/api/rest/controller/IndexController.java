package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
	
	/*SERVIÇO RESTFULL*/
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> init(){
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setLogin("teste");
		usuario.setNome("Java");
		usuario.setSenha("4464");
		
		Usuario usuario2 = new Usuario();
		usuario2.setId(2L);
		usuario2.setLogin("admin");
		usuario2.setNome("admin");
		usuario2.setSenha("4464");
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(usuario);
		usuarios.add(usuario2);
		
		return new ResponseEntity(usuarios, HttpStatus.OK);
		
		
	}

}
