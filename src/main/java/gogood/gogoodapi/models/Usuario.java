package gogood.gogoodapi.models;

import gogood.gogoodapi.enums.GeneroEnum;
import gogood.gogoodapi.DTOS.AtualizarUsuarioPut;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class Usuario {
    private UUID ID;
    private String nome;
    private String email;
    private String senha;
    private Date dt_Nascimento;
    private Date created_at;
    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;


    public Usuario(String nome, String email, String senha, GeneroEnum genero, Date dt_Nascimento) {
        this.ID = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.dt_Nascimento = dt_Nascimento;
        this.created_at = new Date();
    }



    public void atualizar(AtualizarUsuarioPut usuarioAtualizado){
        this.nome = usuarioAtualizado.nome();
        this.email = usuarioAtualizado.email();
        this.senha = usuarioAtualizado.senha();
        this.genero = usuarioAtualizado.genero();
        this.dt_Nascimento = usuarioAtualizado.dt_Nascimento();
    }
}