package curso.api.rest.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import curso.api.rest.model.Usuario;
import curso.api.rest.model.UsuarioDTO;
import curso.api.rest.repository.UsuarioRepository;

@CrossOrigin  /*QUALQUER PARTE DO SISTEMA PODE FAZER A REQUISIÇÃO*/
@RestController    /*ARQUITETURA REST*/
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/*SERVIÇO RESTFULL*/
	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<UsuarioDTO> init(@PathVariable(value = "id") Long id){
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		
		return new ResponseEntity<UsuarioDTO>(new UsuarioDTO(usuario.get()),HttpStatus.OK);
			
	}
	/*LISTANDO TODOS OS ID,S*/
	@GetMapping(value = "/", produces = "application/json")
	public ResponseEntity<List<Usuario>> usuario(){
		
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	/*SALVANDO OS DADOS CRIANDO NO POSTMAN*/
	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws Exception{
		
		/*ASSOCIANDO O OBJETO FILHO(TELEFONE) AO PAI(USUARIO)*/
		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		/*CONSUMINDO API PUBLICA EXTERNA*/
		URL url = new URL("https://viacep.com.br/ws/"+usuario.getCep()+"/json/");
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();//DADOS DA REQUISIÇÃO QUE VEM NO CEP
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		
		String cep = "";
		StringBuilder jsonCep = new StringBuilder();
		
		while((cep = br.readLine()) != null) {
			jsonCep.append(cep);
		}
		
	 //	System.out.println(jsonCep.toString());
		
		Usuario userAux = new Gson().fromJson(jsonCep.toString(), Usuario.class);
		
		usuario.setSenha(userAux.getCep());
		usuario.setLogradouro(userAux.getLogradouro());
		usuario.setComplemento(userAux.getComplemento());
		usuario.setBairro(userAux.getBairro());
		usuario.setLocalidade(userAux.getLocalidade());
		usuario.setUf(userAux.getUf());
		
		/*CONSUMINDO API PUBLICA EXTERNA*/
			
		
		/*CODIFICANDO A SENHA DOS USUÁRIOS ANTES DE SALVAR*/
		String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCriptografada);
		
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
		/*ATUALIZANDO A SENHA JWT*/
		Usuario userTempUsuario = usuarioRepository.findUserByLogin(usuario.getLogin());
		
		if(!userTempUsuario.getSenha().equals(usuario.getSenha())) {//SENHA DIFERENTES
			
			/*CRIPTOGRANDO A SENHA DO USUÁRIO*/
			String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
			
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


