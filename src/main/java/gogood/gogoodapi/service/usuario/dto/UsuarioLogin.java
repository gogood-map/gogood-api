package gogood.gogoodapi.service.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioLogin {
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String senha;

}
