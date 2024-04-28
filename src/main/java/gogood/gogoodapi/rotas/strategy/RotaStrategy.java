package gogood.gogoodapi.rotas.strategy;

import gogood.gogoodapi.rotas.models.Rota;

import java.util.List;

public interface RotaStrategy {
    List<Rota> montarRota(String localidadeOrigem, String localidadeDestino);
}
