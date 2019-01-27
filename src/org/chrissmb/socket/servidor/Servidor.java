package org.chrissmb.socket.servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

import org.chrissmb.socket.shared.Acao;
import org.chrissmb.socket.shared.Login;
import org.chrissmb.socket.shared.Requisicao;
import org.chrissmb.socket.shared.Resposta;
import org.chrissmb.socket.shared.Status;

public class Servidor {

	private int port = 1234;

	private String rotaPackage = "rota";

	private ServerSocket server;

	private Autenticacao autenticacao;

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
				Socket client = server.accept();
				System.out.println("Cliente conectado: " + client.getInetAddress().getHostAddress());
				new ServidorThread(client).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ServidorThread extends Thread {

		private Socket client;

		private ObjectInputStream input;

		private ObjectOutputStream output;

		public ServidorThread(Socket client) {
			this.client = client;
		}

		@Override
		public void run() {
			try {
				output = new ObjectOutputStream(client.getOutputStream());
				input = new ObjectInputStream(client.getInputStream());

				if (autenticacao != null) {
					while (!autenticar()) {}
				}

				while (true) {
					Requisicao requisicao = (Requisicao) input.readObject();
					Resposta resposta = executaRota(requisicao);
					enviarObjeto(resposta);
				}
			} catch (ClassNotFoundException | NullPointerException e) {
				System.err.println("Objeto recebido inválido: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Erro de IO: " + e.getMessage());
				System.out.println(client.getInetAddress().getHostAddress() + " desconectou.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void enviarObjeto(Object obj) throws IOException {
			output.reset();
			output.flush();
			output.writeObject(obj);
		}

		private Resposta executaRota(Requisicao requisicao) {
			Resposta resposta = new Resposta();
			resposta.setStatus(Status.ACAO_INVALIDA);
			try {
				Class<?> classRota = Class.forName(rotaPackage + "." + requisicao.getRota());

				if (!possuiAcessoRota(classRota)) {
					resposta.setStatus(Status.ACESSO_NEGADO);
					return resposta;
				}

				Object obj = classRota.getDeclaredConstructors()[0].newInstance();
				Method metodo = buscaMetodo(requisicao, classRota);
				
				if (!possuiAcessoAcao(metodo)) {
					resposta.setStatus(Status.ACESSO_NEGADO);
					return resposta;
				}
				
				if (metodo.getParameterTypes().length == 0) {
					resposta.setObjeto(metodo.invoke(obj));
					resposta.setStatus(Status.SUCESSO);
				} else if (metodo.getParameterTypes().length == 1) {
					resposta.setObjeto(metodo.invoke(obj, requisicao.getObjeto()));
					resposta.setStatus(Status.SUCESSO);
				}

			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException e) {
				resposta.setStatus(Status.ROTA_INVALIDA);
			} catch (Exception e) {
				resposta.setStatus(Status.ERRO_DESCONHECIDO);
			}

			return resposta;
		}

		private Method buscaMetodo(Requisicao requisicao, Class<?> classRota) {
			for (Method metodo : classRota.getDeclaredMethods()) {
				if (!metodo.isAnnotationPresent(Acao.class))
					continue;
				Acao acao = metodo.getAnnotation(Acao.class);
				if (acao.value().equals(requisicao.getAcao()))
					return metodo;
			}
			return null;
		}

		public boolean autenticar() throws IOException, ClassNotFoundException {
			boolean retorno;
			Requisicao reqAuth = (Requisicao) input.readObject();
			Login login = (Login) reqAuth.getObjeto();
			Resposta resAuth = new Resposta();
			if (autenticacao.validar(login.getLogin(), login.getSenha())) {
				resAuth.setStatus(Status.SUCESSO);
				retorno = true;
			} else {
				resAuth.setStatus(Status.LOGIN_SENHA_INVALIDOS);
				retorno = false;
			}
			enviarObjeto(resAuth);
			return retorno;
		}

		private boolean possuiAcessoRota(Class<?> classRota) {
			if (autenticacao == null)
				return true;
			if (!classRota.isAnnotationPresent(Acesso.class))
				return true;
			
			Acesso acesso = classRota.getAnnotation(Acesso.class);
			if (autenticacao.possuiAcesso(acesso.value()))
				return true;
			return false;
		}

		private boolean possuiAcessoAcao(Method metodo) {
			if (autenticacao == null)
				return true;
			if (!metodo.isAnnotationPresent(Acesso.class))
				return true;
			
			Acesso acesso = metodo.getAnnotation(Acesso.class);
			if (autenticacao.possuiAcesso(acesso.value()))
				return true;
			return false;
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

	public ServerSocket getServer() {
		return server;
	}

	public void setAutenticacao(Autenticacao autenticacao) {
		this.autenticacao = autenticacao;
	}

}
