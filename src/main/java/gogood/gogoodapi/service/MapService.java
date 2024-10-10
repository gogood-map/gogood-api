package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.MapData;
import gogood.gogoodapi.domain.models.Ocorrencia;
import gogood.gogoodapi.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
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

    @Cacheable(value = "ocorrencias", key = "#latitude + #longitude")
    public Map<String, Object> searchRouteOcorrencias(Double latitude, Double longitude) {
        Point localizacao = new Point(longitude, latitude);
        Distance distancia = new Distance(0.5, Metrics.KILOMETERS);
        Map<String, Object> response = new HashMap<>();
        List<Ocorrencia> ocorrencias = mapRepository.findByLocalizacaoNear(localizacao, distancia);
        List<MapData> ocorrenciasLatLng = new ArrayList<>();
        for (Ocorrencia ocorrencia : ocorrencias) {
            MapData data = new MapData(ocorrencia.getLocalizacao().getX(), ocorrencia.getLocalizacao().getY());
            ocorrenciasLatLng.add(data);
        }
        List<Map<String, Object>> top5Ocorrencias = getTop5Ocorrencias(ocorrencias);
        Map<String, Integer> mes = getMes(ocorrencias);

        response.put("qtdOcorrencias", ocorrencias.size());
        response.put("ocorrencias", ocorrenciasLatLng);
        response.put("top5Ocorrencias", top5Ocorrencias);
        response.put("mesOcorrencias", mes);

        return response;
    }

    public List<Ocorrencia> getOcorrenciasAcrossRoute(Double latitude, Double longitude) {
        Point localizacao = new Point(longitude, latitude);
        Distance distancia = new Distance(0.1, Metrics.KILOMETERS);
        Map<String, Object> response = new HashMap<>();

        return mapRepository.findByLocalizacaoNear(localizacao, distancia);
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

    public Map<String, Integer> getMes(List<Ocorrencia> ocorrencias) {
        Map<String, Integer> crimeData = new HashMap<>();
        Locale locale = Locale.forLanguageTag("pt-BR");
        DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Ocorrencia ocorrencia : ocorrencias) {
            String inputData = ocorrencia.getDataOcorrencia();
            try {
                LocalDate data;
                if (inputData.contains(" ")) {
                    ZonedDateTime zonedDateTime = ZonedDateTime.parse(inputData, fullFormatter);
                    data = zonedDateTime.toLocalDate();
                } else {
                    data = LocalDate.parse(inputData, dateFormatter);
                }

                String mes = data.getMonth().getDisplayName(java.time.format.TextStyle.FULL, locale);
                crimeData.merge(mes, 1, Integer::sum);
            } catch (Exception e) {
                System.err.println("Failed to parse date: " + e.getMessage() + " from input: " + inputData);
            }
        }
        return crimeData;
    }

}
