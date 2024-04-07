package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public Mono<ResponseEntity<String>> getDadosOcorrencias() {
        return get()
                .then(Mono.just(ResponseEntity.ok().body("Dados salvos com sucesso")));
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
        partes.clear();
    }

//    @Scheduled(fixedRate = 43200000)
    public Mono<Void> get() {
        String query = "SELECT * FROM ocorrencias";
        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .fetch()
                .all()
                .collectList()
                .doOnNext(this::saveList)
                .then();
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
//    public Mono<MapList> getLocationCached(Double latitude, Double longitude) {
//        return Mono.fromCallable(() -> getByLocation(latitude, longitude));
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<MapList> resultadoRedis(@PathVariable String id) {
//        try {
//            MapList resultado = getCachedMapList(id, mapRepository);
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