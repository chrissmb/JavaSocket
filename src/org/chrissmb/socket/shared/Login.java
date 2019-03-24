package org.chrissmb.socket.shared;

import java.io.Serializable;

/**
 * Classe utilizada para enviar autenticação do cliente para o servidor.
 * @author Christopher Monteiro
 *
 */
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	private String login;
	
	private String senha;

	public Login(String login, String senha) {
		this.login = login;
		this.senha = senha;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
