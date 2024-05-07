package gogood.gogoodapi.domain.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import gogood.gogoodapi.domain.DTOS.AtualizarUsuarioPut;
import gogood.gogoodapi.domain.DTOS.CriarUsuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer ID;
    private String nome;
    private String email;
    private String senha;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dt_nascimento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Sao_Paulo")
    private Date created_at;
    private String genero;
    private String google_id;


    public Usuarios(String nome, String email, String senha, String genero, Date dt_Nascimento, String google_id) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.dt_nascimento = dt_Nascimento;
        this.google_id = google_id;
        this.created_at = new Date();
    }

    public Usuarios(CriarUsuario novoUsuario) {
        this.nome = novoUsuario.nome();
        this.email = novoUsuario.email();
        this.senha = novoUsuario.senha();
        this.genero = novoUsuario.genero();
        this.dt_nascimento = novoUsuario.dt_Nascimento();
        this.google_id = novoUsuario.google_id();
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
