package gogood.gogoodapi.configuration.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthCheck {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    public boolean isRedisUp() {
        try {
            redisConnectionFactory.getConnection().ping();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
