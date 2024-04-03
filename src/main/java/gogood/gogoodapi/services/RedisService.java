package gogood.gogoodapi.services;

import gogood.gogoodapi.models.MapData;
import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.Ocorrencias;
import gogood.gogoodapi.models.config.JdbcConfig;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import gogood.gogoodapi.repository.GoGoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
public class RedisService {
    JdbcConfig jdbcConfig = new JdbcConfig();
    List<MapList> partes = new ArrayList<>();

    private ReactiveRedisTemplate redisTemplate;

    @Cacheable(value = "localizacao", key = "#latitude.toString().concat('-').concat(#longitude.toString())")
    @Transactional(readOnly = true)
    public Mono<MapList> getLocationCached(Double latitude, Double longitude) {
        return Mono.fromCallable(() -> getByLocation(latitude, longitude));
    }

    public void salvarListaNoRedis() {
        for (MapList item : partes) {
            String chave = "parte:" + item.getId();
            redisTemplate.opsForValue().set(chave, item);
        }
    }
    public List<MapList> get(ReactiveRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        List<MapData> resultado = jdbcConfig.getConexaoDoBanco().query("""
                SELECT * FROM ocorrencias
                 """, new BeanPropertyRowMapper<>(MapData.class));

        MapList mapList = new MapList();
        List<Map<String, Object>> mapData = new ArrayList<>();

        for (MapData mapa : resultado) {
            Map<String, Object> map = new HashMap<>();
            map.put("longitude", mapa.getLongitude());
            map.put("latitude", mapa.getLatitude());
            map.put("id", mapa.getId());
            mapData.add(map);
        }

        saveList(mapData);
        return partes;
    }

    public MapList getByLocation(Double latitude, Double longitude) {
        List<MapData> resultado = jdbcConfig.getConexaoDoBanco().query("""
                SELECT *, (6371 * acos(
                        cos(radians(?)) * cos(radians(LATITUDE)) * cos(radians(LONGITUDE) - radians(?)) +
                        sin(radians(?)) * sin(radians(LATITUDE))
                    )) AS distancia
                FROM ocorrencias
                HAVING distancia <= 2;
                """, new Object[]{latitude, longitude, latitude}, new BeanPropertyRowMapper<>(MapData.class));

        MapList mapList = new MapList();
        List<Map<String, Object>> mapData = new ArrayList<>();

        for (MapData mapa : resultado) {
            Map<String, Object> map = new HashMap<>();
            map.put("longitude", mapa.getLongitude());
            map.put("latitude", mapa.getLatitude());
            map.put("id", mapa.getId());
            mapData.add(map);
        }

        mapList.setMapData(mapData);
        mapList.setId("listaLocalizacao");
//        mapRepository.save(mapList);

        return mapList;
    }

    public void saveList(List<Map<String, Object>> mapData){
        int totalPartes = (mapData.size() + 9999) / 10000;

        for (int parte = 0; parte < totalPartes; parte++) {
            int start = parte * 10000;
            int end = Math.min(start + 10000, mapData.size());

            List<Map<String, Object>> subList = mapData.subList(start, end);

            MapList mapList = new MapList();
            mapList.setMapData(subList);
            mapList.setId("lista" + (parte + 1));
            partes.add(mapList);
        }

        salvarListaNoRedis();
    }

//    public Ocorrencias getById(Integer id, GoGoodRepository goGoodRepository) {
//        Optional<Ocorrencias> resultado = goGoodRepository.findById(id);
//        if (resultado.isPresent()) {
//            return GenericConverter.convert(resultado.get(), Ocorrencias.class);
//        } else {
//            throw new RuntimeException("Não existe lista procurada no Redis no ID: " + id);
//        }
//    }
//    @Cacheable(value = "listaEmCache", key = "#id")
//    @Transactional(readOnly = true)
//    public Ocorrencias getCachedMapList(Integer id, GoGoodRepository goGoodRepository) {
//        return getById(id, goGoodRepository);
//    }
}
