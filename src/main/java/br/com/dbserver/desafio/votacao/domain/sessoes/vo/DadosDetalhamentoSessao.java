package br.com.dbserver.desafio.votacao.domain.sessoes.vo;

import br.com.dbserver.desafio.votacao.domain.sessoes.entity.Sessao;

import java.time.LocalDateTime;

public record DadosDetalhamentoSessao(Long id, Long idPauta, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
    public DadosDetalhamentoSessao(Sessao sessao) {
        this(sessao.getId(), sessao.getPauta().getId(), sessao.getDataHoraInicio(), sessao.getDataHoraFim());
    }
}
