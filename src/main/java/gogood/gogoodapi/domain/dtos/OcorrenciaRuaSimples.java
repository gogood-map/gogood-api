package gogood.gogoodapi.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OcorrenciaRuaSimples {
    final private String rua;
    final private Integer quantidadeOcorrencias;
}
