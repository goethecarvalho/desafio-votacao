package br.com.dbserver.desafio.votacao.domain.usuarios.service;

import br.com.dbserver.desafio.votacao.domain.RegraDeNegocioException;
import br.com.dbserver.desafio.votacao.domain.usuarios.entity.Usuario;
import br.com.dbserver.desafio.votacao.domain.usuarios.repository.UsuarioRepository;
import br.com.dbserver.desafio.votacao.domain.usuarios.vo.DadosCadastroUsuario;
import br.com.dbserver.desafio.votacao.domain.usuarios.vo.DadosDetalhamentoUsuario;
import br.com.dbserver.desafio.votacao.infra.ValidarCpf;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "listaUsuarios", allEntries = true)
    public DadosDetalhamentoUsuario cadastrarUsuario(DadosCadastroUsuario dados) {
        if (dados.nome() == null || dados.nome().isEmpty()) {
            throw new RegraDeNegocioException("O nome deve estar preenchido!");
        }

        if (dados.cpf() == null || dados.cpf().isEmpty()) {
            throw new RegraDeNegocioException("O CPF do usuário deve estar preenchido.");
        }

        ValidarCpf validador = new ValidarCpf();

        Boolean cpfValido = validador.valida(dados.cpf());

        if(!cpfValido) {

        }else{

        }

        var usuario = new Usuario(dados);

        repository.save(usuario);

        return new DadosDetalhamentoUsuario(usuario);
    }

    @Cacheable(value = "listaUsuarios")
    public ResponseEntity<Page<DadosDetalhamentoUsuario>> listar(Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosDetalhamentoUsuario::new);
        return ResponseEntity.ok(page);
    }

    public DadosDetalhamentoUsuario detalhar(Long id) {
        var usuario = repository.findById(id).get();
        return new DadosDetalhamentoUsuario(usuario);
    }

    @CacheEvict(value = "listaUsuarios", allEntries = true)
    public DadosDetalhamentoUsuario atualizarUsuario(Long id, DadosCadastroUsuario dados) {
        var usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        DadosCadastroUsuario usuarioAtualizado = new DadosCadastroUsuario(
                dados.id(),
                dados.nome(),
                dados.cpf()
        );

        usuario.atualizarDados(usuarioAtualizado);

        repository.save(usuario);

        return new DadosDetalhamentoUsuario(usuario);
    }

    @CacheEvict(value = "listaUsuarios", allEntries = true)
    public void deletarUsuario(Long id) {
        var usuario = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        repository.delete(usuario);
    }
}
