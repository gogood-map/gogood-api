package gogood.gogoodapi.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisTTL {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public RedisTTL(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setKeyWithExpire(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }
}
