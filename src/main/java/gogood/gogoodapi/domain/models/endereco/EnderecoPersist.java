package gogood.gogoodapi.domain.models.endereco;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.util.Date;

@Data
public class EnderecoPersist {
    @Max(9)
    private String cep;
    private String rua;
    private String bairro;
    private Integer numero;
}
