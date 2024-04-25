package br.com.dbserver.desafio.votacao.controller;

import br.com.dbserver.desafio.votacao.domain.pautas.Pauta;
import br.com.dbserver.desafio.votacao.domain.pautas.PautaRepository;
import br.com.dbserver.desafio.votacao.domain.sessoes.*;
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
@RequestMapping("sessao")
public class SessaoController {

    @Autowired
    private SessaoRepository sessaoRepository;
    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private IniciarSessao iniciarSessao;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroSessao dados, UriComponentsBuilder uriBuilder){

        ResponseEntity<String> body = getStringResponseEntity(dados);
        if (body != null) return body;

        if(!pautaRepository.existsById(dados.idPauta())){
            return ResponseEntity.badRequest().body("Pauta não cadastrada.");
        }

        Boolean sessaoCadastrada = sessaoRepository.findSessaoByPauta(dados.idPauta());

        if (!sessaoCadastrada) {
            Sessao sessaoIniciada = iniciarSessao.iniciarSessao(dados);
            sessaoRepository.save(sessaoIniciada);
            var uri = uriBuilder.path("/sessao/{id}").buildAndExpand(sessaoIniciada.getId()).toUri();

            return ResponseEntity.created(uri).body(new DadosDetalhamentoSessao(sessaoIniciada));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{'mensagem': 'Sessão já cadastrada'}");
        }

    }

    private static ResponseEntity<String> getStringResponseEntity(DadosCadastroSessao dados) {
        if (dados.idPauta() == null) {
            return ResponseEntity.badRequest().body("O idPauta deve estar preenchido.");
        }
        if (dados.dataHoraInicio() == null) {
            return ResponseEntity.badRequest().body("O horário de inicio deve está preenchido.");
        }
        return null;
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoSessao>> listar(@PageableDefault(size = 10, sort = {"id"}) Pageable paginacao) {
        var page = sessaoRepository.findAll(paginacao).map(DadosDetalhamentoSessao::new);
        return ResponseEntity.ok(page);
    }

}
