package gogood.gogoodapi.strategys;

import gogood.gogoodapi.models.Rota;

public interface RotaStrategy {
    Rota montarRota(String localidadeOrigem, String localidadeDestino);
}
