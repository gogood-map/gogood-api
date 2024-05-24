package gogood.gogoodapi.domain.DTOS;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor

public class UsuarioRegistro {
    private Integer ID;
    private String nome;
    private String email;
    private String senha;
    private Date dt_nascimento;
    private Date created_at;
    private String genero;
    private String google_id;
}
