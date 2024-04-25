package br.com.dbserver.desafio.votacao.controller;

import br.com.dbserver.desafio.votacao.domain.pautas.DadosDetalhamentoPauta;
import br.com.dbserver.desafio.votacao.domain.pautas.PautaRepository;
import br.com.dbserver.desafio.votacao.domain.sessoes.SessaoRepository;
import br.com.dbserver.desafio.votacao.infra.ValidarCpf;
import br.com.dbserver.desafio.votacao.domain.usuarios.UsuarioRepository;
import br.com.dbserver.desafio.votacao.domain.votacao.*;
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
    private EfetuarVotacao efetuarVotacao;

    @Autowired
    private SessaoRepository sessaoRepository;

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
            Votacao votoEfetuado = efetuarVotacao.votar(dados);

            Boolean sessaoAberta = sessaoRepository.validaVotoByPautaHorario(dados.idPauta(), dados.dataHoraVotacao());

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
    public ResponseEntity<Page<DadosDetalhamentoVotacao>> listar(@PageableDefault(size = 10, sort = {"id"}) Pageable paginacao) {
        var page = votacaoRepository.findAll(paginacao).map(DadosDetalhamentoVotacao::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{idPauta}")
    public ResponseEntity detalhar(@PathVariable Long idPauta){
        List<Votacao> votacoes = votacaoRepository.findVotosPorPauta(idPauta);

        if (votacoes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Se houver votações, você pode retornar diretamente a lista de votações
        return ResponseEntity.ok(votacoes);
    }

    @GetMapping("/total-votos/{idPauta}")
    public ResponseEntity<List<Object[]>> totalVotos(@PathVariable Long idPauta) {
        List<Object[]> totalVotos = votacaoRepository.countTotalVotosByPauta(idPauta);
        return ResponseEntity.ok(totalVotos);
    }

}
