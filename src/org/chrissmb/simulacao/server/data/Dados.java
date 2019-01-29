package org.chrissmb.simulacao.server.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.chrissmb.simulacao.shared.model.Pessoa;
import org.chrissmb.simulacao.shared.model.Usuario;

public class Dados implements Serializable {

	private static final long serialVersionUID = 1L;

	public static List<Pessoa> pessoas = new ArrayList<Pessoa>(Arrays.asList(
			new Pessoa("Joao"),
			new Pessoa("Maria"),
			new Pessoa("Hugo")
	));
	
	public static Usuario getAdmin() {
		Usuario admin = new Usuario();
		admin.setLogin("admin");
		admin.setSenha("1234");
		admin.setAcessos(new ArrayList<String>());
		admin.getAcessos().add("pessoa");
		admin.getAcessos().add("savePessoa");
		return admin;
	}
	
}
