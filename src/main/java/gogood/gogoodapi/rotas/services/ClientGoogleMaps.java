package gogood.gogoodapi.rotas.services;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

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
        return request.awaitIgnoreError();
    }
}
