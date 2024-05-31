package gogood.gogoodapi.repository;

import java.util.List;

public interface CustomQuantidadeOcorrenciaRuaRepository {
    Integer getTotalOccurrencesByStreets(List<String> streets);
}
