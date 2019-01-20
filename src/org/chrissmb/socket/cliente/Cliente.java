package org.chrissmb.socket.cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.chrissmb.socket.shared.Requisicao;
import org.chrissmb.socket.shared.Resposta;
import org.chrissmb.socket.shared.Status;

public class Cliente {

	private int port = 1234;
	
	private String host;
	
	private Socket client;

	private ObjectInputStream input;

	private ObjectOutputStream output;

	public void start() throws UnknownHostException, IOException {
		client = new Socket(host, port);
		System.out.println("Conectado ao servidor.");

		input = new ObjectInputStream(client.getInputStream());
		output = new ObjectOutputStream(client.getOutputStream());
	}
	
	public Resposta callService(Object object, String rota, String acao) 
			throws IOException, ClassNotFoundException {
		output.reset();
		output.flush();
		output.writeObject(new Requisicao(rota, acao, object));
		Resposta resp = (Resposta) input.readObject(); 
		return resp;
	}
	
	public Resposta callService(String rota, String acao) 
			throws IOException, ClassNotFoundException {
		output.reset();
		output.flush();
		output.writeObject(new Requisicao(rota, acao));
		Resposta resp = (Resposta) input.readObject(); 
		return resp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}
}
