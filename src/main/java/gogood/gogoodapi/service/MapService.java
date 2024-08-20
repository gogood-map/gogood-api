package gogood.gogoodapi.service;

import gogood.gogoodapi.configuration.JdbcConfig;
import gogood.gogoodapi.domain.models.MapData;
import gogood.gogoodapi.domain.models.MapList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapService {
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
            map.put("ano_ocorrencia", mapa.getAno_ocorrencia());
            mapData.add(map);
        }

        mapList.setMapData(mapData);
        mapList.setId("listaLocalizacao");

        return mapList;
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

        return ResponseEntity.ok().body("Ok");
    }
}
