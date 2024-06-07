package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.Coordenada;
import gogood.gogoodapi.domain.models.Etapa;
import gogood.gogoodapi.utils.StringHelper;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeocodingService {

    private final WebClient webClient;

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
        String logradouro = fetchLogradouro(coordenada);
        return logradouro != null ? logradouro : "Endereço não encontrado";
    }

    private String fetchLogradouro(Coordenada coordenada) {
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

        return null;
    }
}
