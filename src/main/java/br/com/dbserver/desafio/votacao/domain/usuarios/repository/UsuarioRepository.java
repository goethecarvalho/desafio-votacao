package br.com.dbserver.desafio.votacao.domain.usuarios.repository;

import br.com.dbserver.desafio.votacao.domain.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("""
            select u.cpf
            from Usuario u
            where u.id = :idUsuario
            """)
    String retornaCpf(Long idUsuario);

}
