package br.com.dbserver.desafio.votacao.domain.votacao.service;

import br.com.dbserver.desafio.votacao.domain.sessoes.repository.SessaoRepository;
import br.com.dbserver.desafio.votacao.domain.sessoes.vo.DadosDetalhamentoSessao;
import br.com.dbserver.desafio.votacao.domain.votacao.entity.Votacao;
import br.com.dbserver.desafio.votacao.domain.votacao.repository.VotacaoRepository;
import br.com.dbserver.desafio.votacao.domain.votacao.vo.DadosCadastroVotacao;
import br.com.dbserver.desafio.votacao.domain.pautas.repository.PautaRepository;
import br.com.dbserver.desafio.votacao.domain.usuarios.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotacaoService {

    @Autowired
    private SessaoRepository sessaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private VotacaoRepository votacaoRepository;

    public ResponseEntity<Page<DadosDetalhamentoSessao>> listar(Pageable paginacao) {
        var page= sessaoRepository.findAll(paginacao).map(DadosDetalhamentoSessao::new);
        return ResponseEntity.ok(page);
    }
    public DadosDetalhamentoSessao detalhar(Long id) {
        var sessao = sessaoRepository.findById(id).get();
        return new DadosDetalhamentoSessao(sessao);
    }
    public Votacao votar(DadosCadastroVotacao dados) {

        var pauta= pautaRepository.getReferenceById(dados.idPauta());

        var usuario = usuarioRepository.getReferenceById(dados.idUsuario());

        var votoEfetuado = new Votacao(null, usuario, pauta, dados.voto(), dados.dataHoraVotacao());

        return votoEfetuado;
    }

    public List<Object[]> countTotalVotosByPauta(Long idPauta) {
        return votacaoRepository.countTotalVotosByPauta(idPauta);
    }
}
