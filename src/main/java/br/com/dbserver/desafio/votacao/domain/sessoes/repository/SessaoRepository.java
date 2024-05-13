package br.com.dbserver.desafio.votacao.domain.sessoes.repository;

import br.com.dbserver.desafio.votacao.domain.sessoes.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    @Query("""
            select count(s) > 0
            from Sessao s
            where s.pauta.id = :idPauta
            """)
    Boolean findSessaoByPauta(Long idPauta);

    @Query("""
            select count(s) > 0
            from Sessao s
            where s.pauta.id = :idPauta
            and :dataHoraVotacao between s.dataHoraInicio and s.dataHoraFim
            """)
    Boolean validaVotoByPautaHorario(Long idPauta, LocalDateTime dataHoraVotacao);


}
