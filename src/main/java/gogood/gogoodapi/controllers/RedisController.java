package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.MapData;
import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableCaching
//@EnableScheduling
@RequestMapping("/consultar")
public class RedisController {

    public final List<MapList> partes = new ArrayList<>();

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

//    JdbcConfig jdbcConfig = new JdbcConfig();

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public RedisController(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @GetMapping
    public Mono<ResponseEntity<String>> get() {
        return getDadosOcorrencia()
                .then(Mono.just(ResponseEntity.ok().body("Dados salvos com sucesso")))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body("Erro ao processar a requisição: " + e.getMessage())));
    }

    @Cacheable(value = "cacheLocalizacao", key = "#latitude.toString().concat('-').concat(#longitude.toString())")
    @GetMapping("/local/{latitude}/{longitude}")
    public Mono<MapList> getLocation(@PathVariable Double latitude, @PathVariable Double longitude) {
        return getAndSaveByLocation(latitude, longitude)
                .then(reactiveRedisTemplate.opsForValue().get("localizacao:" + latitude.toString()))
                .flatMap(lista -> Mono.just(GenericConverter.convert(lista, MapList.class)))
                .switchIfEmpty(Mono.error(new RuntimeException("Não existe lista procurada no Redis")))
                .onErrorResume(e -> Mono.error(new Exception("Erro ao recuperar localização: " + e.getMessage())));
    }


    @Cacheable(value = "cacheLista", key = "#chave")
    @GetMapping("/{chave}")
    public Mono<MapList> recuperarValorPelaChave(@PathVariable String chave) {

        return reactiveRedisTemplate.opsForValue().get(chave)
                .flatMap(lista -> Mono.just(GenericConverter.convert(lista, MapList.class)))
                .switchIfEmpty(Mono.error(new RuntimeException("Não existe lista procurada no Redis")))
                .onErrorResume(e -> Mono.error(new Exception("Erro ao recuperar valor pela chave: " + e.getMessage())));
    }

    public void salvarListaPorPartes(List<Map<String, Object>> mapData) {
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
        salvarPartesNoRedis();
    }

    //    @Scheduled(fixedRate = 43200000)
    public Mono<Void> getDadosOcorrencia() {
        String query = "SELECT * FROM ocorrencias";
        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .fetch()
                .all()
                .collectList()
                .doOnNext(this::salvarListaPorPartes)
                .then();
    }

    public Mono<MapList> getAndSaveByLocation(Double latitude, Double longitude) {
        String query = "SELECT *, (6371 * acos(cos(radians(?)) * cos(radians(LATITUDE)) * cos(radians(LONGITUDE) - radians(?)) + sin(radians(?)) * sin(radians(LATITUDE)))) AS distancia FROM ocorrencias HAVING distancia <= 2;";

        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .bind(0, latitude)
                .bind(1, longitude)
                .bind(2, latitude)
                .map((row, metadata) -> new MapData(
                        String.valueOf(row.get("id", Integer.class)),  // Changed from String.class to Integer.class
                        row.get("latitude", Double.class),
                        row.get("longitude", Double.class)))
                .all()
                .collectList()
                .map(list -> {
                    MapList mapList = new MapList();
                    List<Map<String, Object>> mapData = new ArrayList<>();
                    String id = "";

                    for (MapData mapa : list) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("longitude", mapa.getLongitude());
                        map.put("latitude", mapa.getLatitude());
                        map.put("id", mapa.getId());  // Convert Integer to String
                        mapData.add(map);
                    }

                    mapList.setMapData(mapData);
                    mapList.setId("listaLocalizacao");
                    String chave = "localizacao:" + latitude;
                    redisTemplate.opsForValue().set(chave, mapList);
                    return mapList;
                });
    }

    public void salvarPartesNoRedis() {
        for (MapList item : partes) {
            String chave = "parte:" + item.getId();
            redisTemplate.opsForValue().set(chave, item);
        }
        partes.clear();
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<MapList> resultadoRedis(@PathVariable String id) {
//        try {
//            MapList resultado = getCachedMapList(id, mapRepository);
//            return ResponseEntity.ok().body(resultado);
//        } catch (RuntimeException e) {
//            return ResponseEntity.notFound().build();
//        }
//    }


}