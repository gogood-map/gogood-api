package gogood.gogoodapi.DTOS;

import gogood.gogoodapi.enums.GeneroEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.util.Date;

public record CriarUsuario(
        @Size(min = 4, max = 255)
        @NotBlank
        String nome,
        @Email
        @NotBlank
        String email,
        @Size(min = 6, max = 16)
        @NotBlank
        String senha,
        @NotBlank
        String genero,
        @Past
        @NotBlank
        Date dt_Nascimento,

        @NotBlank
        String google_id){}

