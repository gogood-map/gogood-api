package gogood.gogoodapi.models.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import gogood.gogoodapi.models.redis.config.RedisByteToMapListConverter;
import gogood.gogoodapi.models.redis.config.RedisMapListToBytesConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

@Configuration
public class RedisConfiguration {

    @Bean
    public RedisCustomConversions redisCustomConversions(ObjectMapper objectMapper) {
        return new RedisCustomConversions(Arrays.asList(
                new RedisMapListToBytesConverter(objectMapper),
                new RedisByteToMapListConverter(objectMapper)
        ));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory, RedisCustomConversions redisCustomConversions) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
