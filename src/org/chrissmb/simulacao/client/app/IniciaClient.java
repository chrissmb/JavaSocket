package org.chrissmb.simulacao.client.app;

import java.io.IOException;
import java.net.UnknownHostException;

import org.chrissmb.simulacao.client.util.Tools;
import org.chrissmb.simulacao.client.view.LoginTela;
import org.chrissmb.socket.cliente.Cliente;

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
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Tools.alert(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Tools.alert(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			Tools.alert(e.getMessage());
		}
	}

}
