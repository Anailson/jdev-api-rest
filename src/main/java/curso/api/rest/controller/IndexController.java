package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/*SERVIÇO RESTFULL*/
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id){
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		return new ResponseEntity(usuario.get(),HttpStatus.OK);
			
	}
	/*LISTANDO TODOS OS ID,S*/
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario(){
		
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	/*SALVANDO OS DADOS CRIANDO NO POSTMAN*/
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){
		
		/*ASSOCIANDO O OBJETO FILHO(TELEFONE) AO PAI(USUARIO)*/
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
			
		/*GRAVAR NO BANCO DE DADOS*/
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	/* ATUALIZANDO OS DADOS */
	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar (@RequestBody Usuario usuario){
		
		/*ASSOCIANDO O OBJETO FILHO(TELEFONE) AO PAI(USUARIO)*/
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		/*OUTRAS ROTINAS ANTES DE ATUALIZAR OS REGISTROS*/
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	/*DELETANDO REGISTROS*/
	@DeleteMapping(value = "/{id}", produces = "application/json" )
	public String delete (@PathVariable("id")Long id) {
		
		usuarioRepository.deleteById(id);
		
		return "ok";
	}
	
	
}


