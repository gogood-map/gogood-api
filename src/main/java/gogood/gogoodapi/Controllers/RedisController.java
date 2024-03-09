package gogood.gogoodapi.Controllers;

import gogood.gogoodapi.Models.config.JdbcConfig;
import gogood.gogoodapi.Models.MapData;
import gogood.gogoodapi.Models.MapList;
import gogood.gogoodapi.Models.redis.config.GenericConverter;
import gogood.gogoodapi.Repository.MapRepository;
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

    JdbcConfig jdbcConfig = new JdbcConfig();

    @GetMapping
    public ResponseEntity<String> resultado() {
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
