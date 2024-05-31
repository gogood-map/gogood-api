package gogood.gogoodapi.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InfoLogradouro {
    private String rua;
    private String bairro;
    private String cidade;
}
