package br.com.dbserver.desafio.votacao.domain.sessoes;

import br.com.dbserver.desafio.votacao.domain.pautas.Pauta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "sessao")
@Entity(name = "Sessao")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id")
    private Pauta pauta;

    private LocalDateTime dataHoraInicio;

    private LocalDateTime dataHoraFim;


}
