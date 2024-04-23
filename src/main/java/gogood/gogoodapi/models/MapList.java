package gogood.gogoodapi.models;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class MapList implements Serializable {
    @Id
    private String id;
    private List<Map<String, Object>> mapData;
}
