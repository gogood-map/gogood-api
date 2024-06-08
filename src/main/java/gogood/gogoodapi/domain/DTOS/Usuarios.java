package gogood.gogoodapi.domain.DTOS;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;
    private LocalDate dt_nascimento;
    private LocalDate created_at;
    private String genero;
    private String google_id;


    public void atualizar(UsuarioAtualizado usuarioAtualizado){
        this.nome = usuarioAtualizado.nome();
        this.email = usuarioAtualizado.email();
        this.genero = usuarioAtualizado.genero();
        this.dt_nascimento = usuarioAtualizado.dt_Nascimento();
    }
}