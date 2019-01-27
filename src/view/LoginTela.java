package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.chrissmb.socket.cliente.Cliente;
import org.chrissmb.socket.shared.Login;
import org.chrissmb.socket.shared.Resposta;
import org.chrissmb.socket.shared.Status;

import util.Mensagem;

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
		setSize(300, 200);
		
		panel = new JPanel();
		setContentPane(panel);
		panel.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setBounds(10, 10, 100, 30);
		panel.add(lblLogin);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(120, 10, 100, 30);
		panel.add(txtLogin);
		
		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(10, 60, 100, 30);
		panel.add(lblSenha);
		
		txtSenha = new JPasswordField();
		txtSenha.setBounds(120, 60, 100, 30);
		panel.add(txtSenha);
		
		JButton btnLogin = new JButton("Logar");
		btnLogin.setBounds(150, 120, 100, 40);
		btnLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logar();
			}
		});
		panel.add(btnLogin);
		
		setVisible(true);
	}
	
	public void logar() {
		String senha = new String(txtSenha.getPassword());
		Login login = new Login(txtLogin.getText(), senha);
		try {
			Resposta resposta = cliente.callService(login, null, null);
			if (resposta.getStatus() == Status.LOGIN_SENHA_INVALIDOS) {
				Mensagem.alert("Falha de login");
			} else if (resposta.getStatus() != Status.SUCESSO) {
				Mensagem.alert("Falha: " + resposta.getStatus());
			} else {
				new ListaPessoasTela(cliente);
				dispose();
			}
		} catch (ClassNotFoundException e) {
			Mensagem.alert("Falha: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Mensagem.alert("Falha: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Mensagem.alert("Falha: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
