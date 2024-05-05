package gogood.gogoodapi.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MapData implements Serializable {
    private String id;
    private Double latitude;
    private Double longitude;

    public MapData() {
    }

}
