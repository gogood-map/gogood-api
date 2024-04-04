package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableCaching
@RequestMapping("/consultar")
public class RedisController {


    public final List<MapList> partes = new ArrayList<>();

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public RedisController(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @GetMapping
    public ResponseEntity<String> getDadosOcorrencias() {
        get();
        return ResponseEntity.ok("Ok");
    }

    public Mono<Void> saveListReactive(List<Map<String, Object>> mapData) {
        int batchSize = 10000;
        return Flux.fromIterable(mapData)
                .buffer(batchSize) // Agrupa os mapas em lotes de 10000
                .index() // Fornece um índice para cada lote
                .flatMap(tuple -> {
                    List<Map<String, Object>> batch = tuple.getT2();
                    String batchId = "lista" + (tuple.getT1() + 1);
                    // Transforma o lote em um objeto MapList ou similar
                    MapList mapList = new MapList();
                    mapList.setMapData(batch);
                    mapList.setId(batchId);
                    synchronized (partes) {
                        partes.add(mapList); // Adiciona à lista de forma sincronizada
                    }
                    // Salvar no Redis ou outro armazenamento de forma reativa
                    return salvarListaNoRedis(partes);
                })
                .then(); // Conclui a cadeia retornando um Mono<Void> quando todas as gravações estiverem completas
    }


    public Mono<Void> get() {
        Flux<Map<String, Object>> mapDataFlux = r2dbcEntityTemplate.getDatabaseClient().sql("SELECT * FROM ocorrencias")
                .map((row, metadata) -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", row.get("id", Integer.class));
                    map.put("latitude", row.get("latitude", Double.class));
                    map.put("longitude", row.get("longitude", Double.class));
                    return map;
                }).all();

        return mapDataFlux.collectList()
                .flatMap(this::saveListReactive)
                .doOnError(e -> System.out.println("Erro durante a execução: " + e.getMessage()));

    }


    public Mono<Void> salvarListaNoRedis(List<MapList> partes) {
        return Flux.fromIterable(partes)
                .flatMap(item -> {
                    String chave = "parte:" + item.getId();
                    return reactiveRedisTemplate.opsForValue().set(chave, item);
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