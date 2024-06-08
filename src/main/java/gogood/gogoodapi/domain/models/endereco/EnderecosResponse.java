package gogood.gogoodapi.domain.models.endereco;


import com.fasterxml.jackson.annotation.JsonBackReference;
import gogood.gogoodapi.domain.models.Usuarios;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class EnderecosResponse {
    private Enderecos enderecos;

    private String tipoEndereco;
}
