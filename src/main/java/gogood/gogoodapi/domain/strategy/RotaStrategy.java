package gogood.gogoodapi.domain.strategy;

import gogood.gogoodapi.domain.models.rotas.Rota;

import java.util.List;

public interface RotaStrategy {
    List<Rota> montarRota(String localidadeOrigem, String localidadeDestino);
}
