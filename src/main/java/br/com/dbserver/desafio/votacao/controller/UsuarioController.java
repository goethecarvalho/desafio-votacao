package br.com.dbserver.desafio.votacao.controller;

import br.com.dbserver.desafio.votacao.domain.votacao.DadosDetalhamentoVotacao;
import br.com.dbserver.desafio.votacao.infra.ValidarCpf;
import br.com.dbserver.desafio.votacao.domain.usuarios.DadosCadastroUsuario;
import br.com.dbserver.desafio.votacao.domain.usuarios.DadosDetalhamentoUsuario;
import br.com.dbserver.desafio.votacao.domain.usuarios.Usuario;
import br.com.dbserver.desafio.votacao.domain.usuarios.UsuarioRepository;
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


@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder){

        if (dados.nome() == null || dados.nome().isEmpty()) {
            return ResponseEntity.badRequest().body("O nome do usuário deve estar preenchido.");
        }
        if (dados.cpf() == null || dados.cpf().isEmpty()) {
            return ResponseEntity.badRequest().body("O CPF do usuário deve estar preenchido.");
        }

        ValidarCpf validador = new ValidarCpf();

        Boolean cpfValido = validador.valida(dados.cpf());

        if(!cpfValido) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{'status': 'UNABLE_TO_VOTE'}");
        }else{
            var usuario = new Usuario(dados);
            repository.save(usuario);
            return ResponseEntity.status(HttpStatus.OK).body("{'status': 'ABLE_TO_VOTE'}");
        }

    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoUsuario>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosDetalhamentoUsuario::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        try {
            var usuario = repository.getReferenceById(id);
            return ResponseEntity.ok(new DadosDetalhamentoUsuario(usuario));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
