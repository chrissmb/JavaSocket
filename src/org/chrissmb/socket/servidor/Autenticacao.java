package org.chrissmb.socket.servidor;

public interface Autenticacao {
	
	boolean validar(String login, String senha);
	
	boolean possuiAcesso(String acesso);
}
