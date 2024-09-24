package gogood.gogoodapi.service;

import gogood.gogoodapi.configuration.JdbcConfig;
import gogood.gogoodapi.domain.models.MapData;
import gogood.gogoodapi.domain.models.Ocorrencia;
import gogood.gogoodapi.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MapService {
    @Autowired
    private MapRepository mapRepository;

    public List<Ocorrencia> getAndSaveByLocation(Double latitude, Double longitude) {
        Point localizacao = new Point(longitude, latitude);
        Distance distancia = new Distance(2, Metrics.KILOMETERS);


//        MapList mapList = new MapList();
//        List<Map<String, Object>> mapData = new ArrayList<>();
//
//        for (MapData mapa : resultado) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("longitude", mapa.getLongitude());
//            map.put("latitude", mapa.getLatitude());
//            map.put("id", mapa.getId());
//            map.put("ano_ocorrencia", mapa.getAno_ocorrencia());
//            mapData.add(map);
//        }
//
//        mapList.setMapData(mapData);
//        mapList.setId("listaLocalizacao");

        return mapRepository.findByLocalizacaoNear(localizacao, distancia);;
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
