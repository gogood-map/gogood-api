package gogood.gogoodapi.repository;

import gogood.gogoodapi.domain.models.InfoRegiaoAnoMes;
import gogood.gogoodapi.domain.models.QuantidadeOcorrenciaRegiaoAnoMes;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface QuantidadeOcorrenciaRegiaoRepository extends MongoRepository<QuantidadeOcorrenciaRegiaoAnoMes,InfoRegiaoAnoMes> {
    List<QuantidadeOcorrenciaRegiaoAnoMes>
    findFirst12ByInfoRegiaoAnoMes_AnoMesNotContainsAndInfoRegiaoAnoMes_CidadeAndInfoRegiaoAnoMes_BairroOrderByInfoRegiaoAnoMes_AnoMesDesc
            (String nan,String cidade, String bairro);
}
