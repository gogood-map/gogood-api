package gogood.gogoodapi.domain.DTOS;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RotaSharePersist {
    private String origem;
    private String destino;
    private String tipoTransporte;
}
