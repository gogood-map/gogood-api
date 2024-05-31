package gogood.gogoodapi.domain.models.endereco;


import jakarta.persistence.*;
import lombok.Data;

@Data
public class EnderecosPersist {
    @Column(name = "id_usuario")
    private int idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_endereco")
    private Enderecos enderecos;

    @Column(name = "tipo_endereco", length = 63)
    private String tipoEndereco;
}
