package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.Coordenada;
import gogood.gogoodapi.domain.models.Etapa;
import gogood.gogoodapi.utils.RedisTTL;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class GeocodingService {

    private final WebClient webClient;
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTTL redisTTL;

    public GeocodingService(WebClient.Builder webClientBuilder, RedisTemplate<String, String> redisTemplate, RedisTTL redisTTL) {
        this.webClient = webClientBuilder.baseUrl("https://api.opencagedata.com").build();
        this.redisTemplate = redisTemplate;
        this.redisTTL = redisTTL;
    }

    public List<String> buscarLogradouros(List<Etapa> etapas) {
        return etapas.parallelStream()
                .map(this::getLogradouro)
                .distinct()
                .collect(Collectors.toList());
    }

    private String getLogradouro(Etapa etapa) {
        Coordenada coordenada = etapa.getCoordenadaFinal();
        String key = coordenada.toString();

        String cachedLogradouro = redisTemplate.opsForValue().get(key);
        if (cachedLogradouro != null) {
            return cachedLogradouro;
        }

        String logradouro = fetchAndCacheLogradouro(coordenada, key);
        return logradouro != null ? logradouro : "Endereço não encontrado";
    }

    private String fetchAndCacheLogradouro(Coordenada coordenada, String key) {
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
                    String road = components.getString("road");
                    redisTTL.setKeyWithExpire(key, road, 3600, TimeUnit.SECONDS);
                    return road;
                }
            }
        }

        return null;
    }
}
