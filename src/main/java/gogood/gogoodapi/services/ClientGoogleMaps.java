package gogood.gogoodapi.services;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import java.io.IOException;

public class ClientGoogleMaps {
    public static GeoApiContext gerarContextoClient(){
        return new GeoApiContext.Builder()
                .apiKey("AIzaSyB4oSH_8oIusSaAyZRRPXkptJSgwTsPnvw")
                .build();
    }

    public static DirectionsApiRequest gerarRequestRota(){
        DirectionsApiRequest request = DirectionsApi.newRequest(gerarContextoClient());
        request.alternatives(true);

        request.units(Unit.METRIC);
        request.region("br");
        request.language("pt-BR");

        return request;
    }
    public static DirectionsResult obterRespostaRota(String origem, String destino, TravelMode modoTransporte){
        DirectionsApiRequest request = gerarRequestRota();
        request.mode(modoTransporte);
        request.destination(destino);
        request.origin(origem);
        return request.awaitIgnoreError();
    }
}
