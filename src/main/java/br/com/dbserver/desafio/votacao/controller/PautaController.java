package br.com.dbserver.desafio.votacao.controller;

import br.com.dbserver.desafio.votacao.domain.pautas.DadosCadastroPauta;
import br.com.dbserver.desafio.votacao.domain.pautas.DadosDetalhamentoPauta;
import br.com.dbserver.desafio.votacao.domain.pautas.Pauta;
import br.com.dbserver.desafio.votacao.domain.pautas.PautaRepository;
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
    private PautaRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPauta dados, UriComponentsBuilder uriBuilder){

        if (dados.descricao() == null || dados.descricao().isEmpty()) {
            return ResponseEntity.badRequest().body("A descrição da pauta não pode estar vazia.");
        }

        var pauta = new Pauta(dados);
        repository.save(pauta);
        var uri = uriBuilder.path("/pautas/{id}").buildAndExpand(pauta.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoPauta(pauta));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoPauta>> listar(@PageableDefault(size = 10, sort = {"descricao"}) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosDetalhamentoPauta::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var pauta = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoPauta(pauta));
    }
}
