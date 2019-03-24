package org.chrissmb.socket.shared;

import java.io.Serializable;

/**
 * @author Christopher Monteiro
 *
 */
public class Requisicao implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String rota;
	
	private String acao;
	
	private Object objeto;
	
	public Requisicao() {}
	
	public Requisicao(String rota, String acao) {
		super();
		this.rota = rota;
		this.acao = acao;
	}

	public Requisicao(String rota, String acao, Object objeto) {
		super();
		this.rota = rota;
		this.acao = acao;
		this.objeto = objeto;
	}

	public String getRota() {
		return rota;
	}

	public void setRota(String rota) {
		this.rota = rota;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public Object getObjeto() {
		return objeto;
	}

	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}

}
