package gogood.gogoodapi.models.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gogood.gogoodapi.models.MapList;

public class RedisByteToMapListConverter extends RedisBytesAbstract<MapList> {
    public RedisByteToMapListConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Class<MapList> getType() {
        return MapList.class;
    }
}
