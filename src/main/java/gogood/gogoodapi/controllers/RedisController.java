package gogood.gogoodapi.controllers;

import gogood.gogoodapi.models.Ocorrencia;
import gogood.gogoodapi.models.config.JdbcConfig;
import gogood.gogoodapi.models.MapData;
import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import gogood.gogoodapi.repository.GoGoodRepository;
import gogood.gogoodapi.repository.MapRepository;
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

    @Autowired
    GoGoodRepository goGoodRepository;

    JdbcConfig jdbcConfig = new JdbcConfig();

    @GetMapping
    public ResponseEntity<String> resultado() {
        List<Ocorrencia> resultado = (List<Ocorrencia>) goGoodRepository.findAll();

        MapList mapList = new MapList();
        List<Map<String, Object>> mapData = new ArrayList<>();

        for (Ocorrencia mapa : resultado) {
            Map<String, Object> map = new HashMap<>();
            map.put("longitude", mapa.getLongitude());
            map.put("latitude", mapa.getLatitude());
            map.put("id", mapa.getId());
            mapData.add(map);
        }

        mapList.setMapData(mapData);
        mapList.setId("lista");

        mapRepository.save(mapList);

        return ResponseEntity.ok().body("Ok");
    }

    @GetMapping("/{id}")
    public ResponseEntity<MapList> resultadoRedis(@PathVariable String id) {
        try {
            MapList resultado = getById(id);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }

    public MapList getById(String id) {
        Optional<MapList> resultado = mapRepository.findById(id);
        if (resultado.isPresent()) {
            return GenericConverter.convert(resultado.get(), MapList.class);
        } else {
            throw new RuntimeException("Não existe lista procurada no Redis no ID: " + id);
        }
    }
}
