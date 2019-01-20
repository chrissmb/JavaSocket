package org.chrissmb.socket.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import org.chrissmb.socket.shared.Acao;
import org.chrissmb.socket.shared.Requisicao;
import org.chrissmb.socket.shared.Resposta;
import org.chrissmb.socket.shared.Status;

public class Servidor {

	private int port = 1234;
	
	private String rotaPackage = "rota";
	
	private Socket client;
	
	private ServerSocket server;

	public void start() {
		try {
			server = new ServerSocket(port);
			System.out.printf("Iniciado servidor na porta %d. Aguardando clientes.\n", port);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		while (true) {
			try {
				client = server.accept();
				System.out.println("Cliente conectado: " + client.getInetAddress()
						.getHostAddress());
				new ServidorThread().start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class ServidorThread extends Thread {
		
		private ObjectInputStream input;
		
		private ObjectOutputStream output;
		
		private Requisicao requisicao;
		
		private Resposta respota;
		
		@Override
		public void run() {
			try {
				output = new ObjectOutputStream(client.getOutputStream());
				input = new ObjectInputStream(client.getInputStream());
				
				while (true) {
					requisicao = (Requisicao) input.readObject();
					Resposta resposta = executaRota();
					enviarObjeto(resposta);
				}
			} catch (ClassNotFoundException e) {
				System.err.println("Objeto recebido inválido: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("Erro de IO: " + e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public void enviarObjeto(Object obj) throws IOException {
			output.reset();
			output.flush();
			output.writeObject(obj);
		}
		
		public Resposta executaRota() {
			Resposta resposta = new Resposta();
			resposta.setStatus(Status.ACAO_INVALIDA);
			try {
				Class<?> clazz = Class.forName(rotaPackage + "." + requisicao.getRota());
				Object obj = clazz.getDeclaredConstructors()[0].newInstance();
				
				Method metodo = buscaMetodo(clazz);
				
				if (metodo.getParameterTypes().length == 0) {
					resposta.setObjeto(metodo.invoke(obj));
					resposta.setStatus(Status.SUCESSO);
				} else if (metodo.getParameterTypes().length == 1) {
					resposta.setObjeto(metodo.invoke(obj, requisicao.getObjeto()));
					resposta.setStatus(Status.SUCESSO);
				}
				
			} catch (ClassNotFoundException | InstantiationException | 
					IllegalAccessException | IllegalArgumentException | 
					InvocationTargetException | SecurityException e) {
				resposta.setStatus(Status.ROTA_INVALIDA);
			} catch (Exception e) {
				resposta.setStatus(Status.ERRO_DESCONHECIDO);
			}
			
			return resposta;
		}
		
		private Method buscaMetodo(Class<?> clazz) {
			for (Method metodo : clazz.getDeclaredMethods()) {
				if (!metodo.isAnnotationPresent(Acao.class)) continue;
				Acao acao = metodo.getAnnotation(Acao.class);
				if (acao.nome().equals(requisicao.getAcao())) return metodo;
			}
			return null;
		}
		
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getRotaPackage() {
		return rotaPackage;
	}

	public void setRotaPackage(String rotaPackage) {
		this.rotaPackage = rotaPackage;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public ServerSocket getServer() {
		return server;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}
	
}
