package gogood.gogoodapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dt_nascimento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date created_at;
    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;
    private String orientacao_sexual;


    public Usuario(String nome, String email, String senha, GeneroEnum genero, Date dt_Nascimento, String orientacao_sexual) {
        this.ID = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.dt_nascimento = dt_Nascimento;
        this.orientacao_sexual = orientacao_sexual;
        this.created_at = new Date();
    }



    public void atualizar(AtualizarUsuarioPut usuarioAtualizado){
        this.nome = usuarioAtualizado.nome();
        this.email = usuarioAtualizado.email();
        this.senha = usuarioAtualizado.senha();
        this.genero = usuarioAtualizado.genero();
        this.dt_nascimento = usuarioAtualizado.dt_Nascimento();
    }
}
