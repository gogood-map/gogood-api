package gogood.gogoodapi.rotas.repository;

import gogood.gogoodapi.rotas.models.QuantidadeOcorrenciaPorCep;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OcorrenciasRuasRepository extends MongoRepository<QuantidadeOcorrenciaPorCep, String> {

    Optional<QuantidadeOcorrenciaPorCep> findBy_id(String _id);
}
