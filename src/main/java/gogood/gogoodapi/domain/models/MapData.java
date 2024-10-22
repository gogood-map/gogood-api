package gogood.gogoodapi.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class MapData implements Serializable {
    @Serial
    private static final long serialVersionUID = 8652117075734920962L;
    private Double latitude;
    private Double longitude;

    public MapData() {
    }
}
