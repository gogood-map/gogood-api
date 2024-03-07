package gogood.gogoodapi.Models.DTOS;

import gogood.gogoodapi.Enum.GeneroEnum;

import java.util.Date;

public record AtualizarUsuarioPut(
        String nome,
        String email,
        String senha,
        GeneroEnum genero,
        Date dt_Nascimento
) {
}
