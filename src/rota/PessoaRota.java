package rota;

import java.util.List;

import org.chrissmb.socket.servidor.Acesso;
import org.chrissmb.socket.shared.Acao;

import data.Dados;
import model.Pessoa;

@Acesso("pessoa")
public class PessoaRota {

	@Acao("getAll")
	public List<Pessoa> getAll() {
		return Dados.pessoas;
	}
	
	@Acesso("savePessoa")
	@Acao("save")
	public void save(Pessoa pessoa) {
		Pessoa p = new Pessoa(pessoa.getNome());
		Dados.pessoas.add(p);
	}
}
