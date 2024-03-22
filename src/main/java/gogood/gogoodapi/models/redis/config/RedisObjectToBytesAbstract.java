package gogood.gogoodapi.models.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;


@WritingConverter
public class RedisObjectToBytesAbstract<T> implements Converter<T, byte[]>{
    protected ObjectMapper objectMapper;
    Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<T>(getType());

    protected RedisObjectToBytesAbstract(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] convert(@NotNull T source) {
        return jackson2JsonRedisSerializer.serialize(source);
    }

    @Override
    public <U> @NotNull Converter<T, U> andThen(Converter<? super byte[], ? extends U> after) {
        return Converter.super.andThen(after);
    }

    public Class<T> getType() {
        return null;
    }
}
