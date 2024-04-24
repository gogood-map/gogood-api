package gogood.gogoodapi.strategy.rotaStrategy;

import com.google.maps.model.TravelMode;
import gogood.gogoodapi.adapters.RotaAdapter;
import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.services.ClientGoogleMaps;
import gogood.gogoodapi.strategy.RotaStrategy;

import java.util.List;

public class VeiculoStrategy implements RotaStrategy {
    RotaAdapter rotaAdapter;

    public VeiculoStrategy(RotaAdapter rotaAdapter) {
        this.rotaAdapter = rotaAdapter;
    }

    @Override
    public List<Rota> montarRota(String localidadeOrigem, String localidadeDestino) {
        return rotaAdapter.transformarRotas(ClientGoogleMaps.obterRespostaRota(
                localidadeOrigem, localidadeDestino, TravelMode.DRIVING
        )).block();
    }
}
