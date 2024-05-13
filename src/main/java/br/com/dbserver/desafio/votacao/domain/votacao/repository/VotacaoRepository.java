package br.com.dbserver.desafio.votacao.domain.votacao.repository;

import br.com.dbserver.desafio.votacao.domain.votacao.entity.Votacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VotacaoRepository extends JpaRepository<Votacao, Long> {

    @Query("""
            select count(v) > 0
            from Votacao v
            where v.usuario.id = :idUsuario
            and v.pauta.id = :idPauta
            """)
    Boolean findVotoByUsuarioPauta(Long idUsuario, Long idPauta);

    @Query("""
            select v
            from Votacao v
            where v.pauta.id = :idPauta
            """)
    List<Votacao> findVotosPorPauta(Long idPauta);

    @Query("""
            select v.voto, count(v.voto) 
            from Votacao v
            where v.pauta.id = :idPauta
            group by v.voto """)
    List<Object[]> countTotalVotosByPauta(Long idPauta);
}
