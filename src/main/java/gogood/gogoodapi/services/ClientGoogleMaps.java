package gogood.gogoodapi.services;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import gogood.gogoodapi.models.Coordenada;
import org.hibernate.cfg.Environment;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ClientGoogleMaps {

    public static GeoApiContext gerarContextoClient(){
        return new GeoApiContext.Builder()
                .apiKey(System.getenv("GOOGLE_API_KEY"))
                .build();
    }
    //Rotas
    public static DirectionsApiRequest gerarRequestRota(){
        DirectionsApiRequest request = DirectionsApi.newRequest(gerarContextoClient());
        request.alternatives(true);
        request.optimizeWaypoints(true);
        request.units(Unit.METRIC);
        request.region("br");
        request.language("pt-BR");


        return request;
    }

    public static DirectionsResult obterRespostaRota(String origem, String destino, TravelMode modoTransporte) {
        DirectionsApiRequest request = gerarRequestRota();
        request.mode(modoTransporte);
        request.destination(destino);
        request.origin(origem);
        return request.awaitIgnoreError();
    }
}
