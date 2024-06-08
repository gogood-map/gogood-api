package gogood.gogoodapi.domain.DTOS;

import lombok.Data;

@Data
public class EnderecoUsuarioDTO {
    private String cep;
    private String rua;
    private String cidade;
    private Integer numero;
    private String bairro;
    private Integer idUsuario;
    private String tipoEndereco;
    private Integer usuarioId;

    // Getters and Setters
}
