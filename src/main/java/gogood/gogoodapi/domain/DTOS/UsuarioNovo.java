package gogood.gogoodapi.domain.DTOS;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Date;

public record UsuarioNovo(
        @Size(min = 4, max = 255)
        @NotBlank
        String nome,
        @Email
        @NotBlank
        String email,
        @Size(min = 6, max = 16)
        @NotNull
        @NotBlank
        String senha,
        @NotBlank
        String genero,
        @Past
        @NotNull
        LocalDate dt_Nascimento,
        LocalDate created_at){
}