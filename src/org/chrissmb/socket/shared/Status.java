package org.chrissmb.socket.shared;

/**
 * Status retornado na resposta do servidor.<br>
 * Status: SUCESSO, ROTA_INVALIDA, ACAO_INVALIDA, 
	LOGIN_SENHA_INVALIDOS, ACESSO_NEGADO,
	ERRO_DESCONHECIDO
 * @author Christopher Monteiro
 * @see {@link org.chrissmb.socket.shared.RespostaServidor Resposta}
 * 
 */
public enum Status {
	SUCESSO, ROTA_INVALIDA, ACAO_INVALIDA, 
	LOGIN_SENHA_INVALIDOS, ACESSO_NEGADO,
	ERRO_DESCONHECIDO
}
