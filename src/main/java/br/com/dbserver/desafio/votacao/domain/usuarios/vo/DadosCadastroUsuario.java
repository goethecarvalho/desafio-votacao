package br.com.dbserver.desafio.votacao.domain.usuarios.vo;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroUsuario(
        Long id,
        @NotNull(message = "O nome é obrigatório!")
        String nome,
        @NotNull(message = "O CPF é obrigatório!")
        String cpf
) {
}
