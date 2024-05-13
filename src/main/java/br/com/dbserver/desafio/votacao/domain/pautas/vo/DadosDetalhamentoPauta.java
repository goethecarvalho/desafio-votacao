package br.com.dbserver.desafio.votacao.domain.pautas.vo;

import br.com.dbserver.desafio.votacao.domain.pautas.entity.Pauta;

public record DadosDetalhamentoPauta(Long id, String descricao) {

    public DadosDetalhamentoPauta(Pauta pauta) {
        this(pauta.getId(), pauta.getDescricao());
    }
}
