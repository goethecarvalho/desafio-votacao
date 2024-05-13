package br.com.dbserver.desafio.votacao.controller;

import br.com.dbserver.desafio.votacao.domain.sessoes.service.SessaoService;
import br.com.dbserver.desafio.votacao.domain.sessoes.vo.DadosDetalhamentoSessao;
import br.com.dbserver.desafio.votacao.domain.usuarios.vo.DadosDetalhamentoUsuario;
import br.com.dbserver.desafio.votacao.domain.votacao.entity.Votacao;
import br.com.dbserver.desafio.votacao.domain.pautas.repository.PautaRepository;
import br.com.dbserver.desafio.votacao.domain.sessoes.repository.SessaoRepository;
import br.com.dbserver.desafio.votacao.domain.usuarios.repository.UsuarioRepository;
import br.com.dbserver.desafio.votacao.domain.votacao.repository.VotacaoRepository;
import br.com.dbserver.desafio.votacao.domain.votacao.vo.DadosCadastroVotacao;
import br.com.dbserver.desafio.votacao.domain.votacao.vo.DadosDetalhamentoVotacao;
import br.com.dbserver.desafio.votacao.domain.votacao.service.VotacaoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;


@RestController
@RequestMapping("votacao")
public class VotacaoController {

    @Autowired
    private VotacaoRepository votacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private VotacaoService votacaoService;

    @Autowired
    private SessaoService sessaoService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroVotacao dados, UriComponentsBuilder uriBuilder){

        ResponseEntity<String> body = getStringResponseEntity(dados);
        if (body != null) return body;

        if(!usuarioRepository.existsById(dados.idUsuario())){
            return ResponseEntity.badRequest().body("Usuário não cadastrado.");
        }

        if(!pautaRepository.existsById(dados.idPauta())){
            return ResponseEntity.badRequest().body("Pauta não cadastrada.");
        }

        Boolean votoDuplicado = votacaoRepository.findVotoByUsuarioPauta(dados.idUsuario(), dados.idPauta());

        if (!votoDuplicado) {
            Votacao votoEfetuado = votacaoService.votar(dados);

            Boolean sessaoAberta = sessaoService.validaVotoByPautaHorario(dados.idPauta(), dados.dataHoraVotacao());

            if (sessaoAberta){
                votacaoRepository.save(votoEfetuado);
                var uri = uriBuilder.path("/votacao/{id}").buildAndExpand(votoEfetuado.getId()).toUri();

                return ResponseEntity.created(uri).body(new DadosDetalhamentoVotacao(votoEfetuado));
            }else{
                return ResponseEntity.status(HttpStatus.CONFLICT).body("{'mensagem': 'Sessão encerrada'}");
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{'mensagem': 'Voto já computado'}");
        }

    }

    private static ResponseEntity<String> getStringResponseEntity(DadosCadastroVotacao dados) {
        if (dados.idPauta() == null) {
            return ResponseEntity.badRequest().body("O idPauta deve estar preenchido.");
        }
        if (dados.idUsuario() == null) {
            return ResponseEntity.badRequest().body("O idUsuario deve estar preenchido.");
        }
        if (dados.voto() == null || dados.voto().isEmpty()) {
            return ResponseEntity.badRequest().body("O voto deve estar preenchido.");
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoSessao>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return votacaoService.listar(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoSessao> detalhar(@PathVariable Long id) {
        var sessao = votacaoService.detalhar(id);
        return ResponseEntity.ok(sessao);
    }

    @GetMapping("/total-votos/{idPauta}")
    public ResponseEntity<List<Object[]>> totalVotos(@PathVariable Long idPauta) {
        List<Object[]> totalVotos = votacaoService.countTotalVotosByPauta(idPauta);
        return ResponseEntity.ok(totalVotos);
    }

}
