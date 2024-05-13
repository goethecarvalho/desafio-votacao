package br.com.dbserver.desafio.votacao.domain.usuarios.service;

import br.com.dbserver.desafio.votacao.domain.RegraDeNegocioException;
import br.com.dbserver.desafio.votacao.domain.usuarios.entity.Usuario;
import br.com.dbserver.desafio.votacao.domain.usuarios.repository.UsuarioRepository;
import br.com.dbserver.desafio.votacao.domain.usuarios.vo.DadosCadastroUsuario;
import br.com.dbserver.desafio.votacao.domain.usuarios.vo.DadosDetalhamentoUsuario;
import br.com.dbserver.desafio.votacao.infra.ValidarCpf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public DadosDetalhamentoUsuario cadastrarUsuario(DadosCadastroUsuario dados) {
        if (dados.nome() == null || dados.nome().isEmpty()) {
            throw new RegraDeNegocioException("O nome deve estar preenchido!");
        }

        if (dados.cpf() == null || dados.cpf().isEmpty()) {
            throw new RegraDeNegocioException("O CPF do usuário deve estar preenchido.");
        }

        ValidarCpf validador = new ValidarCpf();

        Boolean cpfValido = validador.valida(dados.cpf());

        /*if(!cpfValido) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{'status': 'UNABLE_TO_VOTE'}");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body("{'status': 'ABLE_TO_VOTE'}");
        }*/

        var usuario = new Usuario(dados);

        repository.save(usuario);

        return new DadosDetalhamentoUsuario(usuario);
    }

    public ResponseEntity<Page<DadosDetalhamentoUsuario>> listar(Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosDetalhamentoUsuario::new);
        return ResponseEntity.ok(page);
    }

    public DadosDetalhamentoUsuario detalhar(Long id) {
        var usuario = repository.findById(id).get();
        return new DadosDetalhamentoUsuario(usuario);
    }
}
