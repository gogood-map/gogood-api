package gogood.gogoodapi.services;

import com.mapbox.geojson.Point;
import gogood.gogoodapi.models.Coordenada;
import gogood.gogoodapi.models.Etapa;
import org.json.JSONObject;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class AzureMapsService{


    public Mono<List<String>> buscarCeps(List<Etapa> etapas){
        
        return Mono.just(ceps.stream().distinct().toList());
    }
}
