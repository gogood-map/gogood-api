package gogood.gogoodapi.models.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;


public abstract class RedisBytesAbstract<T> implements Converter<byte[], T> {
    protected ObjectMapper objectMapper;

    Jackson2JsonRedisSerializer<T> jackson2JsonRedisSerializer;

    protected RedisBytesAbstract(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<T>(getType());
    }

    public abstract Class<T> getType();

    @Override
    public T convert(byte @NotNull [] source) {
        return jackson2JsonRedisSerializer.deserialize(source);
    }

    @Override
    public <U> @NotNull Converter<byte[], U> andThen(Converter<? super T, ? extends U> after) {
        return Converter.super.andThen(after);
    }
}