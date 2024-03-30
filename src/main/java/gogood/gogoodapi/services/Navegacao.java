package gogood.gogoodapi.services;

import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.strategy.RotaStrategy;
import gogood.gogoodapi.utils.Ordenacao;

import java.util.List;

public class Navegacao {

    private RotaStrategy estrategiaRota;

    public Navegacao(RotaStrategy estrategiaRota) {
        escolherStrategy(estrategiaRota);
    }
    public Navegacao() {}

    public void escolherStrategy(RotaStrategy estrategiaRota){
        this.estrategiaRota = estrategiaRota;
    }

    public List<Rota> montarRotas(String localidadeOrigem, String localidadeDestino){
        List<Rota> rotas = estrategiaRota.montarRota(localidadeOrigem, localidadeDestino);

        return Ordenacao.ordenarRotaPorDuracao(rotas.toArray(new Rota[rotas.size()-1]));
    }

}
