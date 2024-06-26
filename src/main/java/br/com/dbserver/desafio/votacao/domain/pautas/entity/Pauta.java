package br.com.dbserver.desafio.votacao.domain.pautas.entity;

import br.com.dbserver.desafio.votacao.domain.pautas.vo.DadosCadastroPauta;
import br.com.dbserver.desafio.votacao.domain.pautas.vo.DadosDetalhamentoPauta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "pautas")
@Entity(name = "Pauta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;

    public Pauta(DadosCadastroPauta dados) {
        this.descricao = dados.descricao();
    }

    public Pauta(DadosDetalhamentoPauta dados) {
        this.id = dados.id();
        this.descricao = dados.descricao();
    }
}
