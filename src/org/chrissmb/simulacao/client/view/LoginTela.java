package org.chrissmb.simulacao.client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.chrissmb.simulacao.client.util.Tools;
import org.chrissmb.socket.cliente.Cliente;
import org.chrissmb.socket.shared.Login;
import org.chrissmb.socket.shared.Resposta;
import org.chrissmb.socket.shared.Status;

public class LoginTela extends JFrame {

	private Cliente cliente;
	
	private JPanel panel;
	
	private JTextField txtLogin;
	
	private JPasswordField txtSenha;
	
	public LoginTela(Cliente cliente) {
		this.cliente = cliente;
		criarJanela();
	}
	
	private void criarJanela() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Login");
		
		panel = new JPanel();
		setContentPane(panel);
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel lblLogin = new JLabel("Login:");
		txtLogin = new JTextField(15);
		
		JLabel lblSenha = new JLabel("Senha:");
		txtSenha = new JPasswordField(15);
		
		JButton btnLogin = new JButton("Logar");
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logar();
			}
		});
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
				.addComponent(lblLogin)
				.addComponent(lblSenha)
			)
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addComponent(txtLogin)
				.addComponent(txtSenha)
				.addComponent(btnLogin)
			)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(lblLogin)
				.addComponent(txtLogin)
			)
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(lblSenha)
				.addComponent(txtSenha)
			)
			.addComponent(btnLogin)
		);
		
		pack();
		Tools.centreWindow(this);
		setVisible(true);
	}
	
	public void logar() {
		String senha = new String(txtSenha.getPassword());
		Login login = new Login(txtLogin.getText(), senha);
		try {
			Resposta resposta = cliente.callService(login, null, null);
			if (resposta.getStatus() == Status.LOGIN_SENHA_INVALIDOS) {
				Tools.alert("Falha de login");
			} else if (resposta.getStatus() != Status.SUCESSO) {
				Tools.alert("Falha: " + resposta.getStatus());
			} else {
				new ListaPessoasTela(cliente);
				dispose();
			}
		} catch (ClassNotFoundException e) {
			Tools.alert("Falha: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Tools.alert("Falha: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Tools.alert("Falha: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
