package gogood.gogoodapi.repository;

import gogood.gogoodapi.models.QuantidadeOcorrenciaPorCep;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OcorrenciasRuasRepository extends MongoRepository<QuantidadeOcorrenciaPorCep, String> {

    Optional<QuantidadeOcorrenciaPorCep> findBy_id(String _id);
}
