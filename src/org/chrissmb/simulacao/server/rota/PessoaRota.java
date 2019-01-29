package org.chrissmb.simulacao.server.rota;

import java.util.List;

import org.chrissmb.simulacao.server.data.Dados;
import org.chrissmb.simulacao.shared.model.Pessoa;
import org.chrissmb.socket.servidor.Acesso;
import org.chrissmb.socket.shared.Acao;

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
