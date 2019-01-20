package rota;

import java.util.List;

import org.chrissmb.socket.shared.Acao;

import data.Dados;
import model.Pessoa;

public class PessoaRota {

	@Acao(nome="getAll")
	public List<Pessoa> getAll() {
		return Dados.pessoas;
	}
	
	@Acao(nome="save")
	public void save(Pessoa pessoa) {
		Pessoa p = new Pessoa(pessoa.getNome());
		Dados.pessoas.add(p);
	}
}
