package gogood.gogoodapi.configuration.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gogood.gogoodapi.domain.models.MapList;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class RedisMapListToBytesConverter extends RedisObjectToBytesAbstract<MapList>{
    public RedisMapListToBytesConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Class<MapList> getType() {
        return MapList.class;
    }


}
