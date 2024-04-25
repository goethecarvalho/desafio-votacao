package br.com.dbserver.desafio.votacao.domain.sessoes;

import java.time.LocalDateTime;

public record DadosCadastroSessao(Long id, Long idPauta, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim) {
}
