package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.Coordenada;
import gogood.gogoodapi.domain.models.Etapa;
import gogood.gogoodapi.utils.RedisTTL;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class GeocodingService {

    private final WebClient webClient;
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final RedisTTL redisTTL;

    public GeocodingService(WebClient.Builder webClientBuilder, ReactiveRedisTemplate<String, String> redisTemplate, RedisTTL redisTTL) {
        this.webClient = webClientBuilder.baseUrl("https://api.opencagedata.com").build();
        this.redisTemplate = redisTemplate;
        this.redisTTL = redisTTL;
    }

    public Flux<String> buscarLogradouros(List<Etapa> etapas) {
        return Flux.fromIterable(etapas)
                .flatMap(this::getLogradouro)
                .distinct();
    }

    private Mono<String> getLogradouro(Etapa etapa) {
        Coordenada coordenada = etapa.getCoordenadaFinal();
        String key = coordenada.toString();

        return redisTemplate.opsForValue().get(key)
                .switchIfEmpty(fetchAndCacheLogradouro(coordenada, key));
    }

    private Mono<String> fetchAndCacheLogradouro(Coordenada coordenada, String key) {
        Dotenv dotenv = Dotenv.load();
        String openCageApiKey = dotenv.get("OPENCAGE_API_KEY");

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geocode/v1/json")
                        .queryParam("q", coordenada.getLat() + "," + coordenada.getLng())
                        .queryParam("key", openCageApiKey)
                        .queryParam("language", "pt")
                        .queryParam("pretty", "1")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (!jsonResponse.getJSONArray("results").isEmpty()) {
                        JSONObject components = jsonResponse.getJSONArray("results").getJSONObject(0).getJSONObject("components");
                        if (components.has("road")) {
                            return components.getString("road"); // Pega o nome da rua
                        }
                    }
                    return "Endereço não encontrado"; // Caso não encontre a rua ou a resposta seja vazia
                })
                .doOnSuccess(locality -> redisTTL.setKeyWithExpire(key, locality, 3600, TimeUnit.SECONDS));
    }
}
