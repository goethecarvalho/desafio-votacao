package br.com.dbserver.desafio.votacao.controller;

import br.com.dbserver.desafio.votacao.domain.pautas.service.PautaService;
import br.com.dbserver.desafio.votacao.domain.pautas.vo.DadosCadastroPauta;
import br.com.dbserver.desafio.votacao.domain.pautas.vo.DadosDetalhamentoPauta;
import br.com.dbserver.desafio.votacao.domain.pautas.entity.Pauta;
import br.com.dbserver.desafio.votacao.domain.pautas.repository.PautaRepository;
import br.com.dbserver.desafio.votacao.domain.usuarios.vo.DadosDetalhamentoUsuario;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("pautas")
public class PautaController {

    @Autowired
    private PautaService service;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPauta dados, UriComponentsBuilder uriBuilder){

        var dadosPautaCadastrada = service.cadastrarPauta(dados);
        var uri = uriBuilder.path("usuarios/{id}").buildAndExpand(dadosPautaCadastrada.id()).toUri();
        return ResponseEntity.created(uri).body(dadosPautaCadastrada);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoPauta>> listar(@PageableDefault(size = 10, sort = {"descricao"}) Pageable paginacao) {

        return service.listar(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPauta> detalhar(@PathVariable Long id) {
        var pauta = service.detalhar(id);
        return ResponseEntity.ok(pauta);
    }
}
