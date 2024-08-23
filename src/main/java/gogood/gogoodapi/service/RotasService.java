package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.DTOS.RotaSharePersist;
import gogood.gogoodapi.domain.models.rotas.RotaShareResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RotasService {
    @Cacheable(value = "rotasCompartilhadas", unless = "#result == null")
    public RotaShareResponse compartilharRota(RotaSharePersist rota)  {
        RotaShareResponse response = new RotaShareResponse();
        String id = UUID.randomUUID().toString();
        response.setId(id);
        response.setUrl(id);
        return response;
    }

//    public List<Rota> obterRotaCompartilhada(String id, RotaMapper rotaMapper) {
//        Object objRedis = redisTemplate.opsForValue().get("rotasCompartilhadas:" + id);
//        RotaSharePersist rota = GenericConverter.convert(objRedis, RotaSharePersist.class);
//        Boolean isRota = TipoTransporteEnum.istipoTransporte(rota.getTipoTransporte());
//
//        if (isRota) {
//            NavegacaoService navegacaoService = new NavegacaoService();
//            RotaStrategy estrategiaRota = switch (rota.getTipoTransporte().toUpperCase()) {
//                case "TRANSPORTE_PUBLICO" -> new TransportePublicoStrategy(rotaMapper);
//                case "BIKE" -> new BicicletaStrategy(rotaMapper);
//                case "VEICULO" -> new VeiculoStrategy(rotaMapper);
//                case "A_PE" -> new APeStrategy(rotaMapper);
//                default -> null;
//            };
//            navegacaoService.escolherStrategy(estrategiaRota);
//            return navegacaoService.montarRotas(rota.getOrigem(), rota.getDestino());
//        } else {
//            return null;
//        }
//    }
}
