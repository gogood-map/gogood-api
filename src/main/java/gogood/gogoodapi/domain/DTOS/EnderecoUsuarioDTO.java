package gogood.gogoodapi.domain.DTOS;

import lombok.Data;

@Data
public class EnderecoUsuarioDTO {
    private String cep;
    private String rua;
    private Integer numero;
    private String bairro;
    private Integer idUsuario;
    private String tipoEndereco;

    // Getters and Setters
}
