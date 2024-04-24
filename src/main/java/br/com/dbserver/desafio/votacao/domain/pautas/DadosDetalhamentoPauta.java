package br.com.dbserver.desafio.votacao.domain.pautas;

public record DadosDetalhamentoPauta(Long id, String descricao) {

    public DadosDetalhamentoPauta(Pauta pauta) {
        this(pauta.getId(), pauta.getDescricao());
    }
}
