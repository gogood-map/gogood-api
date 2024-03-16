package gogood.gogoodapi.strategys.rotaStrategy;

import com.google.maps.model.TravelMode;
import gogood.gogoodapi.adapters.RotaAdapter;
import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.services.ClientGoogleMaps;
import gogood.gogoodapi.strategys.RotaStrategy;

public class VeiculoStrategy implements RotaStrategy {
    @Override
    public Rota montarRota(String localidadeOrigem, String localidadeDestino) {
        return RotaAdapter.transformarRota(ClientGoogleMaps.obterRespostaRota(
                localidadeOrigem, localidadeDestino, TravelMode.DRIVING
        ).routes[0].legs[0]);
    }
}
