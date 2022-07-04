package curso.api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;

@Service
public class ImplementacaoDetailsService implements UserDetailsService{

	@Autowired
    private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		/*CONSULTA NO BANCO O USUÁRIO*/
		Usuario usuario = usuarioRepository.findUserByLogin(username);
		
		/*VALIDAÇÃO*/
		if (usuario == null) {
			 throw new UsernameNotFoundException("Usuário não foi encontrado");
		}
		
		return new User(usuario.getLogin(),usuario.getPassword(), usuario.getAuthorities());
	}
	
	
}
