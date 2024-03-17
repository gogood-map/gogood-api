package gogood.gogoodapi.strategys.rotaStrategy;

import com.google.maps.model.TravelMode;
import gogood.gogoodapi.adapters.RotaAdapter;
import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.services.ClientGoogleMaps;
import gogood.gogoodapi.strategys.RotaStrategy;

import java.util.List;

public class TransportePublicoStrategy implements RotaStrategy {
    @Override
    public List<Rota> montarRota(String localidadeOrigem, String localidadeDestino) {
        return RotaAdapter.transformarRota(ClientGoogleMaps.obterRespostaRota(
                localidadeOrigem, localidadeDestino, TravelMode.TRANSIT
        ));
    }
}
