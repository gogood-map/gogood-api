package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.MapData;
import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.config.JdbcConfig;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@EnableCaching
@RequestMapping("/consultar")
public class RedisController {

    public final List<MapList> partes = Collections.synchronizedList(new ArrayList<>());

    private final AtomicInteger counter = new AtomicInteger(1);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    JdbcConfig jdbcConfig = new JdbcConfig();
//
//    private final R2dbcEntityTemplate r2dbcEntityTemplate;
//
//    public RedisController(R2dbcEntityTemplate r2dbcEntityTemplate) {
//        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
//    }

    @GetMapping
    public Mono<ResponseEntity<String>> getDadosOcorrencias() {
        get();
        return Mono.just(ResponseEntity.ok().body("Dados salvos com sucesso"));
    }


    public void saveList(List<Map<String, Object>> mapData) {
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


    public void get() {
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
    }


    public void salvarListaNoRedis() {
        for (MapList item : partes) {
            String chave = "parte:" + item.getId();
            redisTemplate.opsForValue().set(chave, item);
        }
    }

//    @GetMapping("/local/{latitude}/{longitude}")
//    public Mono<ResponseEntity<MapList>> location(@PathVariable Double latitude, @PathVariable Double longitude) {
//        return getLocationCached(latitude, longitude)
//                .map(resultado -> ResponseEntity.ok().body(resultado))
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
//
//    @Cacheable(value = "localizacao", key = "#latitude.toString().concat('-').concat(#longitude.toString())")
//    @Transactional(readOnly = true)
//    public Mono<MapList> getLocationCached(Double latitude, Double longitude) {
//        return Mono.fromCallable(() -> getByLocation(latitude, longitude));
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<MapList> resultadoRedis(@PathVariable String id) {
//        try {
//            MapList resultado = redisService.getCachedMapList(id, mapRepository);
//            return ResponseEntity.ok().body(resultado);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @Cacheable(value = "cacheLista", key = "#chave")
    @GetMapping("/{chave}")
    public Mono<MapList> recuperarValorPelaChave(@PathVariable String chave) {
        return reactiveRedisTemplate.opsForValue().get(chave)
                .flatMap(lista -> Mono.just(GenericConverter.convert(lista, MapList.class)))
                .switchIfEmpty(Mono.error(new RuntimeException("Não existe lista procurada no Redis")));
    }


}