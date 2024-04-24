package br.com.dbserver.desafio.votacao.domain.usuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("""
            select u.cpf
            from Usuario u
            where u.id = :idUsuario
            """)
    String retornaCpf(Long idUsuario);

}
