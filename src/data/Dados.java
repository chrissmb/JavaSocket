package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Pessoa;

public class Dados implements Serializable {

	private static final long serialVersionUID = 1L;

	public static List<Pessoa> pessoas = new ArrayList<Pessoa>(Arrays.asList(
			new Pessoa("Joao"),
			new Pessoa("Maria"),
			new Pessoa("Hugo")
	));
	
}
