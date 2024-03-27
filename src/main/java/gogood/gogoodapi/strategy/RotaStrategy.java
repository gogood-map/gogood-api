package gogood.gogoodapi.strategy;

import gogood.gogoodapi.models.Rota;

import java.util.List;

public interface RotaStrategy {
    List<Rota> montarRota(String localidadeOrigem, String localidadeDestino);
}
