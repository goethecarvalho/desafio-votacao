package br.com.dbserver.desafio.votacao.controller;

import br.com.dbserver.desafio.votacao.domain.pautas.service.PautaService;
import br.com.dbserver.desafio.votacao.domain.sessoes.service.SessaoService;
import br.com.dbserver.desafio.votacao.domain.sessoes.vo.DadosCadastroSessao;
import br.com.dbserver.desafio.votacao.domain.sessoes.vo.DadosDetalhamentoSessao;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("sessao")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    @Autowired
    private PautaService pautaService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroSessao dados, UriComponentsBuilder uriBuilder){

        var dadosSessaoCadastrada = sessaoService.cadastrarSessao(dados);
        var uri = uriBuilder.path("usuarios/{id}").buildAndExpand(dadosSessaoCadastrada.id()).toUri();
        return ResponseEntity.created(uri).body(dadosSessaoCadastrada);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoSessao>> listar(@PageableDefault(size = 10, sort = {"id"}) Pageable paginacao) {
        return sessaoService.listar(paginacao);
    }

}
