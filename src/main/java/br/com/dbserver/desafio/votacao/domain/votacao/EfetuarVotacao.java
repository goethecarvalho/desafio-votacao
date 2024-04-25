package br.com.dbserver.desafio.votacao.domain.votacao;

import br.com.dbserver.desafio.votacao.domain.pautas.PautaRepository;
import br.com.dbserver.desafio.votacao.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EfetuarVotacao {

    @Autowired
    private PautaRepository pautaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Votacao votar(DadosCadastroVotacao dados) {

        var pauta= pautaRepository.getReferenceById(dados.idPauta());

        var usuario = usuarioRepository.getReferenceById(dados.idUsuario());

        var votoEfetuado = new Votacao(null, usuario, pauta, dados.voto(), dados.dataHoraVotacao());

        return votoEfetuado;
    }

}
