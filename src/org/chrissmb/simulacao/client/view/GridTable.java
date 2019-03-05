package org.chrissmb.simulacao.client.view;

import java.awt.event.MouseListener;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class GridTable extends JScrollPane {
	
	private JTable tabela;
	private DefaultTableModel modelo;
	private List<Coluna> colunas;
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
		colunas = new ArrayList<Coluna>();
		setViewportView(tabela);
		
		tabela.setRowSelectionAllowed(true);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.setDefaultEditor(Object.class, null);
		tabela.getTableHeader().setReorderingAllowed(false);
	}
	
	public GridTable addColuna(String label, String atributo) {
		colunas.add(new Coluna(label, atributo));
		return this;
	}
	
	public GridTable addColuna(String label, String atributo, String mascara) {
		colunas.add(new Coluna(label, atributo, mascara));
		return this;
	}
	
	public void atualizar() {
		limpar();
		for (Coluna coluna: colunas) {
			modelo.addColumn(coluna.label);
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
		
		List<Object> linha;
		
		try {
			for (Object obj: dados) {
				Class<?> clazz = obj.getClass();
				linha = new ArrayList<Object>();
				
				for (Coluna coluna: colunas) {
					execMetodoDoObjeto(linha, coluna.atributo, coluna.mascara, obj, clazz);
				}
				modelo.addRow(linha.toArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean isGetDoAtributo(String metodo, String atributo) {
		StringBuilder builder = new StringBuilder(atributo);
		builder.deleteCharAt(0);
		builder.insert(0, atributo.toUpperCase().charAt(0));
		return metodo.equals("get" + builder.toString())
				|| metodo.equals("is" + builder.toString())
				|| metodo.equals(atributo);
	}
	
	private void execMetodoDoObjeto(List<Object> linha, String atributo, 
			String mascara, Object obj, Class<?> clazz) throws Exception {
		
		int posicaoPonto;
		posicaoPonto = atributo.indexOf('.');
		boolean achouMetodo = false;
		
		if (posicaoPonto == -1) {
			for (Method metodo : clazz.getDeclaredMethods()) {
				if (isGetDoAtributo(metodo.getName(), atributo)) {
					linha.add(formataObjeto(metodo.invoke(obj), mascara));
					achouMetodo = true;
					break;
				}
			}
			if (!achouMetodo) linha.add("");
			
		} else {
			StringBuilder builder = new StringBuilder(atributo);
			String strObjFilho = builder.substring(0, posicaoPonto);
			String atributoFiho = builder.substring(posicaoPonto +1, atributo.length());
			for (Method metodo : clazz.getDeclaredMethods()) {
				if (isGetDoAtributo(metodo.getName(), strObjFilho)) {
					Object objFilho = metodo.invoke(obj);
					if (objFilho == null) {
						linha.add("");
						achouMetodo = true;
					} else {
						Class<?> clazzFilho = objFilho.getClass();
						execMetodoDoObjeto(linha, atributoFiho, mascara, objFilho, clazzFilho);
					}
					achouMetodo = true;
					break;
				}
			}
			if (!achouMetodo) linha.add("");
			
		}
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
	
	private class Coluna {
		
		private String label;
		private String atributo;
		private String mascara;
		
		public Coluna(String label, String atributo) {
			this.label = label;
			this.atributo = atributo;
		}
		
		public Coluna(String label, String atributo, String mascara) {
			this.label = label;
			this.atributo = atributo;
			this.mascara = mascara;
		}

		@Override
		public String toString() {
			return String.format("Coluna [label=%s, atributo=%s, mascara=%s]", label, atributo, mascara);
		}

	}
	
	private String formataObjeto(Object obj, String mascara) {
		if (obj == null) return "";
		
		if (obj instanceof Date) {
			if (eNuloOuVazio(mascara)) mascara = "dd/MM/yyy HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(mascara);
			return sdf.format(obj);
		}
		
		if (obj instanceof Calendar) {
			Calendar cal = (Calendar) obj;
			if (eNuloOuVazio(mascara)) mascara = "dd/MM/yyy HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(mascara);
			return sdf.format(cal.getTime());
		}
		
		if (obj instanceof Boolean) {
			if ((Boolean)obj) {
				return "\u2713";
			} else {
				return "x";
			}
		}
		
		if (obj instanceof Number && mascara != null) {
			DecimalFormat df = new DecimalFormat(mascara);
			return df.format(obj);
		}
		
		return obj.toString();
	}
	
	private boolean eNuloOuVazio(String str) {
		if (str == null) return true;
		return str.isEmpty();
	}
}
