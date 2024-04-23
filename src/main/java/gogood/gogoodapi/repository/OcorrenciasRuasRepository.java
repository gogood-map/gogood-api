package gogood.gogoodapi.repository;

import gogood.gogoodapi.models.QuantidadeOcorrenciaPorCep;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OcorrenciasRuasRepository extends MongoRepository<QuantidadeOcorrenciaPorCep, String> {
    Integer findByIdIs(String id);
}
