package gogood.gogoodapi.Models.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import gogood.gogoodapi.Models.redis.config.RedisByteToMapListConverter;
import gogood.gogoodapi.Models.redis.config.RedisMapListToBytesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.convert.RedisCustomConversions;

import java.util.List;

@Configuration
public class RedisCustomConversionConfig {

    @Bean
    RedisCustomConversions customRedisConversions(ObjectMapper objectMapper) {
        return new RedisCustomConversions(List.of(
                new RedisMapListToBytesConverter(objectMapper),
                new RedisByteToMapListConverter(objectMapper)
        ));
    }
}
