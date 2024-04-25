package br.com.dbserver.desafio.votacao.domain.sessoes;

import br.com.dbserver.desafio.votacao.domain.pautas.PautaRepository;
import br.com.dbserver.desafio.votacao.domain.votacao.Votacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IniciarSessao {

    @Autowired
    private PautaRepository pautaRepository;

    public Sessao iniciarSessao(DadosCadastroSessao dados) {

        var pauta= pautaRepository.getReferenceById(dados.idPauta());


        var sessaoIniciada = new Sessao(null, pauta, dados.dataHoraInicio(), dados.dataHoraFim());

        return sessaoIniciada;
    }

}
