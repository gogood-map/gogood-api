package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.rotas.Rota;
import gogood.gogoodapi.domain.strategy.RotaStrategy;
import gogood.gogoodapi.utils.Ordenacao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class NavegacaoService {
    @Cacheable(value = "rotas", key = "#origem.concat('-').concat(#destino)", unless = "#result == null")
    public List<Rota> escolherStrategy(String id, RotaStrategy estrategiaRota, String origem, String destino) {
        return montarRotas(origem, destino, estrategiaRota);
    }


    public List<Rota> montarRotas(String localidadeOrigem, String localidadeDestino, RotaStrategy estrategiaRota) {
        List<Rota> rotas = estrategiaRota.montarRota(localidadeOrigem, localidadeDestino);
        return Ordenacao.ordenarRotaPorDuracao(rotas.toArray(new Rota[rotas.size()-1]));
    }

}
