package br.com.dbserver.desafio.votacao.domain.votacao;

public record DadosDetalhamentoVotacao(Long id, Long idPauta, Long idUsuario, String voto) {
    public DadosDetalhamentoVotacao(Votacao voto) {
        this(voto.getId(), voto.getPauta().getId(), voto.getUsuario().getId(), voto.getVoto());
    }
}
