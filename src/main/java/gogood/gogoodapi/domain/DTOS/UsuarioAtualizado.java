package gogood.gogoodapi.domain.DTOS;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Date;

public record UsuarioAtualizado(
        @NotNull
        @NotBlank
        @Size(min = 4, max = 255)
        String nome,
        @Email
        @NotNull
        String email,
        @NotNull
        @NotBlank
        String genero,
        @NotNull
        @Past
        LocalDate dt_Nascimento
) {
}