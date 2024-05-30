package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.Ocorrencia;
import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRua;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface OcorrenciaRepository extends MongoRepository<Ocorrencia, String> {
    @Query(value = "{'bairro': ?0,'cidade': ?1}")
List<Ocorrencia> findAllByBairroAndCidade(String bairro, String cidade);
@Query(value = "{'bairro': ?0,'cidade': ?1}", fields = "{rua: 1}")
List<String> findRuasPorBairroECidade(String bairro, String cidade);
}
