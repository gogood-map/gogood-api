package gogood.gogoodapi.domain.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import gogood.gogoodapi.domain.DTOS.UsuarioAtualizado;
import gogood.gogoodapi.domain.models.endereco.Enderecos;
import gogood.gogoodapi.domain.models.endereco.EnderecosUsuarios;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;


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

    @OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<EnderecosUsuarios> endereco;


    public void atualizar(UsuarioAtualizado usuarioAtualizado){
        this.nome = usuarioAtualizado.nome();
        this.email = usuarioAtualizado.email();
        this.genero = usuarioAtualizado.genero();
        this.dt_nascimento = usuarioAtualizado.dt_Nascimento();
    }
}