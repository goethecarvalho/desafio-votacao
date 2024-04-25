package br.com.dbserver.desafio.votacao.domain.votacao;

import java.time.LocalDateTime;

public record DadosCadastroVotacao(Long id, Long idPauta, Long idUsuario, String voto, LocalDateTime dataHoraVotacao) {
}
