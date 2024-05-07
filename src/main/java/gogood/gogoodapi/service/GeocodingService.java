package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.Coordenada;
import gogood.gogoodapi.domain.models.Etapa;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class GeocodingService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public GeocodingService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public List<String> buscarLogradouros(List<Etapa> etapas){

        RestClient restClient = RestClient.create();
        List<String> logradouros = new ArrayList<>();


        for (Etapa etapa:etapas){
            Coordenada coordenada = etapa.getCoordenadaFinal();

            if(redisTemplate.hasKey(coordenada.toString())){
                String ruaRedis =  redisTemplate.opsForValue().get(coordenada.toString()).toString();
                logradouros.add(ruaRedis);
            }else{
                var requisicao = restClient.get()
                        .uri("https://nominatim.openstreetmap.org/reverse.php?lat=%s&lon=%s&zoom=18&format=jsonv2".formatted(
                                coordenada.getLat(),coordenada.getLng()
                        )).retrieve().toEntity(String.class).getBody();;
                JSONObject jo = new JSONObject(requisicao);
                try{
                    var rua = jo.getJSONObject("address").getString("road");
                    redisTemplate.opsForValue().set(coordenada.toString(), rua);
                    setKeyWithExpire(coordenada.toString(), rua, 30, TimeUnit.MINUTES);
                    logradouros.add(rua);
                }catch (Exception ignored){
                }
            }
        }


        return logradouros.stream().distinct().toList();
    }

    public void setKeyWithExpire(String key, String value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, timeout, timeUnit);
    }
}
