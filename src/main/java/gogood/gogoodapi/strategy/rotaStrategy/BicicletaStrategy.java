package gogood.gogoodapi.strategy.rotaStrategy;

import com.google.maps.model.TravelMode;
import gogood.gogoodapi.adapters.RotaAdapter;
import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.services.ClientGoogleMaps;
import gogood.gogoodapi.strategy.RotaStrategy;

import java.util.List;

public class BicicletaStrategy implements RotaStrategy {
    @Override
    public List<Rota> montarRota(String localidadeOrigem, String localidadeDestino) {
        return new RotaAdapter().transformarRotas(ClientGoogleMaps.obterRespostaRota(
                localidadeOrigem, localidadeDestino, TravelMode.BICYCLING
        )).block();
    }
}
