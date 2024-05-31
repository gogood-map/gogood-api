package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.InfoLogradouro;
import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRua;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface QuantidadeOcorrenciaRuaRepository extends MongoRepository<QuantidadeOcorrenciaRua, InfoLogradouro> {

    @Query(value = "{ '_id.cidade': ?0 , '_id.bairro':  ?1}")
    List<QuantidadeOcorrenciaRua> listarQuantidadeOcorrenciaRuaPorBairroECidade(String cidade, String bairro);


}
