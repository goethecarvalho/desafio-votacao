package br.com.dbserver.desafio.votacao.domain.usuarios.vo;

import br.com.dbserver.desafio.votacao.domain.usuarios.entity.Usuario;

public record DadosDetalhamentoUsuario(Long id, String nome, String cpf) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getCpf());
    }
}
