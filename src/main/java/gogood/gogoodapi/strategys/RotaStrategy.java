package gogood.gogoodapi.strategys;

import gogood.gogoodapi.models.Rota;

import java.util.List;

public interface RotaStrategy {
    List<Rota> montarRota(String localidadeOrigem, String localidadeDestino);
}
