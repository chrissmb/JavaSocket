package application;

import java.io.IOException;
import java.net.UnknownHostException;

import org.chrissmb.socket.cliente.Cliente;

import util.Mensagem;
import view.LoginTela;

public class IniciaClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new IniciaClient().start();
	}
	
	private void start() {
		Cliente cliente = new Cliente();
		cliente.setHost("localhost");
		try {
			cliente.start();
			new LoginTela(cliente);
//			new ListaPessoasTela(cliente);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Mensagem.alert(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Mensagem.alert(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Mensagem.alert(e.getMessage());
		}
	}

}
