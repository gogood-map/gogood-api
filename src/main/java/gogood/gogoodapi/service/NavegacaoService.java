package gogood.gogoodapi.service;

import gogood.gogoodapi.domain.models.rotas.Rota;
import gogood.gogoodapi.domain.strategy.RotaStrategy;
import gogood.gogoodapi.utils.Ordenacao;

import java.util.List;

public class NavegacaoService {

    private RotaStrategy estrategiaRota;

    public NavegacaoService(RotaStrategy estrategiaRota) {
        escolherStrategy(estrategiaRota);
    }
    public NavegacaoService() {}

    public void escolherStrategy(RotaStrategy estrategiaRota){
        this.estrategiaRota = estrategiaRota;
    }

    public List<Rota> montarRotas(String localidadeOrigem, String localidadeDestino){
        List<Rota> rotas = estrategiaRota.montarRota(localidadeOrigem, localidadeDestino);

        return Ordenacao.ordenarRotaPorDuracao(rotas.toArray(new Rota[rotas.size()-1]));
    }

}
