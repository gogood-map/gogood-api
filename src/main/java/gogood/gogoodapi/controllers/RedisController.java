package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
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
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public RedisController(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @GetMapping
    public Mono<ResponseEntity<String>> getDadosOcorrencias() {
        return get()
                .then(Mono.just(ResponseEntity.ok().body("Dados salvos com sucesso")));
    }


    public Mono<Void> saveListReactive(List<Map<String, Object>> mapData) {
        int batchSize = 5000;
        return Flux.fromIterable(mapData)
                .buffer(batchSize)
                .flatMap(batch -> {
                    MapList mapList = new MapList();
                    mapList.setMapData(batch);
                    String id = "lista" + counter.getAndIncrement();
                    mapList.setId(id);
                    return salvarListaNoRedis(Collections.singletonList(mapList));
                }).then();
    }


    public Mono<Void> get() {
        String query = "SELECT * FROM ocorrencias";
        return r2dbcEntityTemplate.getDatabaseClient().sql(query)
                .map((row, metadata) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("longitude", row.get("longitude", Double.class));
                    map.put("latitude", row.get("latitude", Double.class));
                    map.put("id", row.get("id", Integer.class));
                    return map;
                })
                .all()
                .buffer(5000)
                .flatMap(this::saveListReactive)
                .then();
    }


    public Mono<Void> salvarListaNoRedis(List<MapList> partes) {
        return Flux.fromIterable(partes)
                .flatMap(item -> {
                    String chave = "parte:" + item.getId();
                    return reactiveRedisTemplate.opsForValue().set(chave, item)
                            .thenReturn(item)
                            .doOnSuccess(i -> System.out.println("Salvo no Redis: " + chave));
                })
                .then();
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