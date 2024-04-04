//package gogood.gogoodapi.services;
//
//import gogood.gogoodapi.models.MapData;
//import gogood.gogoodapi.models.MapList;
//import org.springframework.cache.annotation.Cacheable;
//import org.springframework.data.redis.core.ReactiveRedisTemplate;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import reactor.core.publisher.Mono;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class RedisService {
//
//
//    private ReactiveRedisTemplate redisTemplate;
//
//
//
////    public MapList getByLocation(Double latitude, Double longitude) {
////        List<MapData> resultado = jdbcConfig.getConexaoDoBanco().query("""
////                SELECT *, (6371 * acos(
////                        cos(radians(?)) * cos(radians(LATITUDE)) * cos(radians(LONGITUDE) - radians(?)) +
////                        sin(radians(?)) * sin(radians(LATITUDE))
////                    )) AS distancia
////                FROM ocorrencias
////                HAVING distancia <= 2;
////                """, new Object[]{latitude, longitude, latitude}, new BeanPropertyRowMapper<>(MapData.class));
////
////        MapList mapList = new MapList();
////        List<Map<String, Object>> mapData = new ArrayList<>();
////
////        for (MapData mapa : resultado) {
////            Map<String, Object> map = new HashMap<>();
////            map.put("longitude", mapa.getLongitude());
////            map.put("latitude", mapa.getLatitude());
////            map.put("id", mapa.getId());
////            mapData.add(map);
////        }
////
////        mapList.setMapData(mapData);
////        mapList.setId("listaLocalizacao");
//////        mapRepository.save(mapList);
////
////        return mapList;
////    }
//
//
//
////    public Ocorrencias getById(Integer id, GoGoodRepository goGoodRepository) {
////        Optional<Ocorrencias> resultado = goGoodRepository.findById(id);
////        if (resultado.isPresent()) {
////            return GenericConverter.convert(resultado.get(), Ocorrencias.class);
////        } else {
////            throw new RuntimeException("Não existe lista procurada no Redis no ID: " + id);
////        }
////    }
////    @Cacheable(value = "listaEmCache", key = "#id")
////    @Transactional(readOnly = true)
////    public Ocorrencias getCachedMapList(Integer id, GoGoodRepository goGoodRepository) {
////        return getById(id, goGoodRepository);
////    }
//}
