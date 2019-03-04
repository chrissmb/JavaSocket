package org.chrissmb.simulacao.client.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.chrissmb.simulacao.client.util.Tools;
import org.chrissmb.simulacao.shared.model.Pessoa;
import org.chrissmb.socket.cliente.Cliente;
import org.chrissmb.socket.shared.Resposta;
import org.chrissmb.socket.shared.Status;

public class ListaPessoasTela extends JFrame {

	private JPanel panel;
	private Cliente cliente;
	private JTextField txtNome;
	private GridTable tabela;

	public ListaPessoasTela(Cliente cliente) {
		this.cliente = cliente;
		criarTela();
	}

	private void criarTela() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Pessoas");
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		JLabel lblNome = new JLabel("Adicionar pessoa");
		txtNome = new JTextField();
		
		JButton btnSalvar = new JButton("Salvar");
		
		btnSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				savePessoa(txtNome.getText().trim());
			}
		});
		
		tabela = new GridTable();
		tabela.addColuna("Id", "id")
			.addColuna("Nome", "nome");
		tabela.atualizar();
		tabela.addMouseListener(mouseListener());
		
		JButton btnConsultar = new JButton("Consultar");
		
		btnConsultar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getPessoas();
			}
		});
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addComponent(lblNome)
					.addComponent(txtNome)
					.addComponent(btnSalvar)
				)
				.addComponent(tabela)
				.addComponent(btnConsultar)
			)
		);
		
		layout.setVerticalGroup(layout.createSequentialGroup()
			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(lblNome)
				.addComponent(txtNome)
				.addComponent(btnSalvar)
			)
			.addComponent(tabela)
			.addComponent(btnConsultar)
		);
		
		pack();
		Tools.centreWindow(this);
		setVisible(true);
	}
	
	private void getPessoas() {
		try {
			Resposta resposta =  cliente.callService("PessoaRota", "getAll");
			
			if (resposta.getStatus() != Status.SUCESSO) {
				Tools.alert("Falha: " + resposta.getStatus());
				return;
			}
			
			if(resposta.getObjeto() == null) {
				return;
			}
			
			List<Pessoa> pessoas = (List<Pessoa>) resposta.getObjeto();
			
			tabela.setDados(pessoas.toArray());
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	private void savePessoa(String nome) {
		Pessoa p = new Pessoa(nome);
		try {
			Resposta res = cliente.callService(p, "PessoaRota", "save");
			
			if (res.getStatus() != Status.SUCESSO) {
				Tools.alert("Falha: " + res.getStatus());
				return;
			}
			
			txtNome.setText("");
			
			getPessoas();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private MouseListener mouseListener() {
		return new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println(tabela.getSelecionado());
			}
		};
	}
	
}
