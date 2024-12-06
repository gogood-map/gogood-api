package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.Coordenada;
import gogood.gogoodapi.domain.models.Etapa;
import gogood.gogoodapi.utils.StringHelper;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GeocodingService {

    private final WebClient webClient;

    @Setter
    private Boolean testFlag = false;

    public GeocodingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.opencagedata.com").build();
    }

    public List<String> buscarLogradouros(List<Etapa> etapas) {
        try {
            return etapas.parallelStream()
                    .map(this::getLogradouro)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getLogradouro(Etapa etapa) {
        Coordenada coordenada = etapa.getCoordenadaFinal();
        String logradouro = testFlag ? fetchLogradouroGoogle(coordenada) : fetchLogradouroOpenCage(coordenada);
        if (logradouro == null) {
            logradouro = testFlag ? fetchLogradouroOpenCage(coordenada) : fetchLogradouroGoogle(coordenada);
        }
        return logradouro != null ? logradouro : "Endereço não encontrado";
    }

    private String fetchLogradouroGoogle(Coordenada coordenada) {
        log.info("Buscando logradouro pelo Google API");
        try {
            Dotenv dotenv = Dotenv.load();
            String googleApiKey = dotenv.get("GOOGLE_API_KEY");
            String uri = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                    .queryParam("latlng", coordenada.getLat() + "," + coordenada.getLng())
                    .queryParam("key", googleApiKey)
                    .queryParam("language", "pt-BR")
                    .toUriString();

            String response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response != null) {
                JSONObject jsonResponse = new JSONObject(response);
                if ("OK".equals(jsonResponse.getString("status"))) {
                    var results = jsonResponse.getJSONArray("results");
                    if (!results.isEmpty()) {
                        JSONObject firstResult = results.getJSONObject(0);
                        var addressComponents = firstResult.getJSONArray("address_components");

                        for (int i = 0; i < addressComponents.length(); i++) {
                            JSONObject component = addressComponents.getJSONObject(i);
                            if (component.getJSONArray("types").toList().contains("route")) {
                                return StringHelper.normalizar(component.getString("long_name"));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar logradouro pelo Google API: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private String fetchLogradouroOpenCage(Coordenada coordenada) {
        log.info("Buscando logradouro pelo OpenCage API");
        try {
            Dotenv dotenv = Dotenv.load();
            String openCageApiKey = dotenv.get("OPENCAGE_API_KEY");

            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/geocode/v1/json")
                            .queryParam("q", coordenada.getLat() + "," + coordenada.getLng())
                            .queryParam("key", openCageApiKey)
                            .queryParam("language", "pt")
                            .queryParam("pretty", "1")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response != null) {
                JSONObject jsonResponse = new JSONObject(response);
                if (!jsonResponse.getJSONArray("results").isEmpty()) {
                    JSONObject components = jsonResponse.getJSONArray("results").getJSONObject(0).getJSONObject("components");
                    if (components.has("road")) {
                        return StringHelper.normalizar(components.getString("road"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao buscar logradouro pelo OpenCage API: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}

