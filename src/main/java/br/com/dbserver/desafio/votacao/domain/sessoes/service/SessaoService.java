package br.com.dbserver.desafio.votacao.domain.sessoes.service;

import br.com.dbserver.desafio.votacao.domain.RegraDeNegocioException;
import br.com.dbserver.desafio.votacao.domain.pautas.repository.PautaRepository;
import br.com.dbserver.desafio.votacao.domain.sessoes.repository.SessaoRepository;
import br.com.dbserver.desafio.votacao.domain.sessoes.vo.DadosCadastroSessao;
import br.com.dbserver.desafio.votacao.domain.sessoes.entity.Sessao;
import br.com.dbserver.desafio.votacao.domain.sessoes.vo.DadosDetalhamentoSessao;
import br.com.dbserver.desafio.votacao.domain.votacao.repository.VotacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessaoService {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private VotacaoRepository votacaoRepository;

    public DadosDetalhamentoSessao cadastrarSessao(DadosCadastroSessao dados) {

        if(!pautaRepository.existsById(dados.idPauta())){
            throw new RegraDeNegocioException("Pauta não cadastrada!");
        }

        Boolean sessaoCadastrada = sessaoRepository.findSessaoByPauta(dados.idPauta());

        if (!sessaoCadastrada) {
            Sessao sessaoIniciada = iniciarSessao(dados);
            sessaoRepository.save(sessaoIniciada);
        } else {
            throw new RegraDeNegocioException("Sessão já cadastrada");
        }

        var sessao = new Sessao(dados);

        sessaoRepository.save(sessao);

        return new DadosDetalhamentoSessao(sessao);
    }

    public Sessao iniciarSessao(DadosCadastroSessao dados) {

        var pauta= pautaRepository.getReferenceById(dados.idPauta());

        var sessaoIniciada = new Sessao(null, pauta, dados.dataHoraInicio(), dados.dataHoraFim());

        return sessaoIniciada;
    }

    public ResponseEntity<Page<DadosDetalhamentoSessao>> listar(Pageable paginacao) {
        var page = sessaoRepository.findAll(paginacao).map(DadosDetalhamentoSessao::new);
        return ResponseEntity.ok(page);
    }

    public Boolean validaVotoByPautaHorario(Long idPauta, LocalDateTime dataHoraVotacao) {
        return sessaoRepository.validaVotoByPautaHorario(idPauta, dataHoraVotacao);
    }
}
