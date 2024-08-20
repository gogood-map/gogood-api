package gogood.gogoodapi.service;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public class ClientGoogleMaps {
    public static GeoApiContext gerarContextoClient(){
        Dotenv dotenv = Dotenv.load();
        String googleApiKey = dotenv.get("GOOGLE_API_KEY");

        return new GeoApiContext.Builder()
                .apiKey(googleApiKey)
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