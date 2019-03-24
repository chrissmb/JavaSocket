package org.chrissmb.socket.servidor;

/**
 * Interface para definicação do comportamento da autenticação so serviço.
 * @author Christopher Monteiro
 * @see {@link #validar(String, String) validar}, {@link #possuiAcesso(String) possuiAcesso}
 */
public interface Autenticacao {
	
	/**
	 * Definir como é feita a validação da autenticação.
	 * @param login
	 * @param senha
	 * @return se o login e senha são válidos.
	 */
	boolean validar(String login, String senha);
	
	/**
	 * Definir como é validado se o usuário possui o acesso informado.
	 * @param acesso
	 * @return se o acesso é válido.
	 */
	boolean possuiAcesso(String acesso);
}
