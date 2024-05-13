package br.com.dbserver.desafio.votacao.domain.votacao.vo;

import br.com.dbserver.desafio.votacao.domain.votacao.entity.Votacao;

import java.time.LocalDateTime;

public record DadosDetalhamentoVotacao(Long id, Long idPauta, Long idUsuario, String voto, LocalDateTime dataHoraVotacao) {
    public DadosDetalhamentoVotacao(Votacao voto) {
        this(voto.getId(), voto.getPauta().getId(), voto.getUsuario().getId(), voto.getVoto(), voto.getDataHoraVotacao());
    }
}
