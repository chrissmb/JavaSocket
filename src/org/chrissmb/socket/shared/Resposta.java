package org.chrissmb.socket.shared;

import java.io.Serializable;


/**
 * Classe utilizada como resposta das chamadas de serviço
 * @author Christopher Monteiro
 *
 */
public class Resposta implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object objeto;
	
	private Status status;

	/**
	 * @return o objeto enviado pelo servidor.
	 */
	public Object getObjeto() {
		return objeto;
	}

	public void setObjeto(Object objeto) {
		this.objeto = objeto;
	}

	/**
	 * @return a status da resposta do servidor.
	 * @see {@link org.chrissmb.socket.shared.Status Status}
	 */
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
