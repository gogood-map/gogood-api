package gogood.gogoodapi.domain.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioNovoGoogle {
    @Size(min = 4, max = 255)
    @NotBlank
    @NotNull
    private String nome;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @NotNull
    private String google_id;
}
