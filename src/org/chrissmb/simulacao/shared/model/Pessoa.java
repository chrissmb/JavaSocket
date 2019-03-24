package org.chrissmb.simulacao.shared.model;

import java.io.Serializable;
import java.util.Objects;

public class Pessoa implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int idGen = 0;
	
	private int id;
	
	private String nome;
	
	public Pessoa(String nome) {
		this.id = idGen++;
		this.nome = nome;
	}
	
	public Pessoa(int id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Pessoa [id=" + id + ", nome=" + nome + "]";
	}

}
