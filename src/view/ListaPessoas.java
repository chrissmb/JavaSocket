package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

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

import org.chrissmb.socket.cliente.Cliente;
import org.chrissmb.socket.shared.Resposta;
import org.chrissmb.socket.shared.Status;

import model.Pessoa;

public class ListaPessoas extends JFrame {

	private JTable tabela;
	private JPanel panel;
	private Cliente cliente;
	private DefaultTableModel tModel;
	private JTextField txtNome;

	public ListaPessoas(Cliente cliente) {
		this.cliente = cliente;
		criarJanela();
	}

	private void criarJanela() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Pessoas");
		setSize(600, 500);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 300, 200);
		panel.add(scrollPane);
		
		tModel = new DefaultTableModel();
		tModel.addColumn("Id");
		tModel.addColumn("Nome");
		tModel.addRow(new Object[] {"", ""});
		
		tabela = new JTable(tModel);
		scrollPane.setViewportView(tabela);
		
		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.setBounds(10, 400, 100, 30);
		
		btnConsultar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				getPessoas();
			}
		});
		
		panel.add(btnConsultar);
		
		JLabel lblNome = new JLabel("Adicionar pessoa");
		lblNome.setBounds(10, 250, 100, 30);
		txtNome = new JTextField();
		txtNome.setBounds(10, 280, 200, 30);
		
		panel.add(lblNome);
		panel.add(txtNome);
		
		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(220, 280, 100, 30);
		
		btnSalvar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				savePessoa(txtNome.getText().trim());
			}
		});
		
		panel.add(btnSalvar);
		
		setVisible(true);
	}
	
	private void getPessoas() {
		try {
			Resposta resposta =  cliente.callService("PessoaRota", "getAll");
			
			if (resposta.getStatus() != Status.SUCESSO) {
				JOptionPane.showMessageDialog(null, "Erro: " + resposta.getStatus());
				return;
			}
			
			if(resposta.getObjeto() == null) {
				return;
			}
			
			List<Pessoa> pessoas = (List<Pessoa>) resposta.getObjeto();
			
			preencheTabela(pessoas);
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	private void preencheTabela(List<Pessoa> pessoas) {
		while (tModel.getRowCount() > 0) {
			tModel.removeRow(0);
		}
		for (Pessoa p : pessoas) {
			tModel.addRow(new Object[] {p.getId(), p.getNome()});
		}
	}
	
	private void savePessoa(String nome) {
		Pessoa p = new Pessoa(nome);
		try {
			Resposta res = cliente.callService(p, "PessoaRota", "save");
			
			if (res.getStatus() != Status.SUCESSO) {
				System.out.println("Erro: " + res.getStatus());
				return;
			}
			
			txtNome.setText("");
			
			getPessoas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
