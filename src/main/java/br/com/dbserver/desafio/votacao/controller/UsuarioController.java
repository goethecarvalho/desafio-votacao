package br.com.dbserver.desafio.votacao.controller;

import br.com.dbserver.desafio.votacao.domain.usuarios.service.UsuarioService;
import br.com.dbserver.desafio.votacao.domain.usuarios.vo.DadosCadastroUsuario;
import br.com.dbserver.desafio.votacao.domain.usuarios.vo.DadosDetalhamentoUsuario;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService cadastroDeUsuarioService) {
        this.service = cadastroDeUsuarioService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder){

        var dadosUsuarioCadastrado = service.cadastrarUsuario(dados);
        var uri = uriBuilder.path("usuarios/{id}").buildAndExpand(dadosUsuarioCadastrado.id()).toUri();
        return ResponseEntity.created(uri).body(dadosUsuarioCadastrado);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoUsuario>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return service.listar(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoUsuario> detalhar(@PathVariable Long id) {
        var usuario = service.detalhar(id);
        return ResponseEntity.ok(usuario);
    }
}
