package gogood.gogoodapi.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@RedisHash("DADOS_MAPA")
public class MapList implements Serializable {
    @Id
    private String id;
    private List<Map<String, Object>> mapData;
}
