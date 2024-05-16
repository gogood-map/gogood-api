package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.Coordenada;
import gogood.gogoodapi.domain.models.Etapa;
import gogood.gogoodapi.utils.RedisTTL;
import org.json.JSONObject;
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
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTTL redisTTL;

    private static final int MAX_CONCURRENT_REQUESTS = 2;


    public GeocodingService(WebClient.Builder webClientBuilder, RedisTemplate<String, String> redisTemplate, RedisTTL redisTTL) {
        this.webClient = webClientBuilder.baseUrl("https://nominatim.openstreetmap.org").build();
        this.redisTemplate = redisTemplate;
        this.redisTTL = redisTTL;
    }

    public Flux<String> buscarLogradouros(List<Etapa> etapas) {
        return Flux.fromIterable(etapas)
                .flatMap(this::getLogradouro, MAX_CONCURRENT_REQUESTS)
                .distinct();
    }

    private Mono<String> getLogradouro(Etapa etapa) {
        Coordenada coordenada = etapa.getCoordenadaFinal();
        String key = coordenada.toString();

        return Mono.justOrEmpty(redisTemplate.opsForValue().get(key))
                .switchIfEmpty(fetchAndCacheLogradouro(coordenada, key));
    }

    private Mono<String> fetchAndCacheLogradouro(Coordenada coordenada, String key) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/reverse.php")
                        .queryParam("lat", coordenada.getLat())
                        .queryParam("lon", coordenada.getLng())
                        .queryParam("zoom", "18")
                        .queryParam("format", "jsonv2")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(response -> {
                    try {
                        JSONObject jo = new JSONObject(response);
                        String rua = jo.getJSONObject("address").getString("road");
                        redisTemplate.opsForValue().set(key, rua);
                        redisTTL.setKeyWithExpire(key, rua, 30, TimeUnit.MINUTES);
                        return Mono.just(rua);
                    } catch (Exception e) {
                        return Mono.empty();
                    }
                });
    }
}
