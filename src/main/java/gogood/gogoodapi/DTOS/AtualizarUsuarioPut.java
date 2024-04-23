package gogood.gogoodapi.DTOS;

import gogood.gogoodapi.enums.GeneroEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record AtualizarUsuarioPut(
        @Size(min = 4, max = 255)
        String nome,
        @Email
        String email,
        @Size(min = 6, max = 16)
        String senha,
        GeneroEnum genero,
        @Past
        Date dt_Nascimento
) {
}
