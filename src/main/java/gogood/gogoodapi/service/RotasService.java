package gogood.gogoodapi.service;

import gogood.gogoodapi.utils.GenericConverter;
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
import gogood.gogoodapi.utils.RedisTTL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RotasService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RedisTTL redisTTL;

    public RotasService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RotaShareResponse compartilharRota(RotaSharePersist rota)  {
        String id = UUID.randomUUID().toString();
        String chave = "rotasCompartilhadas:" + id;
        redisTemplate.opsForValue().set(chave, rota);
        redisTTL.setKeyWithExpire(chave, rota, 30, TimeUnit.MINUTES);

        RotaShareResponse response = new RotaShareResponse();
        response.setUrl(id);
        return response;
    }

    public List<Rota> obterRotaCompartilhada(String id, RotaMapper rotaMapper) {
        Object objRedis = redisTemplate.opsForValue().get("rotasCompartilhadas:" + id);
        RotaSharePersist rota = GenericConverter.convert(objRedis, RotaSharePersist.class);
        Boolean isRota = TipoTransporteEnum.istipoTransporte(rota.getTipoTransporte());

        if (isRota) {
            NavegacaoService navegacaoService = new NavegacaoService();
            RotaStrategy estrategiaRota = switch (rota.getTipoTransporte().toUpperCase()) {
                case "TRANSPORTE_PUBLICO" -> new TransportePublicoStrategy(rotaMapper);
                case "BIKE" -> new BicicletaStrategy(rotaMapper);
                case "VEICULO" -> new VeiculoStrategy(rotaMapper);
                case "A_PE" -> new APeStrategy(rotaMapper);
                default -> null;
            };
            navegacaoService.escolherStrategy(estrategiaRota);
            return navegacaoService.montarRotas(rota.getOrigem(), rota.getDestino());
        } else {
            return null;
        }
    }
}
