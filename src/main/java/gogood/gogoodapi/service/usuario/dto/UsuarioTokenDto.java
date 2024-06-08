package gogood.gogoodapi.service.usuario.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioTokenDto {
    private Integer userId;
    private String nome;
    private String email;
    private String token;
    private String genero;
    private LocalDate dt_nascimento;
}
