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

    public Map<String, Object> getAndSaveByLocation(Double latitude, Double longitude) {
        Point localizacao = new Point(longitude, latitude);
        Distance distancia = new Distance(5, Metrics.KILOMETERS);
        Map<String, Object> response = new HashMap<>();
        List<Ocorrencia> ocorrencias = mapRepository.findByLocalizacaoNear(localizacao, distancia);
        response.put("qtdOcorrencias", ocorrencias.size());
        response.put("ocorrencias", ocorrencias);

        return response;
    }
    public Map<String, Object> getDadosOcorrencia() {
        Map<String, Object> response = new HashMap<>();
        List<Ocorrencia> ocorrencias = mapRepository.findAll();
        response.put("qtdOcorrencias", ocorrencias.size());
        response.put("ocorrencias", ocorrencias);

        return response;
    }
}
