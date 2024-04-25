package br.com.dbserver.desafio.votacao.domain.sessoes;

import br.com.dbserver.desafio.votacao.domain.votacao.Votacao;

import java.time.LocalDateTime;

public record DadosDetalhamentoSessao(Long id, Long idPauta, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
    public DadosDetalhamentoSessao(Sessao sessao) {
        this(sessao.getId(), sessao.getPauta().getId(), sessao.getDataHoraInicio(), sessao.getDataHoraFim());
    }
}
