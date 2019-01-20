package application;

import java.io.IOException;
import java.net.UnknownHostException;

import org.chrissmb.socket.cliente.Cliente;

import view.ListaPessoas;

public class ClientApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ClientApp().start();
	}
	
	public void start() {
		Cliente cliente = new Cliente();
		cliente.setHost("localhost");
		try {
			cliente.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new ListaPessoas(cliente);
	}

}
