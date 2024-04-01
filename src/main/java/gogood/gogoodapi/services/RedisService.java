package gogood.gogoodapi.services;

import gogood.gogoodapi.models.MapData;
import gogood.gogoodapi.models.MapList;
import gogood.gogoodapi.models.config.JdbcConfig;
import gogood.gogoodapi.models.redis.config.GenericConverter;
import gogood.gogoodapi.repository.MapRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RedisService {
    JdbcConfig jdbcConfig = new JdbcConfig();


    public ResponseEntity<String> post(MapRepository mapRepository) {

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

        saveList(mapData, mapList, mapRepository);
        return ResponseEntity.ok().body("Ok");
    }

    public ResponseEntity<MapList> getByLocation(MapRepository mapRepository, Double latitude, Double longitude) {
        List<MapData> resultado = jdbcConfig.getConexaoDoBanco().query("""
                SELECT *, (6371 * acos(
                        cos(radians(?)) * cos(radians(LATITUDE)) * cos(radians(LONGITUDE) - radians(?)) +
                        sin(radians(?)) * sin(radians(LATITUDE))
                    )) AS distancia
                FROM ocorrencias
                HAVING distancia <= 2;
                """, new Object[]{latitude, longitude, latitude}, new BeanPropertyRowMapper<>(MapData.class));

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
//        mapRepository.save(mapList);

        return ResponseEntity.ok().body(mapList);
    }

    public void saveList(List<Map<String, Object>> mapData, MapList mapList, MapRepository mapRepository){
        int totalPartes = (mapData.size() + 9999) / 10000;

        for (int parte = 0; parte < totalPartes; parte++) {
            int start = parte * 10000;
            int end = Math.min(start + 10000, mapData.size());

            List<Map<String, Object>> subList = mapData.subList(start, end);

            mapList.setMapData(subList);
            mapList.setId("lista" + (parte + 1));

            mapRepository.save(mapList);
        }
    }

    public MapList getById(String id, MapRepository mapRepository) {
        Optional<MapList> resultado = mapRepository.findById(id);
        if (resultado.isPresent()) {
            return GenericConverter.convert(resultado.get(), MapList.class);
        } else {
            throw new RuntimeException("Não existe lista procurada no Redis no ID: " + id);
        }
    }

}
