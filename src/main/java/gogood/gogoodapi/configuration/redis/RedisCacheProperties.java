package gogood.gogoodapi.configuration.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.cache.redis")
public class RedisCacheProperties {
    private Map<String, Long> ttl;

}
