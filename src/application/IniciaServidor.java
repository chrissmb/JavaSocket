package application;

import org.chrissmb.socket.servidor.Autenticacao;
import org.chrissmb.socket.servidor.Servidor;

import data.Dados;

public class IniciaServidor {

	public static void main(String[] args) {
		new IniciaServidor().start();
	}
	
	private void start() {
		Servidor servidor = new Servidor();
		servidor.setAutenticacao(new Autenticacao() {
			
			@Override
			public boolean validar(String login, String senha) {
				String dadosLogin = Dados.getAdmin().getLogin();
				String dadosSenha = Dados.getAdmin().getSenha();
				if (login.equals(dadosLogin) && senha.equals(dadosSenha)) {
					return true;
				}
				return false;
			}
			
			@Override
			public boolean possuiAcesso(String acesso) {
				if (Dados.getAdmin().getAcessos().contains(acesso)) 
					return true;
				return false;
			}
		});
		servidor.start();
	}

}
