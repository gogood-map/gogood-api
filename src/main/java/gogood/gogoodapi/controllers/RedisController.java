package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import gogood.gogoodapi.repository.MapRepository;
import gogood.gogoodapi.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@EnableCaching
@RequestMapping("/consultar")
public class RedisController {

    @Autowired
    RedisService redisService;

    @Autowired
    private ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    @GetMapping
    public Mono<ResponseEntity<String>> getDadosOcorrencias() {
        return Mono.fromCallable(() -> {
            List<MapList> dados = redisService.get(reactiveRedisTemplate);
            return ResponseEntity.ok("Ok");
        }).onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro"));
    }

    @GetMapping("/local/{latitude}/{longitude}")
    public Mono<ResponseEntity<MapList>> location(@PathVariable Double latitude, @PathVariable Double longitude) {
        return redisService.getLocationCached(latitude, longitude)
                .map(resultado -> ResponseEntity.ok().body(resultado))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

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