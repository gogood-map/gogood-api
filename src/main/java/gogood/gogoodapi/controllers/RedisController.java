package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.config.JdbcConfig;
import gogood.gogoodapi.models.MapData;
import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import gogood.gogoodapi.repository.MapRepository;
import gogood.gogoodapi.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/consultar")
public class RedisController {

    @Autowired
    MapRepository mapRepository;

    @GetMapping
    public ResponseEntity<String> resultado() {
        return new RedisService().post(mapRepository);
    }

    @GetMapping("/local/{latitude}/{longitude}")
    public ResponseEntity<MapList> location(@PathVariable Double latitude, @PathVariable Double longitude) {
        return new RedisService().getByLocation(mapRepository, latitude, longitude);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MapList> resultadoRedis(@PathVariable String id) {
        try {
            MapList resultado = getById(id, mapRepository);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }

    public MapList getById(String id, MapRepository mapRepository) {
        return new RedisService().getById(id , mapRepository);
    }
}