package gogood.gogoodapi.repository;

import java.util.List;

public interface CustomOcorrenciaRuaRepository {
    Integer getTotalOccurrencesByStreets(List<String> streets);
}
