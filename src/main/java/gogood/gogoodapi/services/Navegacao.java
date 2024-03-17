package gogood.gogoodapi.services;

import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.strategys.RotaStrategy;
import gogood.gogoodapi.utils.Ordenacao;

import java.util.Arrays;
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

        Ordenacao.ordenarRotaPorDuracao(rotas.toArray(new Rota[rotas.size()-1]));

        return rotas;
    }
}
