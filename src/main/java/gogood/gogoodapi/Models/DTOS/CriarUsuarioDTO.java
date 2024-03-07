package gogood.gogoodapi.Models.DTOS;

import gogood.gogoodapi.Enum.GeneroEnum;

import java.util.Date;

public record CriarUsuarioDTO(
        String nome,
        String email,
        String senha,
        GeneroEnum genero,
        String dt_Nascimento){}
