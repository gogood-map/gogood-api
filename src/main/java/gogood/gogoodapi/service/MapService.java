package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.Ocorrencia;
import gogood.gogoodapi.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Map<String, Object> searchRouteOcorrencias(Double latitude, Double longitude) {
        Point localizacao = new Point(longitude, latitude);
        Distance distancia = new Distance(0.5, Metrics.KILOMETERS);
        Map<String, Object> response = new HashMap<>();
        List<Ocorrencia> ocorrencias = mapRepository.findByLocalizacaoNear(localizacao, distancia);
        List<Map<String, Object>> top5Ocorrencias = getTop5Ocorrencias(ocorrencias);

        response.put("qtdOcorrencias", ocorrencias.size());
        response.put("ocorrencias", ocorrencias);
        response.put("top5Ocorrencias", top5Ocorrencias);

        return response;
    }

    public List<Map<String, Object>> getTop5Ocorrencias(List<Ocorrencia> ocorrencias) {
        List<String> topCrimes = new ArrayList<>();
        for (Ocorrencia ocorrencia : ocorrencias) {
            topCrimes.add(ocorrencia.getCrime());
        }
        Map<String, Integer> contagemCrimes = new HashMap<>();
        for (String crime : topCrimes) {
            contagemCrimes.put(crime, contagemCrimes.getOrDefault(crime, 0) + 1);
        }

        return contagemCrimes.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())) // Ordena por valor (número de ocorrências)
                .limit(5)
                .map(entry -> {
                    Map<String, Object> crimeInfo = new HashMap<>();
                    crimeInfo.put("crime", entry.getKey());
                    crimeInfo.put("qtdOcorrido", entry.getValue());
                    return crimeInfo;
                })
                .collect(Collectors.toList());
    }

}
