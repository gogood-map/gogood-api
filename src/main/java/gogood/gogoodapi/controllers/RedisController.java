package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.Ocorrencias;
import gogood.gogoodapi.repository.GoGoodRepository;
import gogood.gogoodapi.repository.MapRepository;
import gogood.gogoodapi.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableCaching
@RequestMapping("/consultar")
public class RedisController {

    @Autowired
    GoGoodRepository goGoodRepository;

    @Autowired
    RedisService redisService;

//    @GetMapping
//    public ResponseEntity<MapList> resultado() {
//        MapList resultado = redisService.post();
//        return ResponseEntity.ok().body(resultado);
//    }

    @GetMapping("/local/{latitude}/{longitude}")
    public ResponseEntity<MapList> location(@PathVariable Double latitude, @PathVariable Double longitude) {
        try {
            MapList resultado = redisService.getLocationCached(latitude, longitude);
            return ResponseEntity.ok().body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ocorrencias> resultadoRedis(@PathVariable Integer id) {
        try {
            Ocorrencias resultado = redisService.getCachedMapList(id, goGoodRepository);
            return ResponseEntity.ok().body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


}