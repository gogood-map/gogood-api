package gogood.gogoodapi.domain.strategy.rotaStrategy;

import com.google.maps.model.TravelMode;
import gogood.gogoodapi.domain.mappers.RotaMapper;
import gogood.gogoodapi.domain.models.Rota;
import gogood.gogoodapi.service.ClientGoogleMaps;
import gogood.gogoodapi.domain.strategy.RotaStrategy;

import java.util.List;

public class TransportePublicoStrategy implements RotaStrategy {

    RotaMapper rotaMapper;

    public TransportePublicoStrategy(RotaMapper rotaMapper) {
        this.rotaMapper = rotaMapper;
    }

    @Override
    public List<Rota> montarRota(String localidadeOrigem, String localidadeDestino) {
        return rotaMapper.toRota(ClientGoogleMaps.obterRespostaRota(
                localidadeOrigem, localidadeDestino, TravelMode.TRANSIT
        ));
    }
}
