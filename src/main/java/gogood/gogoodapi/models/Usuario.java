package gogood.gogoodapi.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import gogood.gogoodapi.enums.GeneroEnum;
import gogood.gogoodapi.DTOS.AtualizarUsuarioPut;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor

public class Usuario {
    private Integer ID;
    private String nome;
    private String email;
    private String senha;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dt_nascimento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date created_at;
    @Enumerated(EnumType.STRING)
    private String genero;
    private String google_id;


    public Usuario(String nome, String email, String senha, String genero, Date dt_Nascimento, String google_id) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.dt_nascimento = dt_Nascimento;
        this.google_id = google_id;
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
