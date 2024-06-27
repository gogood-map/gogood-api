package gogood.gogoodapi.service;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ClientGoogleMaps {
    public static GeoApiContext gerarContextoClient(){
        return new GeoApiContext.Builder()
                .apiKey("AIzaSyA9cd3e1BftlFXFnei_GU-mMQrm8WgH_ko")
                .build();
    }
    //Rotas
    public static DirectionsApiRequest gerarRequestRota(){
        DirectionsApiRequest request = DirectionsApi.newRequest(gerarContextoClient());
        request.alternatives(true);
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
        try {
            return request.await();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), "Não foi possível gerar rota. Detalhe mais a origem/destino ou tente novamente mais tarde");
        }
    }
}
