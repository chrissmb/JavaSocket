package org.chrissmb.simulacao.client.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class GridTable extends JScrollPane {
	
	private JTable tabela;
	private DefaultTableModel modelo;
	private Map<String, String> colunas;
	private Object[] dados;
	
	public GridTable() {
		super();
		build();
	}
	
	public GridTable(Object[] dados) {
		super();
		this.dados = dados;
		build();
	}

	private void build() {
		modelo = new DefaultTableModel();
		tabela = new JTable(modelo);
		colunas = new HashMap<>();
		setViewportView(tabela);
		
		tabela.setRowSelectionAllowed(true);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.setDefaultEditor(Object.class, null);
		tabela.getTableHeader().setReorderingAllowed(false);
	}
	
	public GridTable addColuna(String label, String atributo) {
		colunas.put(label, atributo);
		return this;
	}
	
	public void atualizar() {
		limpar();
		for (String label: colunas.keySet()) {
			modelo.addColumn(label);
		}
		processaDados();
	}
	
	public void limpar() {
		modelo.setColumnCount(0);
		while (modelo.getRowCount() > 0) {
			modelo.removeRow(0);
		}
	}

	public void setDados(Object[] dados) {
		this.dados = dados;
		atualizar();
	}
	
	private void processaDados() {
		if (dados == null) return;
		
		List<String> atributos = new ArrayList<String>();
		for (String atributo: colunas.values()) {
			atributos.add(atributo);
		}
		
		List<Object> linha;
		
		try {
			for (Object obj: dados) {
				Class<?> clazz = obj.getClass();
				linha = new ArrayList<Object>();
				
				for (String atributo: atributos) {
					for (Method metodo : clazz.getDeclaredMethods()) {
						if (isGetDoAtributo(metodo.getName(), atributo)) {
							linha.add(metodo.invoke(obj));
						}
					}
				}
				modelo.addRow(linha.toArray());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isGetDoAtributo(String metodo, String atributo) {
		StringBuilder builder = new StringBuilder(atributo);
		builder.deleteCharAt(0);
		builder.insert(0, atributo.toUpperCase().charAt(0));
		builder.insert(0, "get");
		return metodo.equals(builder.toString());
	}
	
	public Object getSelecionado() {
		if (dados == null) return null;
		int i = tabela.getSelectedRow();
		if (i == -1) return null;
		return dados[i];
	}
	
	public void addMouseListener(MouseListener listener) {
		tabela.addMouseListener(listener);
	}
}
