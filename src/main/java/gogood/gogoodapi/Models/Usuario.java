package gogood.gogoodapi.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gogood.gogoodapi.Enum.GeneroEnum;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;

import java.util.Date;
@Getter
@Setter
public class Usuario {
    @JsonIgnore
    @IgnoreForbiddenApisErrors(reason = "")
    private Integer ID;
    private String nome;
    private String email;
    private String senha;
    private GeneroEnum genero;
    private Date dt_Nascimento;

    public Usuario(String nome, String email, String senha, GeneroEnum genero, Date dt_Nascimento) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.genero = genero;
        this.dt_Nascimento = dt_Nascimento;
    }
}
