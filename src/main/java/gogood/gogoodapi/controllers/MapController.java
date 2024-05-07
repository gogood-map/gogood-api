package gogood.gogoodapi.controllers;

import gogood.gogoodapi.domain.models.MapData;
import gogood.gogoodapi.domain.models.MapList;
import gogood.gogoodapi.configuration.JdbcConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@EnableCaching
@CrossOrigin(origins = "https://gogood.azurewebsites.net")
@RequestMapping("/consultar")
@Tag(name = "Mapa", description = "Consultar dados de ocorrências")

public class MapController {
    public final List<MapList> partes = new ArrayList<>();


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;



    @Operation(summary = "Obter dados de ocorrências", description = "Obtém os dados de ocorrências do banco de dados")
    @GetMapping
    public ResponseEntity<String> get() {
        ResponseEntity<String> response = getDadosOcorrencia();
        return response;
    }


    @Operation(summary = "Obter dados de ocorrências por localização", description = "Obtém os dados de ocorrências do banco de dados por localização")
    @GetMapping("/local/{latitude}/{longitude}")
    public MapList getLocation(@PathVariable Double latitude, @PathVariable Double longitude) {
        return getAndSaveByLocation(latitude, longitude);
    }


    public ResponseEntity<String> getDadosOcorrencia() {
        JdbcConfig jdbcConfig = new JdbcConfig();
        List<MapData> resultado = jdbcConfig.getConexaoDoBanco().query("""
                SELECT * FROM ocorrencias
                 """, new BeanPropertyRowMapper<>(MapData.class));

        List<Map<String, Object>> mapData = new ArrayList<>();

        for (MapData mapa : resultado) {
            Map<String, Object> map = new HashMap<>();
            map.put("longitude", mapa.getLongitude());
            map.put("latitude", mapa.getLatitude());
            map.put("id", mapa.getId());
            mapData.add(map);
        }

        salvarListaPorPartes(mapData);
        return ResponseEntity.ok().body("Ok");
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

    public void salvarPartesNoRedis() {
        for (MapList item : partes) {
            String chave = "parte:" + item.getId();

            redisTemplate.opsForValue().set(chave, item);
        }
        partes.clear();
    }


    public MapList getAndSaveByLocation(Double latitude, Double longitude) {
        JdbcConfig jdbcConfig = new JdbcConfig();
        String sql = "SELECT *, (6371 * acos(cos(radians(?)) * cos(radians(LATITUDE)) * cos(radians(LONGITUDE) - radians(?)) + sin(radians(?)) * sin(radians(LATITUDE)))) AS distance FROM ocorrencias HAVING distance <= 2";

        List<MapData> resultado = jdbcConfig.getConexaoDoBanco().query(sql, new Object[]{latitude, longitude, latitude}, new BeanPropertyRowMapper<>(MapData.class));

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
        mapList.setId("listaLocalizacao");

        return mapList;
    }

}
