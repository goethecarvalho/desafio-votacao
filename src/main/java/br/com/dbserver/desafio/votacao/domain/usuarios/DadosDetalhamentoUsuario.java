package br.com.dbserver.desafio.votacao.domain.usuarios;

import br.com.dbserver.desafio.votacao.domain.pautas.Pauta;

public record DadosDetalhamentoUsuario(Long id, String nome, String cpf) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getCpf());
    }
}
