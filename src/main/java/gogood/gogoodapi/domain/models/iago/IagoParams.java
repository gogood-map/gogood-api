package gogood.gogoodapi.domain.models.iago;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IagoParams {
    private Integer limit;
    private Integer page;
}
