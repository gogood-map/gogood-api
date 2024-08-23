package gogood.gogoodapi.configuration.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class GenericKeySerializer implements RedisSerializer<Object> {

    @Override
    public byte[] serialize(Object object) throws SerializationException {
        if (object == null) {
            return new byte[0];
        }
        // Convertendo o objeto para uma string e, em seguida, para um array de bytes
        return object.toString().getBytes();
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        // Deserialização não é usada para chaves, então pode retornar null ou lançar uma exceção
        return null;
    }
}
