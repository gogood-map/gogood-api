package gogood.gogoodapi.rotas.strategy.rotaStrategy;

import com.google.maps.model.TravelMode;
import gogood.gogoodapi.rotas.mapper.RotaMapper;
import gogood.gogoodapi.rotas.models.Rota;
import gogood.gogoodapi.rotas.services.ClientGoogleMaps;
import gogood.gogoodapi.rotas.strategy.RotaStrategy;

import java.util.List;
public class APeStrategy implements RotaStrategy {
    RotaMapper rotaMapper;
    public APeStrategy(RotaMapper rotaMapper) {
        this.rotaMapper = rotaMapper;
    }
    @Override
    public List<Rota> montarRota(String localidadeOrigem, String localidadeDestino) {
        return rotaMapper.toRota(ClientGoogleMaps.obterRespostaRota(
                localidadeOrigem, localidadeDestino, TravelMode.WALKING
        )).block();
    }
}
