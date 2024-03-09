package gogood.gogoodapi.DTOS;

import gogood.gogoodapi.enums.GeneroEnum;

import java.util.Date;

public record AtualizarUsuarioPut(
        String nome,
        String email,
        String senha,
        GeneroEnum genero,
        Date dt_Nascimento
) {
}
