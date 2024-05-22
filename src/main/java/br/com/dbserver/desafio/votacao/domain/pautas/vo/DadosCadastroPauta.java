package br.com.dbserver.desafio.votacao.domain.pautas.vo;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroPauta(
        Long id,
        @NotNull(message = "A descrição é obrigatória!")
        String descricao) {
}
