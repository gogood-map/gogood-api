package gogood.gogoodapi.models.redis.config;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;

import java.lang.reflect.Type;
import java.util.List;

@Slf4j
public class GenericConverter {
    private GenericConverter(){}

    public static <E, T> E convert( T source, Class<E> typeDestination){
        return convert(source, typeDestination, MatchingStrategies.STRICT);
    }

    public static <E, T> E convert( T source, Class<E> typeDestination, MatchingStrategy strategy){
        E model = null;
        if(source != null && typeDestination != null){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(strategy);
            model = modelMapper.map(source, typeDestination);

        }
        return model;
    }

    public static <E, T> List<E> convert(List<T> source, Type typeDestination, MatchingStrategy strategy){
        List<E> model = null;

        if(source != null && typeDestination != null){
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(strategy);
            model = modelMapper.map(source, typeDestination);

        }
        return model;
    }
}
