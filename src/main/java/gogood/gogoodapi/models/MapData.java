package gogood.gogoodapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class MapData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private Double latitude;
    private Double longitude;

}
