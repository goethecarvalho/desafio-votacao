package br.com.dbserver.desafio.votacao.domain.pautas.service;

import br.com.dbserver.desafio.votacao.domain.RegraDeNegocioException;
import br.com.dbserver.desafio.votacao.domain.pautas.entity.Pauta;
import br.com.dbserver.desafio.votacao.domain.pautas.repository.PautaRepository;
import br.com.dbserver.desafio.votacao.domain.pautas.vo.DadosCadastroPauta;
import br.com.dbserver.desafio.votacao.domain.pautas.vo.DadosDetalhamentoPauta;
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
public class PautaService {

    private final PautaRepository repository;

    public PautaService(PautaRepository repository) {
        this.repository = repository;
    }

    public DadosDetalhamentoPauta cadastrarPauta(DadosCadastroPauta dados) {

        if (dados.descricao() == null || dados.descricao().isEmpty()) {
            throw new RegraDeNegocioException("A descrição da pauta não pode estar vazia.");
        }

        var pauta = new Pauta(dados);
        repository.save(pauta);

        return new DadosDetalhamentoPauta(pauta);
    }

    public ResponseEntity<Page<DadosDetalhamentoPauta>> listar(Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosDetalhamentoPauta::new);
        return ResponseEntity.ok(page);
    }

    public DadosDetalhamentoPauta detalhar(Long id) {
        var pauta = repository.findById(id).get();
        return new DadosDetalhamentoPauta(pauta);
    }
}
