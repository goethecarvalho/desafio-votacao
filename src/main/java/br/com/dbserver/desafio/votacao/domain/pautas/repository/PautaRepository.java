package br.com.dbserver.desafio.votacao.domain.pautas.repository;

import br.com.dbserver.desafio.votacao.domain.pautas.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

}
