package org.chrissmb.socket.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.chrissmb.socket.shared.Requisicao;
import org.chrissmb.socket.shared.Resposta;

/**
 * Classe para instanciar o serviço {@link java.net.Socket Socket}
 * que fará as chamadas ao servidor {@link org.chrissmb.socket.servidor.Servidor Servidor}.
 * @author Christopher Monteiro
 *
 */
public class Cliente {

	private int port = 1234;
	
	private String host;
	
	private Socket client;

	private ObjectInputStream input;

	private ObjectOutputStream output;

	/**
	 * Inicia conexão com o servidor.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void start() throws UnknownHostException, IOException {
		client = new Socket(host, port);
		System.out.println("Conectado ao servidor.");

		input = new ObjectInputStream(client.getInputStream());
		output = new ObjectOutputStream(client.getOutputStream());
	}
	
	/**
	 * Faz chamada de serviço enviando um objeto.
	 * @param object objeto a ser enviado.
	 * @param rota rota do serviço.
	 * @param acao acao da rota.
	 * @return a resposta do servidor.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Resposta callService(Object object, String rota, String acao) 
			throws IOException, ClassNotFoundException {
		if (!client.isConnected()) {
			return null;
		}
		output.reset();
		output.flush();
		output.writeObject(new Requisicao(rota, acao, object));
		Resposta resp = (Resposta) input.readObject(); 
		return resp;
	}
	
	/**
	 * Faz chamada de serviço.
	 * @param rota rota do serviço.
	 * @param acao acao da rota.
	 * @return a resposta do servidor.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @see {@link org.chrissmb.socket.shared.Resposta Resposta}
	 */
	public Resposta callService(String rota, String acao) 
			throws IOException, ClassNotFoundException {
		if (!client.isConnected()) {
			return null;
		}
		output.reset();
		output.flush();
		output.writeObject(new Requisicao(rota, acao));
		Resposta resp = (Resposta) input.readObject(); 
		return resp;
	}

	/**
	 * @return a porta TCP do servidor definida.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Define a porta TCP do servidor.
	 * @param port o número da porta.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return o host do servidor definido.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Define o host do servidor.
	 * @param host o host do servidor.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return o objeto client to tipo {@link java.net.Socket Socket}.
	 */
	public Socket getClient() {
		return client;
	}

}
