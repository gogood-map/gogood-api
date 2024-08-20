package gogood.gogoodapi.domain.models.rotas;

import gogood.gogoodapi.domain.DTOS.RotaSharePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RotaShare {
    private String id;
    private RotaSharePersist rotaShare;
}
