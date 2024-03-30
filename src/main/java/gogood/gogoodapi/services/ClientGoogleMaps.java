package gogood.gogoodapi.services;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import gogood.gogoodapi.models.Coordenada;

import java.io.IOException;

public class ClientGoogleMaps {
    public static GeoApiContext gerarContextoClient(){
        return new GeoApiContext.Builder()
                .apiKey("AIzaSyB4oSH_8oIusSaAyZRRPXkptJSgwTsPnvw")
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

    public static DirectionsResult obterRespostaRota(String origem, String destino, TravelMode modoTransporte){
        DirectionsApiRequest request = gerarRequestRota();
        request.mode(modoTransporte);
        request.destination(destino);
        request.origin(origem);
        return request.awaitIgnoreError();
    }
    // Geolocalização
    public static String obterLogradouroEndereco(Coordenada coordenada){
        LatLng latLng = new LatLng(coordenada.getLat(), coordenada.getLng());
        GeocodingResult[] request = GeocodingApi.reverseGeocode(gerarContextoClient(), latLng).awaitIgnoreError();
        return request[0].formattedAddress;
    }
}
