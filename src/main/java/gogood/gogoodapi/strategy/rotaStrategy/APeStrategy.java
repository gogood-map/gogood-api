package gogood.gogoodapi.strategy.rotaStrategy;

import com.google.maps.model.TravelMode;
import gogood.gogoodapi.adapters.RotaAdapter;
import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.services.ClientGoogleMaps;
import gogood.gogoodapi.strategy.RotaStrategy;

import java.util.List;

public class APeStrategy implements RotaStrategy {
    @Override
    public List<Rota> montarRota(String localidadeOrigem, String localidadeDestino) {
        return RotaAdapter.transformarRota(ClientGoogleMaps.obterRespostaRota(
                localidadeOrigem, localidadeDestino, TravelMode.WALKING
        ));
    }
}
