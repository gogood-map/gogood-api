package gogood.gogoodapi.services;

import gogood.gogoodapi.models.Rota;
import gogood.gogoodapi.strategys.RotaStrategy;

public class Navegacao {

    private RotaStrategy estrategiaRota;

    public Navegacao(RotaStrategy estrategiaRota) {
        escolherStrategy(estrategiaRota);
    }
    public Navegacao() {}

    public void escolherStrategy(RotaStrategy estrategiaRota){
        this.estrategiaRota = estrategiaRota;
    }

    public Rota montarRota(String localidadeOrigem, String localidadeDestino){
       return estrategiaRota.montarRota(localidadeOrigem, localidadeDestino);
    }
}
