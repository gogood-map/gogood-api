package gogood.gogoodapi.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class RotaShareResponse {
    private Map<String, String> url;
}
