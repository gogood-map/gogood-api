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
    private static final int MAX_CONCURRENT_REQUESTS = 3;

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
        String bigDataCloudApiKey = "bdc_5f1d8acad0824e15ae424ede15963beb"; // Substitua com seu token real

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.bigdatacloud.net")
                        .path("/data/reverse-geocode-client")
                        .queryParam("latitude", coordenada.getLat())
                        .queryParam("longitude", coordenada.getLng())
                        .queryParam("localityLanguage", "pt")
                        .queryParam("key", bigDataCloudApiKey)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> new JSONObject(response).getString("locality"))
                .doOnSuccess(locality -> redisTTL.setKeyWithExpire(key, locality, 3600, TimeUnit.SECONDS)); // Supondo TTL de 3600 segundos
    }

}