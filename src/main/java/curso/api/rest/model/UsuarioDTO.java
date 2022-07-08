package curso.api.rest.model;

import java.io.Serializable;

public class UsuarioDTO  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/*DEFININDO QUAIS OS ATRIBUTOS SERÁ MOSTRADO NA TELA*/
	private String userLogin;
	
	private String userNome;
	
	//CONSTRUTOR
	public UsuarioDTO(Usuario usuario) {
		
		this.userLogin = usuario.getLogin();
		this.userNome = usuario.getNome();
	}

	public String getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	public String getUserNome() {
		return userNome;
	}

	public void setUserNome(String userNome) {
		this.userNome = userNome;
	}
	
	

	
}
