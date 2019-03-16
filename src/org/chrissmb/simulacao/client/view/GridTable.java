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
import javax.swing.table.AbstractTableModel;

public class GridTable extends JScrollPane {

	private static final long serialVersionUID = 1L;
	
	private JTable tabela;
	private ModeloTabela modeloTabela;
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
		modeloTabela = new ModeloTabela();
		tabela = new JTable(modeloTabela);
		setViewportView(tabela);
		
		tabela.setRowSelectionAllowed(true);
		tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabela.setDefaultEditor(Object.class, null);
		tabela.getTableHeader().setReorderingAllowed(false);
	}
	
	public GridTable addColuna(String label, String atributo) {
		modeloTabela.addColumn(new Coluna(label, atributo));
		return this;
	}
	
	public GridTable addColuna(String label, String atributo, String mascara) {
		modeloTabela.addColumn(new Coluna(label, atributo, mascara));
		return this;
	}

	public void setDados(Object[] dados) {
		this.dados = dados;
		atualizar();
	}
	
	public void atualizar() {
		if (dados == null) return;
		
		modeloTabela.clearData();
		
		List<Object> linha;
		
		try {
			for (Object obj: dados) {
				Class<?> clazz = obj.getClass();
				linha = new ArrayList<Object>();
				
				for (Coluna coluna: modeloTabela.colunas) {
					Object celula = getRetornoMetodo(coluna.atributo, obj, clazz);
					linha.add(formataObjeto(celula, coluna.mascara));
				}
				modeloTabela.addRow(linha.toArray());
				
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
	
	private Object getRetornoMetodo(String atributo, Object obj, Class<?> clazz) throws Exception {
		
		int posicaoPonto;
		posicaoPonto = atributo.indexOf('.');
		boolean achouMetodo = false;
		
		if (posicaoPonto == -1) {
			for (Method metodo : clazz.getMethods()) {
				if (isGetDoAtributo(metodo.getName(), atributo)) {
					return metodo.invoke(obj);
				}
			}
			if (!achouMetodo) return null;
			
		} else {
			StringBuilder builder = new StringBuilder(atributo);
			String strObjFilho = builder.substring(0, posicaoPonto);
			String atributoFiho = builder.substring(posicaoPonto +1, atributo.length());
			for (Method metodo : clazz.getMethods()) {
				if (isGetDoAtributo(metodo.getName(), strObjFilho)) {
					Object objFilho = metodo.invoke(obj);
					if (objFilho == null) {
						return null;
					} else {
						Class<?> clazzFilho = objFilho.getClass();
						return getRetornoMetodo(atributoFiho, objFilho, clazzFilho);
					}
				}
			}
		}
		return null;
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
	
	private Object formataObjeto(Object obj, String mascara) {
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
		
		if (obj instanceof Number && mascara != null) {
			DecimalFormat df = new DecimalFormat(mascara);
			return df.format(obj);
		}
		
		return obj;
	}
	
	private boolean eNuloOuVazio(String str) {
		if (str == null) return true;
		return str.isEmpty();
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
	
	private class ModeloTabela extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		
		private List<Coluna> colunas;
		private List<Object[]> linhas;
		
		public ModeloTabela() {
			super();
			colunas = new ArrayList<>();
			linhas = new ArrayList<>();
		}

		@Override
		public int getColumnCount() {
			return colunas.size();
		}

		@Override
		public int getRowCount() {
			return linhas.size();
		}

		@Override
		public Object getValueAt(int l, int c) {
			return linhas.get(l)[c];
		}
		
		@Override
		public String getColumnName(int column) {
			return colunas.get(column).label;
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			Object obj = linhas.get(0)[columnIndex];
			
			if (obj instanceof String) return String.class;
			if (obj instanceof Number) return Number.class;
			if (obj instanceof Boolean) return Boolean.class;
			if (obj instanceof Date) return Date.class;
			if (obj instanceof Calendar) return Calendar.class;
			
			return Object.class;
		}
		
		public void addRow(Object[] linha) {
			int size = linhas.size();
			linhas.add(linha);
			fireTableRowsInserted(size, size);
		}
		
		public void clearData() {
			linhas = new ArrayList<>();
			fireTableStructureChanged();
		}
		
		public void addColumn(Coluna coluna) {
			colunas.add(coluna);
			fireTableStructureChanged();
		}
	}
}