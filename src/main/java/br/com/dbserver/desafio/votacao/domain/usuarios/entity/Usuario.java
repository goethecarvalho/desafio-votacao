package br.com.dbserver.desafio.votacao.domain.usuarios.entity;

import br.com.dbserver.desafio.votacao.domain.usuarios.vo.DadosCadastroUsuario;
import br.com.dbserver.desafio.votacao.domain.usuarios.vo.UsuarioStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;

    @Enumerated(EnumType.STRING)
    private UsuarioStatus usuarioStatus;

    public Usuario(DadosCadastroUsuario dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
    }

    public void atualizarDados(DadosCadastroUsuario dados) {
        this.nome = dados.nome();
        this.cpf = dados.cpf();
    }
}
