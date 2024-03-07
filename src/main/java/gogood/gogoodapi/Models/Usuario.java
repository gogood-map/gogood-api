package gogood.gogoodapi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gogood.gogoodapi.Enum.GeneroEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor

public class Usuario {
    @JsonIgnore
    @IgnoreForbiddenApisErrors(reason = "")
    private UUID ID;
    private String nome;
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private GeneroEnum genero;
    private Date dt_Nascimento;
    private Date created_at;

    public Usuario(String nome, String email, String senha, GeneroEnum genero, Date dt_Nascimento) {
        this.ID = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.dt_Nascimento = dt_Nascimento;
        this.created_at = new Date();
    }
}
