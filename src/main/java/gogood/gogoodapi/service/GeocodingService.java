package gogood.gogoodapi.service;

import gogood.gogoodapi.configuration.redis.RedisHealthCheck;
import gogood.gogoodapi.domain.models.Coordenada;
import gogood.gogoodapi.domain.models.Etapa;
import gogood.gogoodapi.utils.RedisTTL;
import gogood.gogoodapi.utils.StringHelper;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

    private RedisHealthCheck redisHealthCheck;

    public GeocodingService(WebClient.Builder webClientBuilder, RedisTemplate<String, String> redisTemplate, RedisTTL redisTTL, RedisHealthCheck redisHealthCheck) {
        this.webClient = webClientBuilder.baseUrl("https://api.opencagedata.com").build();
        this.redisTemplate = redisTemplate;
        this.redisTTL = redisTTL;
        this.redisHealthCheck = redisHealthCheck;
    }

    public List<String> buscarLogradouros(List<Etapa> etapas) {
        try{
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
        if (!redisHealthCheck.isRedisUp()) {
            return "Problema ao acessar o Redis, tente novamente";
        }
        ValueOperations<String, String> valueOps = redisTemplate.opsForValue();


        Coordenada coordenada = etapa.getCoordenadaFinal();
        String key = coordenada.toString();

        String cachedLogradouro = valueOps.get(key);
        if (cachedLogradouro != null) {
            return StringHelper.normalizar(redisTemplate.opsForValue().get(key));
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
                    road = StringHelper.normalizar(road);
                    redisTTL.setKeyWithExpire(key, road, 30, TimeUnit.MINUTES);
                    return road;
                }
            }
        }

        return null;
    }
}
