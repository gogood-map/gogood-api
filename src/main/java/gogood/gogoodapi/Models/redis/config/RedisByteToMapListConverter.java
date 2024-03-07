package gogood.gogoodapi.Models.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gogood.gogoodapi.Models.MapList;

public class RedisByteToMapListConverter extends RedisBytesAbstract<MapList> {
    public RedisByteToMapListConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Class<MapList> getType() {
        return MapList.class;
    }
}
