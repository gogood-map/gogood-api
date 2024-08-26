package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.DTOS.RotaSharePersist;
import gogood.gogoodapi.domain.enums.TipoTransporteEnum;
import gogood.gogoodapi.domain.mappers.RotaMapper;
import gogood.gogoodapi.domain.models.rotas.Rota;
import gogood.gogoodapi.domain.models.rotas.RotaShareResponse;
import gogood.gogoodapi.domain.strategy.RotaStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.APeStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.BicicletaStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.TransportePublicoStrategy;
import gogood.gogoodapi.domain.strategy.rotaStrategy.VeiculoStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RotasService {
    @Autowired
    RotasService rotasService;
    private final NavegacaoService navegacaoService;

    public RotasService(NavegacaoService navegacaoService) {
        this.navegacaoService = navegacaoService;
    }

    public RotaShareResponse processarRotaCompartilhada(String id, RotaSharePersist rota, RotaMapper rotaMapper){
        List<Rota> rotas = rotasService.compartilharRota(id, rota, rotaMapper);
        RotaShareResponse rotaShareResponse = new RotaShareResponse();
        rotaShareResponse.setUrl(id);
        return rotaShareResponse;
    }

    @CachePut(value = "rotasCompartilhadas", key = "#id", unless = "#result == null")
    public List<Rota> compartilharRota(String id, RotaSharePersist rota, RotaMapper rotaMapper)  {
        Boolean isRota = TipoTransporteEnum.isTipoTransporte(rota.getTipoTransporte());

        if (isRota) {
            RotaStrategy estrategiaRota = escolherStrategy(rota, rotaMapper);
            return navegacaoService.montarRotas(rota.getOrigem(), rota.getDestino(), Objects.requireNonNull(estrategiaRota));
        } else {
            return null;
        }
    }
    private RotaStrategy escolherStrategy(RotaSharePersist rota, RotaMapper rotaMapper) {
        return switch (rota.getTipoTransporte().toUpperCase()) {
            case "TRANSPORTE_PUBLICO" -> new TransportePublicoStrategy(rotaMapper);
            case "BIKE" -> new BicicletaStrategy(rotaMapper);
            case "VEICULO" -> new VeiculoStrategy(rotaMapper);
            case "A_PE" -> new APeStrategy(rotaMapper);
            default -> null;
        };
    }

    @Cacheable(value = "rotasCompartilhadas", key = "#id")
    public List<Rota> obterRotaCompartilhada(String id) {
        return null;
    }


}
